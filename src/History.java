import javax.swing.*;
import java.awt.*;


public class History implements Draw{
    protected JFrame historyFrm = new JFrame();

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
    }

    @Override
    public void drawMenu(int ID){
        JMenu menu = new JMenu("Options");
        JMenuBar menuBar = new JMenuBar();
        JMenuItem item1, item2;

        item1 = new JMenuItem("Logout");
        item1.addActionListener(e -> {
            historyFrm.dispose();
            new Login();
        });

        item2 = new JMenuItem("Exit");
        item2.addActionListener(e -> {
            int a = JOptionPane.showConfirmDialog(historyFrm, "Are you sure you want to exit?");
            if(a== JOptionPane.YES_OPTION){
                System.exit(0);
            }
        });

        menu.add(item1);
        menu.add(item2);
        menuBar.add(menu);
        historyFrm.setJMenuBar(menuBar);
    }

    public void drawLabels(){
        //JLabel transactionLabel = new JLabel("Transactions");
        JPanel header = new JPanel();
        header.setBounds(0,0,800,150);
        header.setBackground(new Color(196,240,173));

        JLabel withdrawLabel = new JLabel("Withdrawals");
        JLabel depositLabel = new JLabel("Deposits");

        //transactionLabel.setBounds(345,30,150,40);
        //transactionLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));

        withdrawLabel.setBounds(135,175,150,40);
        withdrawLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));

        depositLabel.setBounds(560,175,150,40);
        depositLabel.setFont(new Font("Sans Serif", Font.PLAIN, 20));

        //historyFrm.add(transactionLabel);
        historyFrm.add(withdrawLabel);
        historyFrm.add(depositLabel);
        historyFrm.add(header);
    }

    public void drawTables(int ID) {
        //JTable transactionTable = new JTable();

        //JScrollPane transactPane = new JScrollPane(transactionTable);
        JScrollPane withdrawPane = new JScrollPane(Auxiliary.getWithdrawValues(ID));
        JScrollPane depositPane = new JScrollPane(Auxiliary.getDepositValues(ID));

        //transactPane.setBounds(230,70,350,225);
        withdrawPane.setBounds(55,225,325,290);
        depositPane.setBounds(440,225,325,290);

        //historyFrm.add(transactPane);
        historyFrm.add(withdrawPane);
        historyFrm.add(depositPane);
    }


    public void drawButtons(JFrame account) {
        JButton returnBtn = new JButton("Return");
        returnBtn.setBounds(660,575,75,25);

        historyFrm.add(returnBtn);

        returnBtn.addActionListener(e -> {
            historyFrm.dispose();
            account.setVisible(true);
        });
    }
}
