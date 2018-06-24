package model;

public class Rent {

    private final String userName;
    private final String from;
    private final String until;
    private final Integer rentID;

    public Rent(Integer rentID, String userName, String from, String until) {
        this.userName = userName;
        this.from = from;
        this.until = until;
        this.rentID = rentID;

    }

    public String getUserName() {
        return userName;
    }

    public String getFrom() {
        return from;
    }

    public String getUntil() {
        return until;
    }

    public Integer getRentID() {
        return rentID;
    }
}