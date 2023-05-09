import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

/*
    Class which contain the code for drawing the UI of the login and sign-up screens
 */

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
    // Draws the login screen, which is the first screen the users see.
    public void drawLoginForm(){
        try { // loads the logo in the log-in screen
            BufferedImage myPicture = ImageIO.read(new File(path+"\\img\\logo.png"));
            Image newImage = myPicture.getScaledInstance(190, 190, Image.SCALE_SMOOTH);
            JLabel logo = new JLabel(new ImageIcon(newImage));
            logo.setBounds(140,0,190,190);

            loginFrm.add(logo);
        } catch (IOException error){
            error.printStackTrace();
        }
        loginFrm.setTitle("Welcome!");
        loginFrm.setLayout(null);
        loginFrm.setVisible(true);
        loginFrm.setSize(475,450);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - loginFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - loginFrm.getHeight()) / 2);
        // sets the location of the frame so that it's always centered
        loginFrm.setLocation(x, y);
        loginFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrm.getContentPane().setBackground(Auxiliary.mainColor);
    }

    // Draws all the elements for the login screen which include
    // the labels, buttons, and text fields
    public void drawLoginElements(){ //
        JLabel userLabel = new JLabel("Username: ");
        JLabel passLabel = new JLabel("Password: ");
        userLabel.setFont(Auxiliary.font_16);
        passLabel.setFont(Auxiliary.font_16);
        
        loginBtn.setBounds(320,325,90,25);
        loginBtn.setBackground(Auxiliary.subColor);
        loginBtn.setFont(Auxiliary.font_14);

        userLabel.setBounds(90,175,90,75);
        passLabel.setBounds(90,220,90,75);

        usernameField.setBounds(190,205,175,22);
        usernameField.setBackground(Auxiliary.subColor);
        usernameField.setFont(Auxiliary.font_15);

        passwordField.setBounds(190,250,175,22);
        passwordField.setBackground(Auxiliary.subColor);
        passwordField.setFont(Auxiliary.font_15);

        loginFrm.add(userLabel);
        loginFrm.add(passLabel);
        loginFrm.add(usernameField);
        loginFrm.add(passwordField);
        loginFrm.add(loginBtn);
        //Automatically sets focus on the usernameField when the frame loads in.
        usernameField.requestFocus();

        // When the user presses enter on the usernameField, focus will
        // be shifted onto the passwordField
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

        // When clicked, checks to see if the information the user logged is accurate and
        // logs the user into the next screen if successful
        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            // Error handling to make sure the user has inputted onto the required fields
            if(username.equals("") || password.equals("")){
                JOptionPane.showMessageDialog(loginFrm,"Please enter your information in the required fields");
            }else{
                // Checks to see if the username and password the user entered matches anything from the database
                // If true, users will be taken to the next screen, else they will be shown a message dialog
                // informing them.
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

    // Draws the label which, when clicked, will take users to the sign-up screen
    // Where they can register their account.
    public void drawSignUpLabel(){
        JLabel signUp = new JLabel("No account?");
        signUp.setBounds(90,280,120,40);
        signUp.setFont(Auxiliary.font_14);

        loginFrm.add(signUp);

        // When the label is clicked, login form is hidden and the frame
        // for the sign-up screen is drawn.
        signUp.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                loginFrm.setVisible(false);
                drawSignUpAccount();
            }

            // When hovered, the label has an underline and turns blue to indicate to users the label is clickable.
            @Override
            public void mouseEntered(MouseEvent e) {signUp.setText("<html><a href=''>No account?</a></html>");}

            // When not hovered, label returns back to normal
            @Override
            public void mouseExited(MouseEvent e) {signUp.setText("No account?");}
        });
    }

    // Draws everything on the first sign-up screen.
    // In this screen, users are asked to register a username
    // and a password.
    public void drawSignUpAccount(){
        signUpFrm.setTitle("Welcome!");
        signUpFrm.setLayout(null);
        signUpFrm.setVisible(true);
        signUpFrm.setSize(400,400);
        signUpFrm.getContentPane().setBackground(Auxiliary.mainColor);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - signUpFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - signUpFrm.getHeight()) / 2);
        signUpFrm.setLocation(x, y);

        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");

        userLabel.setBounds(70,130,90,40);
        userLabel.setFont(Auxiliary.font_16);

        passLabel.setBounds(70,170,90,40);
        passLabel.setFont(Auxiliary.font_16);

        signUpFrm.add(userLabel);
        signUpFrm.add(passLabel);

        JTextField userField = new JTextField();
        JTextField passField = new JTextField();
        userField.setBounds(180,140,150,25);
        userField.setBackground(Auxiliary.subColor);
        userField.setFont(Auxiliary.font_15);

        passField.setBounds(180,180,150,25);
        passField.setBackground(Auxiliary.subColor);
        passField.setFont(Auxiliary.font_15);

        signUpFrm.add(userField);
        signUpFrm.add(passField);

        JPanel btnPanel = new JPanel();
        btnPanel.setBounds(110,275,250,40);
        btnPanel.setBackground(Auxiliary.mainColor);

        JButton continueBtn = new JButton("Continue");
        JButton returnBtn = new JButton("Return to Login");

        continueBtn.setSize(50,27);
        continueBtn.setBackground(Auxiliary.subColor);
        continueBtn.setFont(Auxiliary.font_15);

        returnBtn.setSize(50,27);
        returnBtn.setBackground(Auxiliary.subColor);
        returnBtn.setFont(Auxiliary.font_15);

        btnPanel.add(continueBtn);
        btnPanel.add(returnBtn);
        signUpFrm.add(btnPanel);

        // When clicked, if username is unique and password meets the character limit, moves on to the next screen
        // Where users will fill out their information.
        continueBtn.addActionListener(e ->{
            String signUpUsername = userField.getText();
            String signUpPassword = passField.getText();
            //Checks to see if the fields are empty
            if (signUpUsername.equals("") || signUpPassword.equals("")){
                JOptionPane.showMessageDialog(signUpFrm,"Please input values in all the required fields or exit sign up menu");
            }else {
                try{
                    Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
                    con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");
                    // Checks to see if password meets the 6 character minimum
                    if (signUpPassword.length() < 6) {
                        JOptionPane.showMessageDialog(signUpFrm,"Password must be longer than 6 characters");
                    } else { // Prepares the information the user inputted for insertion into table
                        String sql = "INSERT INTO LOGIN (username, password) VALUES (?,?)";
                        pst = con.prepareStatement(sql);
                        pst.setString(1, signUpUsername);
                        pst.setString(2, signUpPassword);
                        // Executes the sql command, if username already exists a message dialog will appear.
                        try {
                            pst.execute();
                            drawSignUpInfoScreen();
                        } catch(SQLException sameUser){
                            JOptionPane.showMessageDialog(signUpFrm, "Username already exists.");
                            userField.requestFocus();
                        }
                    }
                }catch(SQLException | ClassNotFoundException error){
                    JOptionPane.showMessageDialog(signUpFrm, "Error when finding database.");
                }
            }
        });

        // Returns users to the log-in screen.
        returnBtn.addActionListener(e ->{
            int a = JOptionPane.showConfirmDialog(signUpFrm, "Are you sure you want to cancel signing up?");
            if(a== JOptionPane.YES_OPTION) {
                signUpFrm.dispose();
                loginFrm.setVisible(true);
            }
        });
    }

    // Draws the elements in the 2nd account screen where users will fill out
    // the necessary information to successfully register their account.
    public void drawSignUpInfoScreen(){
        signUpFrm.dispose();
        signUpAccountFrm.setTitle("Welcome!");
        signUpAccountFrm.setLayout(null);
        signUpAccountFrm.setVisible(true);
        signUpAccountFrm.setSize(600,600);
        signUpAccountFrm.getContentPane().setBackground(Auxiliary.mainColor);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - signUpAccountFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - signUpAccountFrm.getHeight()) / 2);
        // Always centers the frame
        signUpAccountFrm.setLocation(x, y);
        drawSignUpInfoLabels();
        drawSignUpInfoFields();
    }

    // Draws the labels for the frame that the function drawSignUpInfoScreen draws.
    public void drawSignUpInfoLabels(){
        JLabel nameLabel = new JLabel("Full Name: ");
        nameLabel.setBounds(50,100,100,30);
        nameLabel.setFont(Auxiliary.font_15);

        JLabel dateLabel = new JLabel("Date of Birth:");
        dateLabel.setBounds(50,150,100,30);
        dateLabel.setFont(Auxiliary.font_15);

        JLabel sexLabel = new JLabel("Sex: ");
        sexLabel.setBounds(50,200,100,30);
        sexLabel.setFont(Auxiliary.font_15);

        JLabel civilLabel = new JLabel("Civil Status: ");
        civilLabel.setBounds(50,250,100,30);
        civilLabel.setFont(Auxiliary.font_15);

        JLabel addressLabel = new JLabel("Home Address: ");
        addressLabel.setBounds(50,300,150,30);
        addressLabel.setFont(Auxiliary.font_15);

        JLabel contactLabel = new JLabel("Contact Number: ");
        contactLabel.setBounds(50,350,150,30);
        contactLabel.setFont(Auxiliary.font_15);

        JLabel emailLabel = new JLabel("E-mail Address: ");
        emailLabel.setBounds(50,400,150,30);
        emailLabel.setFont(Auxiliary.font_15);

        signUpAccountFrm.add(nameLabel);
        signUpAccountFrm.add(dateLabel);
        signUpAccountFrm.add(sexLabel);
        signUpAccountFrm.add(civilLabel);
        signUpAccountFrm.add(addressLabel);
        signUpAccountFrm.add(contactLabel);
        signUpAccountFrm.add(emailLabel);
    }

    // Draws the text fields that will be used by users to input their information on sign-up screen.
    public void drawSignUpInfoFields(){
        JTextField nameField = new JTextField(); //string
        nameField.setBounds(210,110,190,25);
        nameField.setBackground(Auxiliary.subColor);
        nameField.setFont(Auxiliary.font_15);

        // Mandatory code for implementing a JDatePicker, which is used
        // by users to pick the date of their birth
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
        sexField.setBackground(Auxiliary.subColor);
        sexField.setFont(Auxiliary.font_15);

        String[] status =  {"Select one from the list", "Single", "Married", "Widowed"};
        JComboBox<String> civilField = new JComboBox<>(status); //string
        civilField.setBounds(210,260,190,25);
        civilField.setBackground(Auxiliary.subColor);
        civilField.setFont(Auxiliary.font_15);

        JTextField addressField = new JTextField(); //string
        addressField.setBounds(210,310,190,25);
        addressField.setBackground(Auxiliary.subColor);
        addressField.setFont(Auxiliary.font_15);

        JTextField contactField = new JTextField(); //string
        contactField.setBounds(210,360,190,25);
        contactField.setBackground(Auxiliary.subColor);
        contactField.setFont(Auxiliary.font_15);

        JTextField emailField = new JTextField(); //string
        emailField.setBounds(210,410,190,25);
        emailField.setBackground(Auxiliary.subColor);
        emailField.setFont(Auxiliary.font_15);

        JButton signBtn = new JButton("Sign Up");
        signBtn.setBounds(420,500,100,25);
        signBtn.setFont(Auxiliary.font_15);
        signBtn.setBackground(Auxiliary.subColor);

        signUpAccountFrm.add(nameField);
        signUpAccountFrm.add(datePicker);
        signUpAccountFrm.add(sexField);
        signUpAccountFrm.add(civilField);
        signUpAccountFrm.add(addressField);
        signUpAccountFrm.add(contactField);
        signUpAccountFrm.add(emailField);
        signUpAccountFrm.add(signBtn);

        // When clicked, logs the information the user inputted and inserts it into the database.
        signBtn.addActionListener(e -> {
            String name = nameField.getText();
            Object userDate = datePicker.getJFormattedTextField().getValue();
            String sex = sexField.getText();
            String civil = String.valueOf(civilField.getSelectedItem());
            String address = addressField.getText();
            String contact = contactField.getText();
            String email = emailField.getText();

            // Checks to see if the user has inputted information into every field
            if (name.equals("") || userDate == null || sex.equals("") || civil.equals("") || address.equals("") || contact.equals("") || email.equals("")){
                JOptionPane.showMessageDialog(signUpAccountFrm,"Please input values in all the required fields or cancel editing");
            }else if(civil.equals("Select one from the list")){
                JOptionPane.showMessageDialog(signUpAccountFrm,"Please select an appropriate civil status");
            }else {
                try{
                    Calendar newUserDate = (Calendar) userDate;
                    // Converts the date from the JDatePicker object into the sql date format
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

                    // Prompt just to make sure the user filled in their details accurately
                    int a = JOptionPane.showConfirmDialog(signUpAccountFrm, "Are you sure you the information you logged is accurate?");
                    if(a== JOptionPane.YES_OPTION) {
                        pst.execute();
                        signUpAccountFrm.dispose();
                        loginFrm.setVisible(true);
                    }
                }catch (SQLException | ClassNotFoundException error){
                    JOptionPane.showMessageDialog(signUpAccountFrm, "Error when finding database.");
                }

            }
        });
    }
}
