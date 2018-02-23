package model;

public class Rent {

    private final String id;
    private final String itemName;
    private final String userName;

    public Rent(String itemName, String id, String userName) {
        this.itemName = itemName;
        this.id = id;
        this. userName = userName;
    }

    public String getID() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }
    public String getUserName() {
        return userName;
    }
}