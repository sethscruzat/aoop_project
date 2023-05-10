import net.ucanaccess.complex.Attachment;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

/*
    Class which handles the account screen wherein users are able to
    check their information and also edit their account if they
    want to.
 */

public class Account implements Draw{
    protected JFrame accountFrm = new JFrame();
    protected Font font_18 = new Font("Trebuchet MS", Font.PLAIN, 18);
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
    protected JLabel iconDisplay = new JLabel();
    protected File currentDir = new File("");
    protected String path = currentDir.getAbsolutePath();
    Account(int customer_ID){
        drawForm();
        drawMenu(customer_ID);
        drawLabels();
        drawDisplay(customer_ID);
        drawButtons(customer_ID);
        drawProfilePicture(customer_ID);
    }
    @Override
    public void drawForm(){ // Draws the user account screen
        accountFrm.setTitle("Welcome!");
        accountFrm.setLayout(null);
        accountFrm.setVisible(true);
        accountFrm.setSize(800,700);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - accountFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - accountFrm.getHeight()) / 2);
        // Sets the location of the frame so that it's always centered
        accountFrm.setLocation(x, y);
        accountFrm.getContentPane().setBackground(Auxiliary.mainColor);
    }

    @Override
    public void drawMenu(int userID){ // Draws the menu bar found on the upper part of the frame
        JMenu menu = new JMenu("Options");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1, item2;

        item1 = new JMenuItem("Edit Account");
        item2 = new JMenuItem("Logout");

        // When clicked, removes the labels that display the user's information
        // and replaces them with text fields which will allow the user to edit their account
        item1.addActionListener(e -> {
            removeDisplay();
            replaceDisplay(userID);
        });

        // When clicked, prompts users for logout and takes them to the login screen
        // upon confirmation
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
        menuBar.setBackground(Auxiliary.highlightColor);
        menu.setFont(Auxiliary.font_14);
        item1.setFont(Auxiliary.font_14);
        item2.setFont(Auxiliary.font_14);
        accountFrm.setJMenuBar(menuBar);
    }

    // Draws the labels for the account screen.
    public void drawLabels(){
        JLabel dateLabel = new JLabel("Date of Birth: ");
        JLabel sexLabel = new JLabel("Sex: ");
        JLabel civilLabel = new JLabel("Civil Status: ");
        JLabel addressLabel = new JLabel("Home Address: ");
        JLabel contactLabel = new JLabel("Contact Number: ");
        JLabel emailLabel = new JLabel("E-mail Address: ");
        JLabel balanceLabel = new JLabel("Balance: ");

        dateLabel.setBounds(50,250, 150,40);
        dateLabel.setFont(font_18);

        sexLabel.setBounds(50,290, 150,40);
        sexLabel.setFont(font_18);

        civilLabel.setBounds(50,330, 150,40);
        civilLabel.setFont(font_18);

        addressLabel.setBounds(50,370, 150,40);
        addressLabel.setFont(font_18);

        contactLabel.setBounds(50,410, 150,40);
        contactLabel.setFont(font_18);

        emailLabel.setBounds(50,450, 150,40);
        emailLabel.setFont(font_18);

        balanceLabel.setBounds(500,245, 150,40);
        balanceLabel.setFont(font_18);

        accountFrm.add(dateLabel);
        accountFrm.add(sexLabel);
        accountFrm.add(civilLabel);
        accountFrm.add(addressLabel);
        accountFrm.add(contactLabel);
        accountFrm.add(emailLabel);
        accountFrm.add(balanceLabel);
    }

    // Draws the labels that will display the user's information
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

            // SQL Command for extracting the necessary fields from the database based on the user's customer id
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
            JOptionPane.showMessageDialog(accountFrm, "Error when finding database.");
        }
        // Cuts the date to only include year/month/day
        String newDate = birthDate.substring(0,10);
        // Sets the values from the table as the text for the display labels
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
        dateDisplay.setFont(font_18);

        sexDisplay.setBounds(200,290, 150,40);
        sexDisplay.setFont(font_18);

        civilDisplay.setBounds(200,330, 200,40);
        civilDisplay.setFont(font_18);

        addressDisplay.setBounds(200,370, 250,40);
        addressDisplay.setFont(font_18);

        contactDisplay.setBounds(200,410, 250,40);
        contactDisplay.setFont(font_18);

        emailDisplay.setBounds(200,450, 250,40);
        emailDisplay.setFont(font_18);

        balanceDisplay.setBounds(525,300, 250,40);
        balanceDisplay.setFont(font_18);

        accountFrm.add(nameLabel);
        accountFrm.add(dateDisplay);
        accountFrm.add(sexDisplay);
        accountFrm.add(civilDisplay);
        accountFrm.add(addressDisplay);
        accountFrm.add(contactDisplay);
        accountFrm.add(emailDisplay);
        accountFrm.add(balanceDisplay);
    }

    // Function that draws the user's profile picture from the database.
    public void drawProfilePicture(int ID){
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");

            // SQL Command for extracting the necessary fields from the database based on the user's customer id
            String sql = "select profilePicture from CUSTOMER_INFO where customer_ID='" + ID + "'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();

            while(rs.next()){
                iconDisplay.setIcon(new ImageIcon(ImageIO.read(new ByteArrayInputStream(((Attachment[])rs.getObject("profilePicture"))[0].getData())).getScaledInstance(150,150, Image.SCALE_AREA_AVERAGING)));
            }
            iconDisplay.setBounds(50,50,150,150);
            accountFrm.add(iconDisplay);

        }catch (SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(accountFrm, "Error when finding database.");
        } catch(IOException | ArrayIndexOutOfBoundsException noImage) {
            JPanel temp = new JPanel();
            temp.setBackground(Auxiliary.highlightColor);
            temp.setBounds(50,50,150,150);
            accountFrm.add(temp);
            JOptionPane.showMessageDialog(accountFrm, "User has no profile picture in database");
        }
    }

    // Function used when user clicks on "Edit Account?" on the menu
    // Hides the display labels.
    public void removeDisplay(){
        dateDisplay.setVisible(false);
        sexDisplay.setVisible(false);
        civilDisplay.setVisible(false);
        addressDisplay.setVisible(false);
        contactDisplay.setVisible(false);
        emailDisplay.setVisible(false);
        btnPanel.setVisible(false);
    }

    // Used if the user decides to cancel editing
    // Unhides the display labels.
    public void recoverDisplay(){
        dateDisplay.setVisible(true);
        sexDisplay.setVisible(true);
        civilDisplay.setVisible(true);
        addressDisplay.setVisible(true);
        contactDisplay.setVisible(true);
        emailDisplay.setVisible(true);
        btnPanel.setVisible(true);
    }

    // Replaces the display labels with text fields which will enable users
    // to input new information
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
        sexField.setFont(Auxiliary.font_15);
        sexField.setBackground(Auxiliary.subColor);

        JComboBox<String> civilField = new JComboBox<>(status);
        civilField.setBounds(230,340,225,25);
        civilField.setFont(Auxiliary.font_15);
        civilField.setBackground(Auxiliary.subColor);

        JTextField addressField = new JTextField();
        addressField.setBounds(230,380,225,25);
        addressField.setFont(Auxiliary.font_15);
        addressField.setBackground(Auxiliary.subColor);

        JTextField contactField = new JTextField();
        contactField.setBounds(230,420,225,25);
        contactField.setFont(Auxiliary.font_15);
        contactField.setBackground(Auxiliary.subColor);

        JTextField emailField = new JTextField();
        emailField.setBounds(230,460,225,25);
        emailField.setFont(Auxiliary.font_15);
        emailField.setBackground(Auxiliary.subColor);

        JPanel editPanel = new JPanel();
        editPanel.setBounds(350,550,400,75);
        editPanel.setBackground(Auxiliary.mainColor);

        JButton saveBtn = new JButton("Save Changes");
        saveBtn.setSize(100,25);
        saveBtn.setFont(Auxiliary.font_15);
        saveBtn.setBackground(Auxiliary.subColor);

        JButton cancel = new JButton("Cancel Editing");
        cancel.setSize(100,25);
        cancel.setFont(Auxiliary.font_15);
        cancel.setBackground(Auxiliary.subColor);

        editPanel.add(saveBtn);
        editPanel.add(cancel);

        // When clicked, logs the new values the user entered and updates the old values in the database.
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

                    // Prompt which asks users to check the information they logged before continuing
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
                    JOptionPane.showMessageDialog(accountFrm, "Error when finding database.");
                }
            }
        });

        // When clicked, removes the fields and un-hides the display labels.
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

    // Draws the buttons on the main account screen
    public void drawButtons(int ID){
        JButton historyBtn = new JButton("Check History");
        JButton withdrawBtn = new JButton("Withdraw");
        JButton depositBtn = new JButton("Deposit");
        JButton exitBtn = new JButton("Exit");

        btnPanel.setBounds(350,550,400,75);
        btnPanel.setBackground(Auxiliary.mainColor);

        historyBtn.setSize(50,25);
        historyBtn.setFont(Auxiliary.font_15);
        historyBtn.setBackground(Auxiliary.subColor);

        withdrawBtn.setSize(50,25);
        withdrawBtn.setFont(Auxiliary.font_15);
        withdrawBtn.setBackground(Auxiliary.subColor);

        depositBtn.setSize(50,25);
        depositBtn.setFont(Auxiliary.font_15);
        depositBtn.setBackground(Auxiliary.subColor);

        exitBtn.setSize(50,25);
        exitBtn.setFont(Auxiliary.font_15);
        exitBtn.setBackground(Auxiliary.subColor);

        btnPanel.add(historyBtn);
        btnPanel.add(withdrawBtn);
        btnPanel.add(depositBtn);
        btnPanel.add(exitBtn);

        accountFrm.add(btnPanel);

        // When clicked, takes users to the history screen where they can see their withdrawal and deposit history
        historyBtn.addActionListener(e ->{
            new History(accountFrm, ID);
            accountFrm.setVisible(false);
        });

        // When clicked, takes users to the withdrawal screen where they schedule a day when they can withdraw money
        withdrawBtn.addActionListener(e ->{
            new Withdraw(accountFrm, ID);
            accountFrm.setVisible(false);
        });

        // When clicked, takes users to the withdrawal screen where they schedule a day when they can deposit money
        depositBtn.addActionListener(e ->{
            new Deposit(accountFrm, ID);
            accountFrm.setVisible(false);
        });

        // When clicked, prompts users with a confirmation dialog and if yes was clicked, exits the program.
        exitBtn.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(accountFrm, "Are you sure you want to exit?");
            if(a== JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });
    }
}
