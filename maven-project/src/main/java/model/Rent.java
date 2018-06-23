package model;

public class Rent {

    private final String userName;
    private final String from;
    private final String until;

    public Rent(String userName, String from, String until) {
        this.userName = userName;
        this.from = from;
        this.until = until;
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
}