package model;

public claRent {

    private final String id;
    private final String itemName;
    private final String userName;
    private final String fullName;

    public Rent(String itemName, String id, String userName, String fullName) {
        this.itemName = itemName;
        this.id = id;
        this.userName = userName;
        this.fullName = fullName;
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

    public String getFullName() {
        return fullName;
    }
}