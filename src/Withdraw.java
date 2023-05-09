import com.google.zxing.WriterException;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

/*
    Class that contains code for the withdrawal screen.
    Lets users decide on the day and also the amount of
    money to withdraw.
 */

public class Withdraw implements Draw{
    protected JFrame withdrawFrm = new JFrame();
    protected PreparedStatement pst = null;
    protected Connection con = null;
    protected ResultSet rs = null;
    protected File currentDir = new File("");
    protected String path = currentDir.getAbsolutePath();
    Withdraw(JFrame frame, int cust_ID){
        drawForm();
        drawLabels(frame, cust_ID);
        drawElements();
        drawMenu(cust_ID);
    }

    @Override
    public void drawForm() { // draws the main screen
        withdrawFrm.setTitle("Welcome!");
        withdrawFrm.setLayout(null);
        withdrawFrm.setVisible(true);
        withdrawFrm.setSize(750,650);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - withdrawFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - withdrawFrm.getHeight()) / 2);
        // sets the location of the frame so that it's always centered
        withdrawFrm.setLocation(x, y);
        withdrawFrm.getContentPane().setBackground(Auxiliary.mainColor);
    }

    @Override
    public void drawMenu(int ID) { // draws the menu bar on the upper portion of the frame
        JMenu menu = new JMenu("Options");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1, item2;

        // When clicked, users are taken to the login screen
        item1 = new JMenuItem("Logout");
        item1.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(withdrawFrm, "Are you sure you want to logout?");
            if (a == JOptionPane.YES_OPTION){
                withdrawFrm.dispose();
                new Login();
            }
        });

        // When clicked, prompts users with a confirmation dialog for exiting
        item2 = new JMenuItem("Exit ");
        item2.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(withdrawFrm, "Are you sure you want to exit?");
            if(a== JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });
        menuBar.setBackground(Auxiliary.highlightColor);
        menu.setFont(Auxiliary.font_14);
        item1.setFont(Auxiliary.font_14);
        item2.setFont(Auxiliary.font_14);

        menu.add(item1);
        menu.add(item2);
        menuBar.add(menu);
        withdrawFrm.setJMenuBar(menuBar);
    }

    // draws the labels in the frame
    // userID is the user's customer_ID in the database,
    // prev is the frame for the account screen
    public void drawLabels(JFrame prev, int userID) {
        JLabel title = new JLabel("Withdraw");
        JLabel subtitle = new JLabel("Please input the necessary details");

        title.setBounds(20,215,200,60);
        title.setFont(Auxiliary.font_20);
        subtitle.setBounds(20,245,300,60);
        subtitle.setFont(Auxiliary.font_17);
        withdrawFrm.add(title);
        withdrawFrm.add(subtitle);

        JLabel customer_ID = new JLabel("Customer ID:");
        customer_ID.setBounds(75,325,150,30);
        customer_ID.setFont(Auxiliary.font_17);

        JLabel date = new JLabel("Planned date of withdrawal:");
        date.setBounds(75,375,250,30);
        date.setFont(Auxiliary.font_17);

        JLabel amount = new JLabel("Amount to withdraw:");
        amount.setBounds(75,430,170,30);
        amount.setFont(Auxiliary.font_17);

        String displayID = Integer.toString(userID);
        JTextField idField = new JTextField(displayID);
        idField.setBounds(305,330,200,25);
        idField.setBackground(Auxiliary.subColor);
        idField.setFont(Auxiliary.font_17);
        idField.setEditable(false);

        // Mandatory code for implementing a JDatePicker, which is used
        // by users to pick dates on when they want to withdraw
        UtilDateModel model = new UtilDateModel();
        Properties dateProperty = new Properties();
        dateProperty.put("text.today", "Today");
        dateProperty.put("text.month", "Month");
        dateProperty.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, dateProperty);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new Auxiliary.DateLabelFormatter());
        datePicker.setBounds(305,380,200,30);

        JTextField amountField = new JTextField();
        amountField.setBounds(305,440,200,25);
        amountField.setFont(Auxiliary.font_16);
        amountField.setBackground(Auxiliary.subColor);

        JButton generate = new JButton("Generate QR Code");
        generate.setBounds(400,510, 175,27);
        generate.setFont(Auxiliary.font_16);
        generate.setBackground(Auxiliary.subColor);

        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(585,510,100,27);
        returnBtn.setFont(Auxiliary.font_16);
        returnBtn.setBackground(Auxiliary.subColor);

        withdrawFrm.add(customer_ID);
        withdrawFrm.add(date);
        withdrawFrm.add(amount);
        withdrawFrm.add(idField);
        withdrawFrm.add(datePicker);
        withdrawFrm.add(amountField);
        // puts focus on the field where users input the amount of money they want to withdraw
        amountField.requestFocus();
        withdrawFrm.add(generate);
        withdrawFrm.add(returnBtn);

        // When clicked, will log the information that the user typed into the fields
        // and create a qr code based on it.
        generate.addActionListener(e -> {
            String moneyDisplay = amountField.getText();
            Object userDate = datePicker.getJFormattedTextField().getValue();

            // Shows a message dialog for when users try to generate a qr code but
            // don't input anything/input incorrectly in the required fields
            if (moneyDisplay.equals("") || userDate == null){
                JOptionPane.showMessageDialog(withdrawFrm,"Please input values in the required fields");
            }else{
                int money = Integer.parseInt(amountField.getText());
                Calendar newUserDate = (Calendar) userDate;
                java.sql.Date sqlDate =  new java.sql.Date(newUserDate.getTimeInMillis());

                if(money < 1000){
                    JOptionPane.showMessageDialog(withdrawFrm,"Please enter an amount higher than or equal to 1000");
                }else{
                    try {
                        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                        con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");
                        String sql = "SELECT currentBalance from CUSTOMER_INFO where customer_ID='" + userID + "'";
                        pst = con.prepareStatement(sql);
                        rs = pst.executeQuery();
                        int balance = rs.getInt("currentBalance");
                        if (balance == 0){
                            throw new SQLException();
                        } else {
                            sql = "INSERT INTO WITHDRAW_HISTORY (customer_ID, withdrawalDate, withdrawalAmount) VALUES (?,?,?)";

                            pst = con.prepareStatement(sql);
                            pst.setInt(1, userID);
                            pst.setDate(2,sqlDate);
                            pst.setInt(3,money);

                            // Asks users for confirmation before logging the details into the database
                            int a = JOptionPane.showConfirmDialog(withdrawFrm, "Confirm Transaction?");
                            if(a== JOptionPane.YES_OPTION){
                                pst.execute();
                                String tempID = String.valueOf(userID);
                                String tempDate = String.valueOf(sqlDate);
                                String tempAmount = String.valueOf(money);

                                // Stores the info that the user inputted into a string, which will be encoded in the qr code
                                String QRInfo = "Customer ID: " + tempID + "\n" + "Date of Withdrawal: " + tempDate + "\n" + "Amount to Withdraw: Php" + tempAmount;
                                boolean success = Auxiliary.convertQR(QRInfo);
                                if(success) {
                                    JOptionPane.showMessageDialog(withdrawFrm,"QR Code has been successfully created");
                                }
                            }
                            amountField.setText("");
                        }
                    } catch (SQLException | ClassNotFoundException noBalanceError) {
                        JOptionPane.showMessageDialog(withdrawFrm,"You have no remaining balance in your account.");
                        amountField.setText("");
                        amountField.requestFocus();
                    }catch (WriterException | IOException qrError){
                        JOptionPane.showMessageDialog(withdrawFrm, "Error during QR generation process.");
                    }
                }
            }
        });

        //returns users to the account screen
        returnBtn.addActionListener(e ->{
            withdrawFrm.dispose();
            prev.setVisible(true);
        });
    }

    // function for drawing the header
    // TODO
    //  transfer to auxiliary class
    public void drawElements(){
        try {
            BufferedImage myPicture = ImageIO.read(new File(path+"\\img\\header.png"));
            Image newImage = myPicture.getScaledInstance(735, 190, Image.SCALE_AREA_AVERAGING);
            JLabel header = new JLabel(new ImageIcon(newImage));
            header.setBounds(0,0,735,190);

            withdrawFrm.add(header);
        } catch (IOException error){
            JOptionPane.showMessageDialog(withdrawFrm, "Image not found");
        }
    }
}
