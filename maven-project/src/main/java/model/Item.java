package model;

import loginPackage.DBConnection;

import java.sql.SQLException;

public class Item {

            private String name;
            private String description;
            private String id;

            public Item(String name, String description, String id){
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
                    return DBConnection.getInstance().countExemplars(Integer.parseInt(this.getId()));
                }
                return "";
            }
    @Override
    public String toString() {
        return this.getName();
    }
}
