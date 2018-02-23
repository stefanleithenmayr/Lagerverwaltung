package controller;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import java.security.Key;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.*;

public class ExportDatasController implements Initializable {
    @FXML
    JFXComboBox<String> cbDatas;
    @FXML
    JFXButton btnExport;
    @FXML
    JFXTextField tfFileName,tfSearchName;
    @FXML
    TableView<User> tvUser;
    ObservableList<User> users;
    @FXML
    TableColumn<User,String> tcUser;
    @FXML
    TableColumn tcSelect;

    @FXML
    private void checkIfRentsByUser(MouseEvent event){
        if (cbDatas.getSelectionModel().getSelectedItem().equals("Users Rents")){
            tvUser.setVisible(true);
            tfSearchName.setVisible(true);
            tcUser.setVisible(true);
        }
        else{
            tvUser.setVisible(false);
            tfSearchName.setVisible(false);
            tcUser.setVisible(false);
        }
    }
    @FXML
    private  void searchUser(KeyEvent event){
        tvUser.getItems().clear();
        try {
            users = FXCollections.observableArrayList(DBConnection.getInstance().getUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        KeyCode keycode = event.getCode();
        String search = tfSearchName.getText();
        if(keycode == KeyCode.BACK_SPACE && search.length() > 0){
            search = search.substring(0,search.length()-1);
        }
        else search += event.getText();

        for (int i = 0; i < users.size(); i++){
            if (users.get(i).getRealName().toLowerCase().contains(search.toLowerCase())){
                tvUser.getItems().add(users.get(i));
            }
        }
    }

    @FXML
    private void exportDatas() throws SQLException, IOException {
        GregorianCalendar now = new GregorianCalendar();
        DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        if (cbDatas.getSelectionModel().getSelectedItem().equals("Users")) {
            List<User> users = DBConnection.getInstance().getUsers();
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA ,20);
            contentStream.newLineAtOffset(150,750);
            contentStream.showText("USERS, am "+DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT).format(now.getTime()));

            contentStream.endText();

            String[][] content = new String[users.size() + 1][3];
            content[0][0] = "Username";
            content[0][1] = "RealName";
            content[0][2] = "Password";

            for (int i = 0; i < users.size(); i++) {
                content[i + 1][0] = users.get(i).getUsername().getText();
                content[i + 1][1] = users.get(i).getName().getText();
                content[i + 1][2] = users.get(i).getPassword().getText();
            }
            String fileName = "";
            if (tfFileName.getText() == null || tfFileName.getText().equals("")) {
                enterFileNameWindow();
                return;
            }
            else fileName = tfFileName.getText();
            File selectedDirectory = fileChooser();
            if (selectedDirectory == null || selectedDirectory.equals("")){
                return;
            }
            writeToPdf(contentStream, content);
            contentStream.close();
            File f = new File(selectedDirectory + "\\" + fileName + ".pdf");
            while(f.exists()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Look, a Confirmation Dialog");
                alert.setContentText("Choose your option.");

                ButtonType buttonTypeOverwrite = new ButtonType("overwrite");
                ButtonType buttonTypeChangeFileName = new ButtonType("change filename");
                ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeOverwrite, buttonTypeChangeFileName, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOverwrite){
                    f.delete();
                } else if (result.get() == buttonTypeChangeFileName) {
                    return;
                } else {
                    return;
                }
                f = new File(selectedDirectory + "\\" + fileName + ".pdf");
            }
            if (selectedDirectory != null && !fileName.equals("")) {
                document.save(selectedDirectory + "\\" + fileName + ".pdf");
            }
            fileName = "";
        }
        else if(cbDatas.getSelectionModel().getSelectedItem().equals("Users Rents")){

        }
    }
    private void enterFileNameWindow() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Enter a filename!");

        alert.showAndWait();
    }

    private File fileChooser() {
        Stage primaryStage = new Stage();

        DirectoryChooser directoryChooser = new DirectoryChooser();
        return directoryChooser.showDialog(primaryStage);
    }

    private static void writeToPdf(PDPageContentStream contentStream, String[][] data) throws IOException {
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
            contentStream.moveTo(margin, anoy);
            contentStream.lineTo(margin+tableWidth, anoy);
            contentStream.stroke();
            anoy -= rowHeight;
        }

        float anox = margin;
        for (int i = 0; i <= cols; i++) {
            contentStream.moveTo(anox, y);
            contentStream.lineTo(anox, y-tableHeight);
            contentStream.stroke();
            anox += colWidth;
        }

        contentStream.setFont(PDType1Font.COURIER_BOLD, 10);

        float textx = margin + cellMargin;
        float texty = y - 15;
        for (String[] aData : data) {
            for (String anAData : aData) {
                if (anAData != null) {
                    String text = anAData;

                    contentStream.beginText();
                    contentStream.newLineAtOffset(textx,texty);
                    contentStream.showText(text);
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
        cbDatas.getItems().add("Users");
        cbDatas.getItems().add("Store Journal");
        cbDatas.getItems().add("Users Rents");
        cbDatas.getSelectionModel().selectFirst();

        tcUser.setCellValueFactory(new PropertyValueFactory<>("realName"));
        tcSelect.setCellValueFactory(new PropertyValueFactory<>("selected"));

        try {
            users = FXCollections.observableArrayList(DBConnection.getInstance().getUsers());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        tvUser.setItems(users);
    }
}
