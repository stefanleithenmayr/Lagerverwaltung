package model;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import loginPackage.DBConnection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.List;

public class BarcodesToPdfGenerator {

    public static void generateBarcoeds(List<Product> products) throws FileNotFoundException, DocumentException, SQLException {
        int x = 20, y = 800;
        File selectedDirectory = fileChooser();
        if ((selectedDirectory == null) || selectedDirectory.equals("")){
            return;
        }
        Document doc  = new Document(new Rectangle(PageSize.A4));

        PdfWriter pdfWriter = PdfWriter.getInstance(doc, new FileOutputStream(selectedDirectory + "\\barcodes.pdf"));
        doc.open();
        String productName = DBConnection.getInstance().getProductTypeNameByID(DBConnection.getInstance().getProductTypeIdByProductID(products.get(0).getProductID()));
        String lastProductName;
        Barcode128 barcode128 = new Barcode128();
        for (int i = 0; i < products.size(); i++){
            if (Integer.toString(products.get(i).getProductID()) != null &&
                    !Integer.toString(products.get(i).getProductID()).equals("")){
                lastProductName = productName;
                productName =DBConnection.getInstance().getProductTypeNameByID(DBConnection.getInstance().getProductTypeIdByProductID(products.get(i).getProductID()));
                if (!lastProductName.equals(productName)){
                    y-=70;
                }
                ColumnText ct = new ColumnText(pdfWriter.getDirectContent());
                ct.setSimpleColumn(x,y,600,0);//600 600
                Paragraph p=new Paragraph();
                p.add(productName);
                ct.addElement(p);
                ct.go();


                barcode128.setCode(Integer.toString(products.get(i).getProductID()));
                Image a = barcode128.createImageWithBarcode(pdfWriter.getDirectContent(), null, null);
                a.setAbsolutePosition(x  , y);
                if(productName.length() > 40){
                    x = 20;
                }
                else x = getNextXPosition2Colums(x);
                if(x == 20)y -= 70;
                doc.add(a);
            }
        }
        doc.close();
    }

    private static int getNextXPosition2Colums(int x) {
        if (x == 20) return 270;
        return 20;
    }

    private static File fileChooser() {
        Stage primaryStage = new Stage();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(primaryStage);
    }
}
