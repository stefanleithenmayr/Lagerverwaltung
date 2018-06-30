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
        StringBuilder output = new StringBuilder("     ");
        for (int i = 0; i < level*10; i++) {
            output.append(" ");
        }
        output.append(this.productNr);
        return output.toString();

    }

    public String getProductName() {
        StringBuilder output = new StringBuilder(String.format("%15s", ""));
        for (int i = 0; i < level*10; i++) {
            output.append(" ");
        }
        output.append(this.productName);
        return output.toString();
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
