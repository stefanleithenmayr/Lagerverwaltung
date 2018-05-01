package model;

import javafx.scene.control.CheckBox;
import loginPackage.DBConnection;

import java.sql.SQLException;

public class ProductType {
    private Integer productTypeID;
    private String typeName;
    private String description;
    private CheckBox selected;

    public ProductType(Integer productTypeID, String typeName, String description){
        this.productTypeID = productTypeID;
        this.typeName = typeName;
        this.description = description;
        selected = new CheckBox();
    }
    public ProductType(Integer productTypeID, String typeName, String description, CheckBox checkBox){
        this.productTypeID = productTypeID;
        this.typeName = typeName;
        this.description = description;
        this.selected = checkBox;
    }
    public Integer getProductTypeID() {
        return productTypeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public CheckBox getSelected() {return selected; }

    public void setSelected(CheckBox selected) {this.selected = selected;}

    public String getDescription() {
        return description;
    }
    public int getAvailAbleProducts() throws SQLException {
        return DBConnection.getInstance().getAvailAbleProductsByProductType(this.productTypeID);
    }
    public String getTotalProducts() throws SQLException {
        try{
            Integer.parseInt(getDescription());
            return "";
        }
        catch(Exception e){
            return Integer.toString(DBConnection.getInstance().getTotalProductsByProductTypeID(this.productTypeID));
        }

    }
}
