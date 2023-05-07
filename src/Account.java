import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;


public class Account implements Draw{
    protected JFrame accountFrm = new JFrame();
    protected PreparedStatement pst = null;
    protected ResultSet rs = null;
    protected Connection con = null;
    protected JLabel nameLabel = new JLabel();
    protected JLabel dateDisplay = new JLabel();
    protected JLabel sexDisplay = new JLabel();
    protected JLabel civilDisplay = new JLabel();
    protected JLabel addressDisplay = new JLabel();
    protected JLabel contactDisplay = new JLabel();
    protected JLabel emailDisplay = new JLabel();
    protected JLabel balanceDisplay = new JLabel();
    protected JPanel btnPanel = new JPanel();
    protected File currentDir = new File("");
    protected String path = currentDir.getAbsolutePath();
    Account(int customer_ID){
        drawForm();
        drawMenu(customer_ID);
        paint();
        drawLabels();
        drawDisplay(customer_ID);
        drawButtons(customer_ID);
    }
    @Override
    public void drawForm(){
        accountFrm.setTitle("Welcome!");
        accountFrm.setLayout(null);
        accountFrm.setVisible(true);
        accountFrm.setSize(800,700);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - accountFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - accountFrm.getHeight()) / 2);
        accountFrm.setLocation(x, y);
        accountFrm.getContentPane().setBackground(new Color(222,194,186));
    }

    @Override
    public void drawMenu(int userID){
        JMenu menu = new JMenu("Options");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1, item2;

        item1 = new JMenuItem("Edit Account");
        item2 = new JMenuItem("Logout");

        item1.addActionListener(e -> {
            removeDisplay();
            replaceDisplay(userID);
        });

        item2.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(accountFrm, "Are you sure you want to logout?");
            if (a == JOptionPane.YES_OPTION){
                accountFrm.dispose();
                new Login();
            }
        });
        menu.add(item1);
        menu.add(item2);
        menuBar.add(menu);
        menuBar.setBackground(new Color(251,250,249));
        menu.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        item1.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        item2.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
        accountFrm.setJMenuBar(menuBar);
    }

    public void paint(){
        JPanel temp = new JPanel();
        temp.setBackground(new Color(196,240,173));
        temp.setBounds(50,50,150,150);
        accountFrm.add(temp);
    }

    public void drawLabels(){
        JLabel dateLabel = new JLabel("Date of Birth: ");
        JLabel sexLabel = new JLabel("Sex: ");
        JLabel civilLabel = new JLabel("Civil Status: ");
        JLabel addressLabel = new JLabel("Home Address: ");
        JLabel contactLabel = new JLabel("Contact Number: ");
        JLabel emailLabel = new JLabel("E-mail Address: ");
        JLabel balanceLabel = new JLabel("Balance: ");


        dateLabel.setBounds(50,250, 150,40);
        dateLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        sexLabel.setBounds(50,290, 150,40);
        sexLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        civilLabel.setBounds(50,330, 150,40);
        civilLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        addressLabel.setBounds(50,370, 150,40);
        addressLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        contactLabel.setBounds(50,410, 150,40);
        contactLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        emailLabel.setBounds(50,450, 150,40);
        emailLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        balanceLabel.setBounds(500,245, 150,40);
        balanceLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        accountFrm.add(dateLabel);
        accountFrm.add(sexLabel);
        accountFrm.add(civilLabel);
        accountFrm.add(addressLabel);
        accountFrm.add(contactLabel);
        accountFrm.add(emailLabel);
        accountFrm.add(balanceLabel);
    }
    public void drawDisplay(int ID){
        String fullName = "";
        String birthDate = "";
        String sex = "";
        String civilStatus = "";
        String address = "";
        String contactNumber = "";
        String emailAddress = "";
        String currentBalance = "";
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");

            String sql = "select * from CUSTOMER_INFO where customer_ID='" + ID + "'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                fullName = rs.getString("fullName");
                birthDate = rs.getString("birthDate");
                sex = rs.getString("sex");
                civilStatus = rs.getString("civilStatus");
                address = rs.getString("address");
                contactNumber = rs.getString("contactNumber");
                emailAddress = rs.getString("emailAddress");
                currentBalance = rs.getString("currentBalance");
            }
        }catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        String newDate = birthDate.substring(0,10);
        nameLabel.setText(fullName);
        dateDisplay.setText(newDate);
        sexDisplay.setText(sex);
        civilDisplay.setText(civilStatus);
        addressDisplay.setText(address);
        contactDisplay.setText(contactNumber);
        emailDisplay.setText(emailAddress);
        balanceDisplay.setText("Php " + currentBalance);

        nameLabel.setBounds(225,115,300,40);
        nameLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 22));

        dateDisplay.setBounds(200,250, 250,40);
        dateDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        sexDisplay.setBounds(200,290, 150,40);
        sexDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        civilDisplay.setBounds(200,330, 200,40);
        civilDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        addressDisplay.setBounds(200,370, 250,40);
        addressDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        contactDisplay.setBounds(200,410, 250,40);
        contactDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        emailDisplay.setBounds(200,450, 250,40);
        emailDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        balanceDisplay.setBounds(525,300, 250,40);
        balanceDisplay.setFont(new Font("Trebuchet MS", Font.PLAIN, 18));

        accountFrm.add(nameLabel);
        accountFrm.add(dateDisplay);
        accountFrm.add(sexDisplay);
        accountFrm.add(civilDisplay);
        accountFrm.add(addressDisplay);
        accountFrm.add(contactDisplay);
        accountFrm.add(emailDisplay);
        accountFrm.add(balanceDisplay);
    }

    public void removeDisplay(){
        dateDisplay.setVisible(false);
        sexDisplay.setVisible(false);
        civilDisplay.setVisible(false);
        addressDisplay.setVisible(false);
        contactDisplay.setVisible(false);
        emailDisplay.setVisible(false);
        btnPanel.setVisible(false);
    }

    public void recoverDisplay(){
        dateDisplay.setVisible(true);
        sexDisplay.setVisible(true);
        civilDisplay.setVisible(true);
        addressDisplay.setVisible(true);
        contactDisplay.setVisible(true);
        emailDisplay.setVisible(true);
        btnPanel.setVisible(true);
    }

    public void replaceDisplay(int userID){
        String[] status =  {"Select one from the list", "Single", "Married", "Widowed"};
        UtilDateModel model = new UtilDateModel();
        Properties dateProperty = new Properties();
        dateProperty.put("text.today", "Today");
        dateProperty.put("text.month", "Month");
        dateProperty.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, dateProperty);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new Auxiliary.DateLabelFormatter());
        datePicker.setBounds(230,260,175,30);

        JTextField sexField = new JTextField(); //string
        sexField.setBounds(230,300,225,25);
        sexField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        sexField.setBackground(new Color(237,226,222));

        JComboBox<String> civilField = new JComboBox<>(status);
        civilField.setBounds(230,340,225,25);
        civilField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        civilField.setBackground(new Color(237,226,222));

        JTextField addressField = new JTextField();
        addressField.setBounds(230,380,225,25);
        addressField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        addressField.setBackground(new Color(237,226,222));

        JTextField contactField = new JTextField();
        contactField.setBounds(230,420,225,25);
        contactField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        contactField.setBackground(new Color(237,226,222));

        JTextField emailField = new JTextField();
        emailField.setBounds(230,460,225,25);
        emailField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        emailField.setBackground(new Color(237,226,222));

        JPanel editPanel = new JPanel();
        editPanel.setBounds(350,550,400,75);
        editPanel.setBackground(new Color(222,194,186));

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setSize(100,25);
        saveBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        saveBtn.setBackground(new Color(237,226,222));

        JButton cancel = new JButton("Cancel Editing");
        cancel.setSize(100,25);
        cancel.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        cancel.setBackground(new Color(237,226,222));

        editPanel.add(saveBtn);
        editPanel.add(cancel);

        saveBtn.addActionListener(e -> {
            Object userDate = datePicker.getJFormattedTextField().getValue();
            String sex = sexField.getText();
            String civil = String.valueOf(civilField.getSelectedItem());
            String address = addressField.getText();
            String contact = contactField.getText();
            String email = emailField.getText();
            if (userDate == null || sex.equals("") || civil.equals("") || address.equals("") || contact.equals("") ||  email.equals("")){
                JOptionPane.showMessageDialog(accountFrm,"Please input values in all the required fields or cancel editing");
            }else {
                try {
                    Calendar newUserDate = (Calendar) userDate;
                    java.sql.Date sqlDate =  new java.sql.Date(newUserDate.getTimeInMillis());
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");
                    String sql = "UPDATE CUSTOMER_INFO SET birthDate='" + sqlDate +"', sex='" + sex + "', civilStatus='" + civil + "', address='"+ address + "', contactNumber='" + contact+ "', emailAddress='" + email +"' WHERE customer_ID='" + userID +"'";

                    pst = con.prepareStatement(sql);

                    int a = JOptionPane.showConfirmDialog(accountFrm, "Are you sure you want to log your changes?");
                    if(a== JOptionPane.YES_OPTION) {
                        pst.execute();
                        sexField.setText("");
                        civilField.setSelectedIndex(0);
                        addressField.setText("");
                        contactField.setText("");
                        emailField.setText("");
                        datePicker.setToolTipText("");
                        accountFrm.dispose();
                        new Login();
                    }
                }catch (SQLException | ClassNotFoundException error) {
                    error.printStackTrace();
                }
            }
        });

        cancel.addActionListener(e ->{
            accountFrm.remove(editPanel);
            accountFrm.remove(datePicker);
            accountFrm.remove(sexField);
            accountFrm.remove(civilField);
            accountFrm.remove(addressField);
            accountFrm.remove(contactField);
            accountFrm.remove(emailField);

            recoverDisplay();

        });

        accountFrm.add(editPanel);
        accountFrm.add(datePicker);
        accountFrm.add(sexField);
        accountFrm.add(civilField);
        accountFrm.add(addressField);
        accountFrm.add(contactField);
        accountFrm.add(emailField);
    }

    public void drawButtons(int ID){
        JButton historyBtn = new JButton("Check History");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton depositBtn = new JButton("Deposit");
        JButton exitBtn = new JButton("Exit");


        btnPanel.setBounds(350,550,400,75);
        btnPanel.setBackground(new Color(222,194,186));

        historyBtn.setSize(50,25);
        historyBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        historyBtn.setBackground(new Color(237,226,222));

        withdrawBtn.setSize(50,25);
        withdrawBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        withdrawBtn.setBackground(new Color(237,226,222));

        depositBtn.setSize(50,25);
        depositBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        depositBtn.setBackground(new Color(237,226,222));

        exitBtn.setSize(50,25);
        exitBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        exitBtn.setBackground(new Color(237,226,222));

        btnPanel.add(historyBtn);
        btnPanel.add(withdrawBtn);
        btnPanel.add(depositBtn);
        btnPanel.add(exitBtn);

        accountFrm.add(btnPanel);

        historyBtn.addActionListener(e ->{
            new History(accountFrm, ID);
            accountFrm.setVisible(false);
        });

        withdrawBtn.addActionListener(e ->{
            new Withdraw(accountFrm, ID);
            accountFrm.setVisible(false);
        });

        depositBtn.addActionListener(e ->{
            new Deposit(accountFrm, ID);
            accountFrm.setVisible(false);
        });

        exitBtn.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(accountFrm, "Are you sure you want to exit?");
            if(a== JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });
    }
}
