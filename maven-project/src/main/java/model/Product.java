package model;

import javafx.scene.control.CheckBox;

import java.sql.SQLException;

public class Product {

    private Integer productID;
    private Integer producttypeID;
    private String productEan;
    private Integer superProductID;
    private Integer statusID;
    private CheckBox selected;
    private String productTypeName;
    private String productTypeDescription;

    public Boolean getIsChild() {
        return isChild;
    }

    public void setIsChild(Boolean listHeader) {
        this.isChild = listHeader;
    }

    private Boolean isChild;

    public Product(Integer productID, Integer producttypeID, String picture, String productean, Integer superProductID, Integer statusID){
        this.productID = productID;
        this. producttypeID = producttypeID;
        this.productEan = productean;
        this.superProductID = superProductID;
        this.statusID = statusID;
        selected = new CheckBox();
        setIsChild(false);
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

    public void setProductTypeName(String productTypeName) {
        this.productTypeName = productTypeName;
    }

    public void setProductTypeDescription(String productTypeDescription) {
        this.productTypeDescription = productTypeDescription;
    }

    public Integer getSuperProductID() {
        return superProductID;
    }

    public Integer getStatusID() {
        return statusID;
    }
    public String getProductTypeName() throws SQLException {
        return this.productTypeName;
        //return DBConnection.getInstance().getProductTypeNameByID(this.producttypeID);
    }
    public String getProductTypeDescription() throws SQLException {
        if (isChild) return this.getProductID().toString();
        //return DBConnection.getInstance().getProductTypeDescriptionByID(this.producttypeID);
        return this.productTypeDescription;
    }
}
