package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import loginPackage.DBConnection;
import model.User;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ExportDatasController implements Initializable {
    @FXML
    JFXComboBox<String> cbDatas, cbFormat;
    @FXML
    JFXButton btnExport;

    @FXML
    private void exportDatas(ActionEvent event) throws SQLException, IOException {
        if (cbFormat.getSelectionModel().getSelectedItem().equals("PDF")){
            if (cbDatas.getSelectionModel().getSelectedItem().equals("Users")){
                /*try {
                    PDDocument doc = new PDDocument();
                    PDPage page = new PDPage();
                    doc.addPage(page);

                    PDPageContentStream content = new PDPageContentStream(doc, page);

                    content.beginText();
                    //content.setFont(PDType1Font.HELVETICA ,26);
                    content.moveTextPositionByAmount(250,750);
                    content.drawString("Hallo Welt");
                    content.endText();

                    content.close();
                    doc.save("Users.pdf");
                    doc.close();

                    System.out.println("your File createt in: "+ System.getProperty("user.dir"));


                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }*/
                List<User> users = DBConnection.getInstance().getUsers();
                PDDocument document = new PDDocument();
                PDPage page = new PDPage();
                document.addPage(page);
                PDPageContentStream contentStream = new PDPageContentStream(document, page);

                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA ,26);
                contentStream.moveTextPositionByAmount(250,750);
                contentStream.drawString("USERS");
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

                writeToPdf(contentStream, content);
                contentStream.close();

                Stage primaryStage = new Stage();

                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory =
                        directoryChooser.showDialog(primaryStage);


                document.save(selectedDirectory+"\\Users.pdf");
            }
        }
        else if (cbFormat.getSelectionModel().getSelectedItem().equals("CSV")){

        }
    }

    public static void writeToPdf(PDPageContentStream contentStream, String[][] data) throws IOException {
        final int rows = data.length;
        final int cols = data[0].length;

        final float rowHeight = 20f;
        final float tableWidth = 550;

        final float tableHeight = rowHeight * rows;
        final float colWidth = tableWidth / (float) cols;
        final float cellMargin = 5f;
        float margin = 20;
        float y = 750;
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
        cbDatas.getItems().add("Users");
    }
}
