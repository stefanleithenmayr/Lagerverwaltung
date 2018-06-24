package model;

public class SimpleOutput {

    private String productNr, productName;

    public SimpleOutput(String productNr, String productName) {
        this.productNr = productNr;
        this.productName = productName;
    }

    public String getProductNr() {
        return productNr;
    }

    public String getProductName() {
        return productName;
    }
}
