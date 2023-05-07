import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

public class Deposit implements Draw{
    protected JFrame depositFrm = new JFrame();
    protected PreparedStatement pst = null;
    protected Connection con = null;
    protected File currentDir = new File("");
    protected String path = currentDir.getAbsolutePath();
    Deposit(JFrame frame,int cust_ID){
        drawForm();
        drawLabels(frame, cust_ID);
        drawElements();
        drawMenu(cust_ID);
    }

    @Override
    public void drawForm() {
        depositFrm.setTitle("Welcome!");
        depositFrm.setLayout(null);
        depositFrm.setVisible(true);
        depositFrm.setSize(600,500);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - depositFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - depositFrm.getHeight()) / 2);
        depositFrm.setLocation(x, y);
    }

    @Override
    public void drawMenu(int ID) {
        JMenu menu = new JMenu("Options");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1, item2;

        item1 = new JMenuItem("Logout");
        item1.addActionListener(e -> {
            depositFrm.dispose();
            new Login();
        });

        item2 = new JMenuItem("Exit ");
        item2.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(depositFrm, "Are you sure you want to exit?");
            if(a== JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });
        menu.add(item1);
        menu.add(item2);
        menuBar.add(menu);
        depositFrm.setJMenuBar(menuBar);
    }

    public void drawLabels(JFrame prev, int userID) {
        JLabel title = new JLabel("Deposit");
        JLabel subtitle = new JLabel("Please input the necessary details");

        title.setBounds(20,135,150,40);
        title.setFont(new Font("Sans Serif", Font.PLAIN, 20));
        subtitle.setBounds(20,155,250,40);
        subtitle.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        depositFrm.add(title);
        depositFrm.add(subtitle);

        JLabel customer_ID = new JLabel("Customer ID:");
        customer_ID.setBounds(50,215,150,30);
        customer_ID.setFont(new Font("Sans Serif", Font.PLAIN, 16));

        JLabel date = new JLabel("Planned date of deposit:");
        date.setBounds(50,265,250,30);
        date.setFont(new Font("Sans Serif", Font.PLAIN, 16));

        JLabel amount = new JLabel("Amount to deposit:");
        amount.setBounds(50,320,150,30);
        amount.setFont(new Font("Sans Serif", Font.PLAIN, 16));

        String displayID = Integer.toString(userID);
        JTextField idField = new JTextField(displayID);
        idField.setBounds(280,220,175,25);
        idField.setEditable(false);

        UtilDateModel model = new UtilDateModel();
        Properties dateProperty = new Properties();
        dateProperty.put("text.today", "Today");
        dateProperty.put("text.month", "Month");
        dateProperty.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, dateProperty);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new Auxiliary.DateLabelFormatter());
        datePicker.setBounds(280,270,175,30);

        JTextField amountField = new JTextField();
        amountField.setBounds(280,325,175,25);

        JButton generate = new JButton("Generate QR Code");
        generate.setBounds(300,380, 155,27);
        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(475,380,75,27);

        depositFrm.add(customer_ID);
        depositFrm.add(date);
        depositFrm.add(amount);
        depositFrm.add(idField);
        depositFrm.add(datePicker);
        depositFrm.add(amountField);
        depositFrm.add(generate);
        depositFrm.add(returnBtn);

        generate.addActionListener(e -> {
            String moneyDisplay = amountField.getText();
            Object userDate = datePicker.getJFormattedTextField().getValue();
            if (moneyDisplay.equals("") || userDate == null){
                JOptionPane.showMessageDialog(depositFrm,"Please input values in the required fields");
            }else{
                int money = Integer.parseInt(amountField.getText());
                Calendar newUserDate = (Calendar) userDate;
                java.sql.Date sqlDate =  new java.sql.Date(newUserDate.getTimeInMillis());

                if(money < 1000){
                    JOptionPane.showMessageDialog(depositFrm,"Please enter an amount higher than or equal to 1000");
                }else{
                    try {
                        Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                        con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");
                        String sql = "INSERT INTO DEPOSIT_HISTORY (customer_ID, depositedDate, depositedAmount) VALUES (?,?,?)";

                        pst = con.prepareStatement(sql);
                        pst.setInt(1, userID);
                        pst.setDate(2,sqlDate);
                        pst.setInt(3,money);
                        int a = JOptionPane.showConfirmDialog(depositFrm, "Confirm Transaction?");
                        if(a== JOptionPane.YES_OPTION){
                            pst.execute();
                            String tempID = String.valueOf(userID);
                            String tempDate = String.valueOf(sqlDate);
                            String tempAmount = String.valueOf(money);
                            String QRInfo = "Customer ID: " + tempID + "\n" + "Date of Deposit: " + tempDate + "\n" + "Amount to Deposit: Php" + tempAmount;
                            boolean success = Auxiliary.convertQR(QRInfo);
                            if(success) {
                                JOptionPane.showMessageDialog(depositFrm,"QR Code has been successfully created");
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
            depositFrm.dispose();
            prev.setVisible(true);
        });
    }

    public void drawElements(){
        JPanel header = new JPanel();
        header.setBounds(0,0,600,125);
        header.setBackground(new Color(196,240,173));

        depositFrm.add(header);
    }
}
