package model;

import loginPackage.DBConnection;

import java.sql.SQLException;

public class Product {

            private final String name;
            private final String description;
            private final String id;

            public Product(String name, String description, String id){
                this.name = name;
                this.description = description;
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public String getDescription() {
                return description;
            }

            public String getId() {
                return id;
            }

            public String getTotalExemplars() throws SQLException {
                if (!this.getId().isEmpty()){
                    return DBConnection.getInstance().countItems(Integer.parseInt(this.getId()));
                }
                return "";
            }

            public String getAvailableExemplars() throws SQLException {
                String total = getTotalExemplars();
                if (!this.getId().isEmpty() && !total.isEmpty()) {
                    return Integer.toString(Integer.parseInt(total) - Integer.parseInt(DBConnection.getInstance().getAvailableItemsCount(Integer.parseInt(this.getId()))));
                }
                return "";
            }
    @Override
    public String toString() {
        return this.getName();
    }
}
