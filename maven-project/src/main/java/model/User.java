package model;

import javafx.scene.control.TextField;

public class User {

    private String username, password, name;
    private TextField nameField, userNameField, passwordField;

    private boolean b;

    public TextField getUsername() {
        this.userNameField.setText(this.username);
        this.userNameField.setDisable(b);
        return this.userNameField;
    }

    public TextField getPassword() {
        this.passwordField.setDisable(b);
        this.passwordField.setText(this.password);
        return this.passwordField;
    }

    public TextField getName() {
        this.nameField.setDisable(b);
        this.nameField.setText(this.name);
        return this.nameField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public void setB(boolean b) {
        this.b = b;
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
        b = true;
    }
}
