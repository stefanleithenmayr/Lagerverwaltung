package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.net.URL;
import java.util.ResourceBundle;

public class ExportDatasController implements Initializable {
    @FXML
    JFXComboBox<String> cbDatas, cbFormat;
    @FXML
    JFXButton btnExport;

    @FXML
    private void exportDatas(ActionEvent event){
        if (cbFormat.getSelectionModel().getSelectedItem().equals("PDF")){
            if (cbDatas.getSelectionModel().getSelectedItem().equals("Users")){
                try {
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
                }
            }
        }
        else if (cbFormat.getSelectionModel().getSelectedItem().equals("CSV")){

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
