package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class User {

    private final String username, password, name, email, klasse;
    private final int userrole;
    private final TextField nameField, userNameField, passwordField, emailField, klasseField, userroleField;
    private final SimpleStringProperty realName;
    private CheckBox selected;
    private boolean isActivated;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public CheckBox getSelected() {
        return selected;
    }

    public void setSelected(CheckBox selected) {
        this.selected = selected;
    }
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

    public TextField getEmail(){
        this.emailField.setDisable(this.isActivated);
        this.emailField.setText(this.email);
        return this.emailField;
    }

    public TextField getKlasse(){
        this.klasseField.setDisable(this.isActivated);
        this.klasseField.setText(this.klasse);
        return this.klasseField;
    }

    public TextField getUserRole(){
        this.userroleField.setDisable(this.isActivated);
        this.userroleField.setText(Integer.toString(this.userrole));
        return this.userroleField;
    }
    public String getRealName() {
        return realName.get();
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

    public TextField getEmailField() {
        return emailField;
    }

    public TextField getKlasseField() {
        return klasseField;
    }

    public TextField getUserroleField() {
        return userroleField;
    }

    public User(String username, String password, String name, String email, String klasse, int userrole) {
        this.nameField = new TextField();
        this.userNameField = new TextField();
        this.passwordField = new TextField();
        this.klasseField = new TextField();
        this.emailField = new TextField();
        this.userroleField = new TextField();
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.klasse = klasse;
        this.userrole = userrole;
        this.isActivated = true;
        this.realName = new SimpleStringProperty(name);
        selected = new CheckBox();
    }
}
