import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/*
    Class that contains auxiliary functions and variables that are static so that other
    classes can refer to these functions directly. The functions  and variables in this
    class are used multiple times across different classes.
 */


public class Auxiliary {
    protected static File currentDir = new File("");
    protected static String path = currentDir.getAbsolutePath();
    public static Color mainColor = new Color(222,194,186);
    public static Color subColor = new Color(237,226,222);
    public static Color highlightColor = new Color(251,250,249);
    public static Font font_14 = new Font("Trebuchet MS", Font.PLAIN, 14);
    public static Font font_15 = new Font("Trebuchet MS", Font.PLAIN, 15);
    public static Font font_16 = new Font("Trebuchet MS", Font.PLAIN, 16);
    public static Font font_17 = new Font("Trebuchet MS", Font.PLAIN, 17);
    public static Font font_20 = new Font("Trebuchet MS", Font.PLAIN, 20);


    // Obtains string text and prepares other variables to be used in order to generate a qr code
    public static boolean convertQR(String text) throws WriterException, IOException {
        String filePath = path + "\\QRCode.png";
        int size = 600;
        String fileType = "png";
        File qrFile = new File(filePath);
        createQRImage(qrFile, text, size, fileType);

        return true;
    }

    // Function which creates the qr codes and stores them as a png file within the project directory
    private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException {
        // Creates the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        // Makes the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        int matrixHeight = byteMatrix.getHeight();
        BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixHeight);
        // Paints and save the image using the ByteMatrix
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < matrixWidth; i++) {
            for (int j = 0; j < matrixWidth; j++) {
                if (byteMatrix.get(i, j)) {
                    graphics.fillRect(i, j, 1, 1);
                }
            }
        }
        ImageIO.write(image, fileType, qrFile);
    }

    // Function which aids in the functionality of the JDatepicker object. Is used to format
    // the date in these objects into a year-month-day format.
    public static class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);
        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) {
            if (value != null) {
                Calendar cal = (Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }

    // Function which obtains the values from the WITHDRAW_HISTORY Table found in the database and
    // creates a table based off of it.
    public static JTable getWithdrawValues(int ID){
        PreparedStatement pst;
        ResultSet rs;
        Connection con;
        Vector<Vector<String>> withdrawList = new Vector<>(); //nested vector which will contain table values
        int i = 0;
        Vector<String> withdrawCol = new Vector<>(3,2);
        // defines the table columns
        withdrawCol.addElement("Withdrawal ID");
        withdrawCol.addElement("Date of Withdrawal");
        withdrawCol.addElement("Amount Withdrawn");

        try {
            String withdrawID;
            String sqlDate;
            String currency;

            // Prepares a sql statement to be executed in order to retrieve the values in the database
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");
            String sql = "select * from WITHDRAW_HISTORY where customer_ID='" + ID + "'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                i++;
            }
            for(int j = 0; j < i; j++)  {
                withdrawList.add(new Vector<>());
            }
            rs = pst.executeQuery();
            i = 0;
            while(rs.next()){
                withdrawID = rs.getString("withrdraw_ID");
                sqlDate = rs.getString("withdrawalDate");
                currency = rs.getString("withdrawalAmount");

                String newDate = sqlDate.substring(0,10);

                // inserts the values from the database into the vector
                // the iterator (i) denotes the current row number
                withdrawList.get(i).add(withdrawID);
                withdrawList.get(i).add(newDate);
                withdrawList.get(i).add("Php " + currency);
                i++;
            }
        } catch(SQLException | ClassNotFoundException error) {
            error.printStackTrace();
        }

        // creates a table based on the values retrieved from the database
        JTable withdrawTable = new JTable(withdrawList, withdrawCol);
        withdrawTable.setSize(325,275);
        withdrawTable.setDefaultEditor(Object.class, null);

        return withdrawTable;
    }

    // Function which obtains the values from the DEPOSIT_HISTORY Table found in the database and
    // creates a table based off of it.
    public static JTable getDepositValues(int ID){
        PreparedStatement pst;
        ResultSet rs;
        Connection con;
        int i = 0;
        Vector<Vector<String>> depositList = new Vector<>(); //nested vector which will contain table values
        try {
            String withdrawID;
            String sqlDate;
            String currency;

            // Prepares a sql statement to be executed in order to retrieve the values in the database
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con = DriverManager.getConnection("jdbc:ucanaccess://" + path +"\\GUI_Database.accdb");
            String sql = "select * from DEPOSIT_HISTORY where customer_ID='" + ID + "'";
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while(rs.next()){
                i++;
            }
            for(int j = 0; j < i; j++)  {
                depositList.add(new Vector<>());
            }
            rs = pst.executeQuery();
            i = 0;
            while(rs.next()){
                withdrawID = rs.getString("deposit_ID");
                sqlDate = rs.getString("depositedDate");
                currency = rs.getString("depositedAmount");

                String newDate = sqlDate.substring(0,10);

                // inserts the values from the database into the vector
                // the iterator (i) denotes the current row number
                depositList.get(i).add(withdrawID);
                depositList.get(i).add(newDate);
                depositList.get(i).add("Php " + currency);
                i++;
            }
        } catch(SQLException | ClassNotFoundException error) {
            error.printStackTrace();
        }

        Vector<String> depositCol = new Vector<>(3,2);
        depositCol.addElement("Deposit ID");
        depositCol.addElement("Date of Deposit");
        depositCol.addElement("Amount Deposited");

        // creates a table based on the values retrieved from the database
        JTable depositTable = new JTable(depositList, depositCol);
        depositTable.setSize(325,275);
        depositTable.setDefaultEditor(Object.class,null);

        return depositTable;
    }
}
