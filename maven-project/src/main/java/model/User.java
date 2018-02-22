package model;

import javafx.scene.control.TextField;

public class User {

    private final String username, password, name;
    private final TextField nameField, userNameField, passwordField;

    private boolean isActivated;

    public TextField getUsername() {
        this.userNameField.setText(this.username);
        this.userNameField.setDisable(this.isActivated);
        return this.userNameField;
    }

    public TextField getPassword() {
        this.passwordField.setDisable(this.isActivated);
        this.passwordField.setText(this.password);
        return this.passwordField;
    }

    public TextField getName() {
        this.nameField.setDisable(this.isActivated);
        this.nameField.setText(this.name);
        return this.nameField;
    }

    public TextField getNameField() {
        return nameField;
    }

    public void setIsActivated(boolean isActivated) {
        this.isActivated = isActivated;
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
        this.isActivated = true;
    }
}
