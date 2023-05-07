import java.awt.Color;
import java.awt.Graphics2D;
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


public class Auxiliary {
    protected static File currentDir = new File("");
    protected static String path = currentDir.getAbsolutePath();
    public static boolean convertQR(String text) throws WriterException, IOException {
        String filePath = path + "\\QRCode.png";
        int size = 600;
        String fileType = "png";
        File qrFile = new File(filePath);
        createQRImage(qrFile, text, size, fileType);

        return true;
    }

    private static void createQRImage(File qrFile, String qrCodeText, int size, String fileType) throws WriterException, IOException {
        // Create the ByteMatrix for the QR-Code that encodes the given String
        Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap = new Hashtable<>();
        hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix byteMatrix = qrCodeWriter.encode(qrCodeText, BarcodeFormat.QR_CODE, size, size, hintMap);
        // Make the BufferedImage that are to hold the QRCode
        int matrixWidth = byteMatrix.getWidth();
        int matrixHeight = byteMatrix.getHeight();
        BufferedImage image = new BufferedImage(matrixWidth, matrixHeight, BufferedImage.TYPE_INT_RGB);
        image.createGraphics();

        Graphics2D graphics = (Graphics2D) image.getGraphics();
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, matrixWidth, matrixHeight);
        // Paint and save the image using the ByteMatrix
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

    public static JTable getWithdrawValues(int ID){
        PreparedStatement pst;
        ResultSet rs;
        Connection con;
        Vector<Vector<String>> withdrawList = new Vector<>();
        int i = 0;
        //String[] transactColumn = {"Transaction ID", "Location of Transaction","Date of Transaction", "Amount Paid"};
        Vector<String> withdrawCol = new Vector<>(3,2);
        withdrawCol.addElement("Withdrawal ID");
        withdrawCol.addElement("Date of Withdrawal");
        withdrawCol.addElement("Amount Withdrawn");

        try {
            String withdrawID;
            String sqlDate;
            String currency;

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

                withdrawList.get(i).add(withdrawID);
                withdrawList.get(i).add(newDate);
                withdrawList.get(i).add("₱ " + currency);
                i++;
            }
        } catch(SQLException | ClassNotFoundException error) {
            error.printStackTrace();
        }

        JTable withdrawTable = new JTable(withdrawList, withdrawCol);
        withdrawTable.setSize(325,290);
        withdrawTable.setDefaultEditor(Object.class, null);

        return withdrawTable;
    }

    public static JTable getDepositValues(int ID){
        PreparedStatement pst;
        ResultSet rs;
        Connection con;
        int i = 0;
        Vector<Vector<String>> depositList = new Vector<>();
        try {
            String withdrawID;
            String sqlDate;
            String currency;

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

                depositList.get(i).add(withdrawID);
                depositList.get(i).add(newDate);
                depositList.get(i).add("₱ " + currency);
                i++;
            }
        } catch(SQLException | ClassNotFoundException error) {
            error.printStackTrace();
        }

        Vector<String> depositCol = new Vector<>(3,2);
        depositCol.addElement("Deposit ID");
        depositCol.addElement("Date of Deposit");
        depositCol.addElement("Amount Deposited");

        JTable depositTable = new JTable(depositList, depositCol);
        depositTable.setSize(325,290);
        depositTable.setDefaultEditor(Object.class,null);

        return depositTable;
    }
}
