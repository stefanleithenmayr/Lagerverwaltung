package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.DirectoryChooser;
import loginPackage.DBConnection;
import model.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

public class ExportDatasController implements Initializable {
    @FXML
    JFXComboBox<String> cbDatas, cbFormat;
    @FXML
    JFXButton btnExport;
    @FXML
    JFXTextField tfFileName,tfError;

    @FXML
    private void exportDatas(ActionEvent event) throws SQLException, IOException {
        tfError.setVisible(false);
        GregorianCalendar now = new GregorianCalendar();
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);


        if (cbFormat.getSelectionModel().getSelectedItem().equals("PDF")){
            if (cbDatas.getSelectionModel().getSelectedItem().equals("Users")){
                List<User> users = DBConnection.getInstance().getUsers();
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA ,20);
                contentStream.moveTextPositionByAmount(150,750);
                contentStream.drawString("USERS, am"+DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(now.getTime()));
                contentStream.endText();

                String[][] content = new String[users.size()+1][3];
                content[0][0] = "Username";
                content[0][1] = "RealName";
                content[0][2] = "Password";

                for (int i = 0;i < users.size();i++){
                    content[i+1][0] = users.get(i).getUsername().getText();
                    content[i+1][1] = users.get(i).getName().getText();
                    content[i+1][2] = users.get(i).getPassword().getText();
                }

                if (tfFileName.getText() == null || tfFileName.getText().equals("")){
                    tfError.setText("enter a filename!");
                    tfError.setVisible(true);
                    return;
                }

                File selectedDirectory = fileChooser();
                writeToPdf(contentStream, content);
                contentStream.close();

                File f = new File(selectedDirectory+"\\"+tfFileName.getText()+".pdf");
                if(f.exists()) {
                    tfError.setVisible(true);
                    tfError.setText("File already exists");
                    return;
                }

                if (selectedDirectory != null){
                    document.save(selectedDirectory+"\\"+tfFileName.getText()+".pdf");
                }
            }
        }
        else if (cbFormat.getSelectionModel().getSelectedItem().equals("CSV")){

        }
    }

    private File fileChooser() {
        Stage primaryStage = new Stage();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(primaryStage);
    }

    public static void writeToPdf(PDPageContentStream contentStream, String[][] data) throws IOException {
        final int rows = data.length;
        final int cols = data[0].length;

        final float rowHeight = 20f;
        final float tableWidth = 550;

        final float tableHeight = rowHeight * rows;
        final float colWidth = tableWidth / (float) cols;
        final float cellMargin = 5f;
        float margin = 30;
        float y = 730;
        float anoy = y;

        for (int i = 0; i <= rows; i++) {
            contentStream.drawLine(margin, anoy, margin + tableWidth, anoy);
            anoy -= rowHeight;
        }

        float anox = margin;
        for (int i = 0; i <= cols; i++) {
            contentStream.drawLine(anox, y, anox, y - tableHeight);
            anox += colWidth;
        }

        contentStream.setFont(PDType1Font.COURIER_BOLD, 10);

        float textx = margin + cellMargin;
        float texty = y - 15;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != null) {
                    String text = data[i][j];

                    contentStream.beginText();
                    contentStream.moveTextPositionByAmount(textx, texty);
                    contentStream.drawString(text);
                    contentStream.endText();
                    textx += colWidth;
                }
            }
            texty -= rowHeight;
            textx = margin + cellMargin;
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbFormat.getItems().add("PDF");
        cbFormat.getItems().add("CSV");
        cbDatas.getItems().add("Users");
        cbDatas.getItems().add("Store Journal");
        cbDatas.getItems().add("Users Rents");
        cbDatas.getSelectionModel().selectFirst();
        cbFormat.getSelectionModel().selectFirst();
    }
}
