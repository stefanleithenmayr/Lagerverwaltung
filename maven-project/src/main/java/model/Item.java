package model;

import loginPackage.DBConnection;

import java.sql.SQLException;

public class Item {

            private String name;
            private String description;
            private Integer id;

            public Item(String name, String description, Integer id){
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

            public Integer getId() {
                return id;
            }

            public Integer getTotalExemplars() throws SQLException {
                return DBConnection.getInstance().countExemplars(this.getId());
            }
    @Override
    public String toString() {
        return this.getName();
    }
}
