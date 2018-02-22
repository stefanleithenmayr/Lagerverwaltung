package model;

public class Rent {

    private final String id;
    private final String itemName;

    public Rent(String itemName, String id) {
        this.itemName = itemName;
        this.id = id;
    }

    public String getID() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }
}