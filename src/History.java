import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
    Class which will let users check their history
    of withdrawals and deposits if any through mainly
    the use of tables.
 */
public class History implements Draw{
    protected JFrame historyFrm = new JFrame();
    protected File currentDir = new File("");
    protected String path = currentDir.getAbsolutePath();

    History(JFrame account, int customer_ID){
        drawForm();
        drawMenu(customer_ID);
        drawLabels();
        drawButtons(account);
        drawTables(customer_ID);
    }
    @Override
    public void drawForm(){ // draws the main screen
        historyFrm.setTitle("Welcome!");
        historyFrm.setLayout(null);
        historyFrm.setVisible(true);
        historyFrm.setSize(800,700);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - historyFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - historyFrm.getHeight()) / 2);
        // sets the location of the frame so that it's always centered
        historyFrm.setLocation(x, y);
        historyFrm.getContentPane().setBackground(Auxiliary.mainColor);
    }

    @Override
    public void drawMenu(int ID){ // draws the menu bar on the upper portion of the frame
        JMenu menu = new JMenu("Options");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1, item2;

        // When clicked, users are taken to the login screen
        item1 = new JMenuItem("Logout");
        item1.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(historyFrm, "Are you sure you want to logout?");
            if (a == JOptionPane.YES_OPTION){
                historyFrm.dispose();
                new Login();
            }
        });

        // When clicked, prompts users with a confirmation dialog for exiting
        item2 = new JMenuItem("Exit");
        item2.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(historyFrm, "Are you sure you want to exit?");
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
        historyFrm.setJMenuBar(menuBar);
    }

    // Draws the labels in the frame
    public void drawLabels(){
        JLabel withdrawLabel = new JLabel("Withdrawals");
        JLabel depositLabel = new JLabel("Deposits");

        withdrawLabel.setBounds(135,230,150,40);
        withdrawLabel.setFont(Auxiliary.font_20);

        depositLabel.setBounds(560,230,150,40);
        depositLabel.setFont(Auxiliary.font_20);

        historyFrm.add(withdrawLabel);
        historyFrm.add(depositLabel);

        // Draws the header of the screen
        try {
            BufferedImage myPicture = ImageIO.read(new File(path+"\\img\\header.png"));
            Image newImage = myPicture.getScaledInstance(790, 225, Image.SCALE_AREA_AVERAGING);
            JLabel header = new JLabel(new ImageIcon(newImage));
            header.setBounds(0,0,790,225);

            historyFrm.add(header);
        } catch (IOException error){
            error.printStackTrace();
        }
    }

    // Draws the withdrawal and deposit tables which
    // serve as display for the user to check their
    // history of withdraws and deposits.
    public void drawTables(int ID) {
        JScrollPane withdrawPane = new JScrollPane(Auxiliary.getWithdrawValues(ID));
        JScrollPane depositPane = new JScrollPane(Auxiliary.getDepositValues(ID));

        withdrawPane.setBounds(40,275,325,275);
        depositPane.setBounds(430,275,325,275);

        historyFrm.add(withdrawPane);
        historyFrm.add(depositPane);
    }


    public void drawButtons(JFrame account) {
        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(660,575,100,27);
        returnBtn.setFont(Auxiliary.font_16);
        returnBtn.setBackground(Auxiliary.subColor);

        historyFrm.add(returnBtn);

        // When clicked, returns users to the account screen.
        returnBtn.addActionListener(e -> {
            historyFrm.dispose();
            account.setVisible(true);
        });
    }
}
