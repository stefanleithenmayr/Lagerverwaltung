package model;

import javafx.scene.control.TextField;

public class User {

    private String username, password, name;
    private TextField nameField, userNameField, passwordField;

    public TextField getUsername() {
        this.userNameField.setText(this.username);
        this.userNameField.setDisable(true);
        return this.userNameField;
    }

    public TextField getPassword() {
        this.passwordField.setDisable(true);
        this.passwordField.setText(this.password);
        return this.passwordField;
    }

    public TextField getName() {
        this.nameField.setDisable(true);
        this.nameField.setText(this.name);
        return this.nameField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextField getUserNameField() {
        return userNameField;
    }

    public TextField getPasswordField() {
        return passwordField;
    }

    public User(String username, String password, String name) {
        this.nameField = new TextField();
        this.userNameField = new TextField();
        this.passwordField = new TextField();
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public void handleFields(boolean b){
        nameField.setDisable(b);
        userNameField.setDisable(b);
        passwordField.setDisable(b);
    }
}
