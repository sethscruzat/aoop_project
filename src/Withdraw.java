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

public class Withdraw implements Draw{
    protected JFrame withdrawFrm = new JFrame();
    protected PreparedStatement pst = null;
    protected Connection con = null;
    protected File currentDir = new File("");
    protected String path = currentDir.getAbsolutePath();
    Withdraw(JFrame frame, int cust_ID){
        drawForm();
        drawLabels(frame, cust_ID);
        drawElements();
        drawMenu(cust_ID);
    }

    @Override
    public void drawForm() {
        withdrawFrm.setTitle("Welcome!");
        withdrawFrm.setLayout(null);
        withdrawFrm.setVisible(true);
        withdrawFrm.setSize(750,650);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - withdrawFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - withdrawFrm.getHeight()) / 2);
        withdrawFrm.setLocation(x, y);
        withdrawFrm.getContentPane().setBackground(new Color(222,194,186));
    }

    @Override
    public void drawMenu(int ID) {
        JMenu menu = new JMenu("Options");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1, item2;

        item1 = new JMenuItem("Logout");
        item1.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(withdrawFrm, "Are you sure you want to logout?");
            if (a == JOptionPane.YES_OPTION){
                withdrawFrm.dispose();
                new Login();
            }
        });

        item2 = new JMenuItem("Exit ");
        item2.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(withdrawFrm, "Are you sure you want to exit?");
            if(a== JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });
        menuBar.setBackground(new Color(251,250,249));
        menu.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        item1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        item2.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));

        menu.add(item1);
        menu.add(item2);
        menuBar.add(menu);
        withdrawFrm.setJMenuBar(menuBar);
    }

    public void drawLabels(JFrame prev, int userID) {
        JLabel title = new JLabel("Withdraw");
        JLabel subtitle = new JLabel("Please input the necessary details");

        title.setBounds(20,225,200,60);
        title.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));
        subtitle.setBounds(20,255,300,60);
        subtitle.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
        withdrawFrm.add(title);
        withdrawFrm.add(subtitle);

        JLabel customer_ID = new JLabel("Customer ID:");
        customer_ID.setBounds(50,325,150,30);
        customer_ID.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));

        JLabel date = new JLabel("Planned date of withdrawal:");
        date.setBounds(50,375,250,30);
        date.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));

        JLabel amount = new JLabel("Amount to withdraw:");
        amount.setBounds(50,430,170,30);
        amount.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));

        String displayID = Integer.toString(userID);
        JTextField idField = new JTextField(displayID);
        idField.setBounds(280,330,175,25);
        idField.setBackground(new Color(237,226,222));
        idField.setFont(new Font("Trebuchet MS", Font.PLAIN, 17));
        idField.setEditable(false);

        UtilDateModel model = new UtilDateModel();
        Properties dateProperty = new Properties();
        dateProperty.put("text.today", "Today");
        dateProperty.put("text.month", "Month");
        dateProperty.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, dateProperty);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new Auxiliary.DateLabelFormatter());
        datePicker.setBounds(280,380,175,30);

        JTextField amountField = new JTextField();
        amountField.setBounds(280,440,175,25);
        amountField.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        amountField.setBackground(new Color(237,226,222));

        JButton generate = new JButton("Generate QR Code");
        generate.setBounds(400,510, 175,27);
        generate.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        generate.setBackground(new Color(237,226,222));

        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(585,510,100,27);
        returnBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        returnBtn.setBackground(new Color(237,226,222));

        withdrawFrm.add(customer_ID);
        withdrawFrm.add(date);
        withdrawFrm.add(amount);
        withdrawFrm.add(idField);
        withdrawFrm.add(datePicker);
        withdrawFrm.add(amountField);
        amountField.requestFocus();
        withdrawFrm.add(generate);
        withdrawFrm.add(returnBtn);

        generate.addActionListener(e -> {
            String moneyDisplay = amountField.getText();
            Object userDate = datePicker.getJFormattedTextField().getValue();
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
                        String sql = "INSERT INTO WITHDRAW_HISTORY (customer_ID, withdrawalDate, withdrawalAmount) VALUES (?,?,?)";

                        pst = con.prepareStatement(sql);
                        pst.setInt(1, userID);
                        pst.setDate(2,sqlDate);
                        pst.setInt(3,money);
                        int a = JOptionPane.showConfirmDialog(withdrawFrm, "Confirm Transaction?");
                        if(a== JOptionPane.YES_OPTION){
                            pst.execute();
                            String tempID = String.valueOf(userID);
                            String tempDate = String.valueOf(sqlDate);
                            String tempAmount = String.valueOf(money);
                            String QRInfo = "Customer ID: " + tempID + "\n" + "Date of Withdrawal: " + tempDate + "\n" + "Amount to Withdraw: Php" + tempAmount;
                            boolean success = Auxiliary.convertQR(QRInfo);
                            if(success) {
                                JOptionPane.showMessageDialog(withdrawFrm,"QR Code has been successfully created");
                            }
                        }
                        amountField.setText("");

                    }catch (Exception error){
                        error.printStackTrace();
                    }
                }
            }
        });

        returnBtn.addActionListener(e ->{
            withdrawFrm.dispose();
            prev.setVisible(true);
        });
    }

    public void drawElements(){
        try {
            BufferedImage myPicture = ImageIO.read(new File(path+"\\img\\header.png"));
            Image newImage = myPicture.getScaledInstance(750, 225, Image.SCALE_AREA_AVERAGING);
            JLabel header = new JLabel(new ImageIcon(newImage));
            header.setBounds(0,0,750,225);

            withdrawFrm.add(header);
        } catch (IOException error){
            error.printStackTrace();
        }
    }
}
