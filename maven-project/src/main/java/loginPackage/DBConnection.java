package loginPackage;

import java.sql.*;

public class DBConnection {
    private static DBConnection INSTANCE;

    public static final String DRIVER_STRING = "org.apache.derby.jdbc.ClientDriver";
    static final String CONNECTION_STRING = "jdbc:derby://localhost:1527/db";
    public static String userName;
    public static String password;
    private Connection conn;

    private DBConnection() { }
    public static DBConnection getInstance() {
        if(INSTANCE == null){
            INSTANCE = new DBConnection();
        }
        return INSTANCE;
    }

    public boolean login(String userName, String password) throws ClassNotFoundException {
        try {
            Class.forName(DRIVER_STRING);
            conn = DriverManager.getConnection(CONNECTION_STRING, "app", "app");
            boolean existUser = existUser(userName, password);
            this.userName = userName;
            this.password = password;
            if (existUser) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            return false;
        }
    }

    private ResultSet GetUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM USERS");
    }

    private boolean existUser(String userName, String password) throws SQLException {
        ResultSet rs = getInstance().GetUsers();
        while (rs.next()) {
            if (rs.getString("USERNAME").equals(userName) && rs.getString("PASSWORD").equals(password)) {
                return true;
            }
        }
        return false;
    }
}