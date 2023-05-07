import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


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
    public void drawForm(){
        historyFrm.setTitle("Welcome!");
        historyFrm.setLayout(null);
        historyFrm.setVisible(true);
        historyFrm.setSize(800,700);

        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - historyFrm.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - historyFrm.getHeight()) / 2);
        historyFrm.setLocation(x, y);
        historyFrm.getContentPane().setBackground(new Color(222,194,186));
    }

    @Override
    public void drawMenu(int ID){
        JMenu menu = new JMenu("Options");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1, item2;

        item1 = new JMenuItem("Logout");
        item1.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(historyFrm, "Are you sure you want to logout?");
            if (a == JOptionPane.YES_OPTION){
                historyFrm.dispose();
                new Login();
            }
        });

        item2 = new JMenuItem("Exit");
        item2.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(historyFrm, "Are you sure you want to exit?");
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
        historyFrm.setJMenuBar(menuBar);
    }

    public void drawLabels(){
        JLabel withdrawLabel = new JLabel("Withdrawals");
        JLabel depositLabel = new JLabel("Deposits");

        withdrawLabel.setBounds(135,230,150,40);
        withdrawLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));

        depositLabel.setBounds(560,230,150,40);
        depositLabel.setFont(new Font("Trebuchet MS", Font.PLAIN, 20));

        historyFrm.add(withdrawLabel);
        historyFrm.add(depositLabel);
        try {
            BufferedImage myPicture = ImageIO.read(new File(path+"\\img\\header.png"));
            Image newImage = myPicture.getScaledInstance(800, 225, Image.SCALE_AREA_AVERAGING);
            JLabel header = new JLabel(new ImageIcon(newImage));
            header.setBounds(0,0,800,225);

            historyFrm.add(header);
        } catch (IOException error){
            error.printStackTrace();
        }
    }

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
        returnBtn.setFont(new Font("Trebuchet MS", Font.PLAIN, 16));
        returnBtn.setBackground(new Color(237,226,222));

        historyFrm.add(returnBtn);

        returnBtn.addActionListener(e -> {
            historyFrm.dispose();
            account.setVisible(true);
        });
    }
}
