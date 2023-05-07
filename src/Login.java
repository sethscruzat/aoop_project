import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

public class Login{
    protected JFrame loginFrm = new JFrame();
    protected JFrame signUpFrm = new JFrame();
    protected JFrame signUpAccountFrm = new JFrame();
    protected JTextField usernameField = new JTextField();
    protected JTextField passwordField =  new JTextField();
    protected JButton loginBtn = new JButton("LOGIN");
    protected PreparedStatement pst = null;
    protected ResultSet rs = null;
    protected Connection con = null;
    protected File currentDir = new File("");
    protected String path = currentDir.getAbsolutePath();

    public Login(){
        drawLoginForm();
        drawLoginElements();
        drawSignUpLabel();
    }
    public void drawLoginForm(){
        loginFrm.setTitle("Welcome!");
        loginFrm.setLayout(null);
        loginFrm.setVisible(true);
        loginFrm.setSize(400,400);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - loginFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - loginFrm.getHeight()) / 2);
        loginFrm.setLocation(x, y);
        loginFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrm.getContentPane().setBackground(new Color(222,194,186));
    }

    public void drawLoginElements(){
        JLabel userLabel = new JLabel("Username: ");
        JLabel passLabel = new JLabel("Password: ");
        userLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        passLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        
        loginBtn.setBounds(250,275,90,25);
        loginBtn.setBackground(new Color(237,226,222));
        loginBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));

        userLabel.setBounds(50,120,90,75);
        passLabel.setBounds(50,165,90,75);

        usernameField.setBounds(150,150,150,22);
        usernameField.setBackground(new Color(237,226,222));
        usernameField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        passwordField.setBounds(150,195,150,22);
        passwordField.setBackground(new Color(237,226,222));
        passwordField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        loginFrm.add(userLabel);
        loginFrm.add(passLabel);
        loginFrm.add(usernameField);
        loginFrm.add(passwordField);
        loginFrm.add(loginBtn);
        usernameField.requestFocus();

        usernameField.addKeyListener(new KeyListener(){
            @Override
            public void keyPressed(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_ENTER){
                    passwordField.requestFocus();
                }
            }
            public void keyReleased(KeyEvent arg0){

            }
            public void keyTyped(KeyEvent arg0) {

            }
        });

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            if(username.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(loginFrm,"Please enter your information in the required fields");
            }else{
                try {
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");
                    String sql = "select customer_ID from LOGIN where username= '" + username + "' and password= '" + password + "'";

                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    if (rs.next()){
                        String cust_ID = rs.getString("customer_ID");
                        int customer_ID = Integer.parseInt(cust_ID);
                        new Account(customer_ID);
                        loginFrm.dispose();
                    }else{
                        JOptionPane.showMessageDialog(loginFrm, "Incorrect login details");
                    }
                }catch (Exception error){
                    error.printStackTrace();
                }
            }
        });
    }

    public void drawSignUpLabel(){
        JLabel signUp = new JLabel("No account?");
        signUp.setBounds(50,215,120,40);
        signUp.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));

        loginFrm.add(signUp);

        signUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginFrm.setVisible(false);
                drawSignUpAccount();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                signUp.setText("<html><a href=''>No account?</a></html>");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                signUp.setText("No account?");
            }
        });
    }

    public void drawSignUpAccount(){
        signUpFrm.setTitle("Welcome!");
        signUpFrm.setLayout(null);
        signUpFrm.setVisible(true);
        signUpFrm.setSize(400,400);
        signUpFrm.getContentPane().setBackground(new Color(222,194,186));

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - signUpFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - signUpFrm.getHeight()) / 2);
        signUpFrm.setLocation(x, y);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        userLabel.setBounds(70,130,90,40);
        userLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));

        passLabel.setBounds(70,170,90,40);
        passLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));

        signUpFrm.add(userLabel);
        signUpFrm.add(passLabel);

        JTextField userField = new JTextField();
        JTextField passField = new JTextField();
        userField.setBounds(180,140,150,25);
        userField.setBackground(new Color(237,226,222));
        userField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        passField.setBounds(180,180,150,25);
        passField.setBackground(new Color(237,226,222));
        passField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        signUpFrm.add(userField);
        signUpFrm.add(passField);

        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(110,275,250,40);
        btnPanel.setBackground(new Color(222,194,186));

        JButton continueBtn = new JButton("Continue");
        JButton returnBtn = new JButton("Return to Login");

        continueBtn.setSize(50,27);
        continueBtn.setBackground(new Color(237,226,222));
        continueBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        returnBtn.setSize(50,27);
        returnBtn.setBackground(new Color(237,226,222));
        returnBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        btnPanel.add(continueBtn);
        btnPanel.add(returnBtn);
        signUpFrm.add(btnPanel);

        continueBtn.addActionListener(e ->{
            String signUpUsername = userField.getText();
            String signUpPassword = passField.getText();
            if (signUpUsername.equals("") || signUpPassword.equals("")){
                JOptionPane.showMessageDialog(signUpFrm,"Please input values in all the required fields or exit sign up menu");
            }else {
                try{
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");
                    String sql = "select username from LOGIN";
                    pst = con.prepareStatement(sql);
                    rs = pst.executeQuery();
                    while(rs.next()) {
                        String compareUser = rs.getString("username");
                        if (compareUser.equals(signUpUsername)) {
                            JOptionPane.showMessageDialog(signUpFrm, "Username already exists.");
                            userField.requestFocus();
                        }
                    }
                    if (signUpPassword.length() < 6) {
                        JOptionPane.showMessageDialog(signUpFrm,"Password must be longer than 6 characters");
                    } else {
                        sql = "INSERT INTO LOGIN (username, password) VALUES (?,?)";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, signUpUsername);
                        pst.setString(2, signUpPassword);
                        pst.execute();
                        drawSignUpInfoScreen();
                    }
                }catch(SQLException | ClassNotFoundException error){
                    error.printStackTrace();
                }
            }
        });

        returnBtn.addActionListener(e ->{
            int a = JOptionPane.showConfirmDialog(signUpFrm, "Are you sure you want to cancel signing up?");
            if(a== JOptionPane.YES_OPTION) {
                signUpFrm.dispose();
                loginFrm.setVisible(true);
            }
        });
    }

    public void drawSignUpInfoScreen(){
        signUpFrm.dispose();
        signUpAccountFrm.setTitle("Welcome!");
        signUpAccountFrm.setLayout(null);
        signUpAccountFrm.setVisible(true);
        signUpAccountFrm.setSize(600,600);
        signUpAccountFrm.getContentPane().setBackground(new Color(222,194,186));

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - signUpAccountFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - signUpAccountFrm.getHeight()) / 2);
        signUpAccountFrm.setLocation(x, y);
        drawSignUpInfoLabels();
        drawSignUpInfoFields();
    }

    public void drawSignUpInfoLabels(){
        JLabel nameLabel = new JLabel("Full Name: ");
        nameLabel.setBounds(50,100,100,30);
        nameLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JLabel dateLabel = new JLabel("Date of Birth:");
        dateLabel.setBounds(50,150,100,30);
        dateLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JLabel sexLabel = new JLabel("Sex: ");
        sexLabel.setBounds(50,200,100,30);
        sexLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JLabel civilLabel = new JLabel("Civil Status: ");
        civilLabel.setBounds(50,250,100,30);
        civilLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JLabel addressLabel = new JLabel("Home Address: ");
        addressLabel.setBounds(50,300,150,30);
        addressLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JLabel contactLabel = new JLabel("Contact Number: ");
        contactLabel.setBounds(50,350,150,30);
        contactLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JLabel emailLabel = new JLabel("E-mail Address: ");
        emailLabel.setBounds(50,400,150,30);
        emailLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        signUpAccountFrm.add(nameLabel);
        signUpAccountFrm.add(dateLabel);
        signUpAccountFrm.add(sexLabel);
        signUpAccountFrm.add(civilLabel);
        signUpAccountFrm.add(addressLabel);
        signUpAccountFrm.add(contactLabel);
        signUpAccountFrm.add(emailLabel);
    }

    public void drawSignUpInfoFields(){
        JTextField nameField = new JTextField(); //string
        nameField.setBounds(210,110,190,25);
        nameField.setBackground(new Color(237,226,222));
        nameField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        UtilDateModel model = new UtilDateModel();
        Properties dateProperty = new Properties();
        dateProperty.put("text.today", "Today");
        dateProperty.put("text.month", "Month");
        dateProperty.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(model, dateProperty);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, new Auxiliary.DateLabelFormatter());
        datePicker.setBounds(210,160,190,30);

        JTextField sexField = new JTextField(); //string
        sexField.setBounds(210,210,190,25);
        sexField.setBackground(new Color(237,226,222));
        sexField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        String[] status =  {"Select one from the list", "Single", "Married", "Widowed"};
        JComboBox<String> civilField = new JComboBox<>(status); //string
        civilField.setBounds(210,260,190,25);
        civilField.setBackground(new Color(237,226,222));
        civilField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JTextField addressField = new JTextField(); //string
        addressField.setBounds(210,310,190,25);
        addressField.setBackground(new Color(237,226,222));
        addressField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JTextField contactField = new JTextField(); //string
        contactField.setBounds(210,360,190,25);
        contactField.setBackground(new Color(237,226,222));
        contactField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JTextField emailField = new JTextField(); //string
        emailField.setBounds(210,410,190,25);
        emailField.setBackground(new Color(237,226,222));
        emailField.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));

        JButton signBtn = new JButton("Sign Up");
        signBtn.setBounds(420,500,100,25);
        signBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 15));
        signBtn.setBackground(new Color(237,226,222));

        signUpAccountFrm.add(nameField);
        signUpAccountFrm.add(datePicker);
        signUpAccountFrm.add(sexField);
        signUpAccountFrm.add(civilField);
        signUpAccountFrm.add(addressField);
        signUpAccountFrm.add(contactField);
        signUpAccountFrm.add(emailField);
        signUpAccountFrm.add(signBtn);

        signBtn.addActionListener(e -> {
            String name = nameField.getText();
            Object userDate = datePicker.getJFormattedTextField().getValue();
            String sex = sexField.getText();
            String civil = String.valueOf(civilField.getSelectedItem());
            String address = addressField.getText();
            String contact = contactField.getText();
            String email = emailField.getText();

            if (name.equals("") || userDate == null || sex.equals("") || civil.equals("") || address.equals("") || contact.equals("") || email.equals("")){
                JOptionPane.showMessageDialog(signUpAccountFrm,"Please input values in all the required fields or cancel editing");
            }else {
                try{
                    Calendar newUserDate = (Calendar) userDate;
                    java.sql.Date sqlDate =  new java.sql.Date(newUserDate.getTimeInMillis());
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");

                    String sql = "INSERT INTO CUSTOMER_INFO (fullName, birthDate, sex, civilStatus, address, contactNumber, emailAddress) VALUES (?,?,?,?,?,?,?)";
                    pst = con.prepareStatement(sql);
                    pst.setString(1, name);
                    pst.setDate(2, sqlDate);
                    pst.setString(3, sex);
                    pst.setString(4, civil);
                    pst.setString(5, address);
                    pst.setString(6, contact);
                    pst.setString(7, email);

                    int a = JOptionPane.showConfirmDialog(signUpAccountFrm, "Are you sure you the information you logged is accurate?");
                    if(a== JOptionPane.YES_OPTION) {
                        pst.execute();
                        signUpAccountFrm.dispose();
                        loginFrm.setVisible(true);
                    }
                }catch (SQLException | ClassNotFoundException error){
                    error.printStackTrace();
                }

            }
        });
    }
}
