package model;

import javafx.scene.control.CheckBox;
import loginPackage.DBConnection;

import java.sql.SQLException;

public class TestProduct {

    private Integer productID;
    private Integer producttypeID;
    private String productEan;
    private Integer superProductID;
    private Integer statusID;
    private CheckBox selected;
    private int hyrachieNumber;

    public TestProduct(Integer productID, Integer producttypeID, String picture, String productean, Integer superProductID, Integer statusID, int hyrachieNumber){
        this.productID = productID;
        this. producttypeID = producttypeID;
        this.productEan = productean;
        this.superProductID = superProductID;
        this.statusID = statusID;
        this.hyrachieNumber = hyrachieNumber;
        selected = new CheckBox();
    }
    public CheckBox getSelected() {return selected; }

    public void setSelected(CheckBox selected) {this.selected = selected;}


    public Integer getProductID() {
        return productID;
    }

    public Integer getProducttypeID() {
        return producttypeID;
    }

    public String getProductEan() {
        return productEan;
    }

    public Integer getSuperProductID() {
        return superProductID;
    }

    public Integer getStatusID() {
        return statusID;
    }
    public String getProductTypeName() throws SQLException {
        return DBConnection.getInstance().getProductTypeNameByID(this.producttypeID);
    }
    public String getProductTypeDescription() throws SQLException {
        return DBConnection.getInstance().getProductTypeDescriptionByID(this.producttypeID);
    }

    public int getHyrachieNumber() {
        return hyrachieNumber;
    }
}