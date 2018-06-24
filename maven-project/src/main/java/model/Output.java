package model;

public class Output {
    private String productNr, productName, successfully, username;
    private int level;

    public Output(String productNr, String productName, String username, int level) {
        this.productNr = productNr;
        this.productName = productName;
        this.username = username;
        this.level = level;
    }

    public String getProductNr() {
        String output = "     ";
        for (int i = 0; i < level*10; i++) {
            output += " ";
        }
        output += this.productNr;
        return output;

    }

    public String getProductName() {
        String output = String.format("%15s", "");
        for (int i = 0; i < level*10; i++) {
            output += " ";
        }
        output += this.productName;
        return output;
    }

    public String getSuccessfully() {
        return successfully;
    }

    public void setSuccessfully(String successfully) {
        this.successfully = successfully;
    }

    public String getUsername() {
        return username;
    }
}
