package model;

public class User {

    private String username, password, name;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public User(String username, String password, String name) {

        this.username = username;
        this.password = password;
        this.name = name;
    }
}
