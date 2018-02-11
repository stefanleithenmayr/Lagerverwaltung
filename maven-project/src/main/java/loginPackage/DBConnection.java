package loginPackage;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DBConnection {
    private static DBConnection INSTANCE;

    private final String DRIVER_STRING = "org.apache.derby.jdbc.ClientDriver";
    private final String CONNECTION_STRING = "jdbc:derby://localhost:1527/db;create=true";
    private String userName;
    private String password;
    private Integer itemId;

    private Connection conn;

    private DBConnection() {
    }

    public static DBConnection getInstance() throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new DBConnection();
        }
        return INSTANCE;
    }

    private Integer getLastItemID() throws SQLException {
        Integer biggestID = 1;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");
        while (rs.next()) {
            if (rs.getInt("ITEMID") > biggestID) {
                biggestID = rs.getInt("ITEMID");
            }
        }
        return biggestID;
    }

    public boolean login(String userName, String password) throws ClassNotFoundException, IOException, SQLException {
        Class.forName(DRIVER_STRING);
        conn = DriverManager.getConnection(CONNECTION_STRING, "app", "app");
        this.userName = userName;
        this.password = password;

        try {    //test if table exist, if not than an exception occurs and the program read the sql statments from the file
            Statement stmt = conn.createStatement();
            stmt.executeQuery("SELECT * FROM USERS");
        } catch (SQLException ex) {
            File file = new File("src/main/resources/db/startScript.sql");
            this.importSQL(new FileInputStream(file));
        }
        itemId = this.getLastItemID();
        boolean existUser = existUser(userName, password);
        if (existUser) {
            return true;
        }
        return false;
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

    public void addProduct(String name, String description, Integer quantity) throws SQLException {
        /*Statement stmt = conn.createStatement();

        //String a = "INSERT INTO items VALUES (itemID.nextval,'A', 'Kabel')";
        String a = "INSERT INTO items VALUES (1,'" + name + "','" + description + "')";

        stmt.executeUpdate(a);
*//*
        int nextID_from_seq;
//   String query = "SELECT nextval('nodes_nodeid_seq')";
        String sql = "select itemID.nextval from DUAL";
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next())
             nextID_from_seq = rs.getInt(1);
        String SQLCommand = "INSERT INTO ITEMS " +
                "VALUES (5,'" + name + "','" + description + "')";
        ps = conn.prepareStatement(SQLCommand);
        //ps.setString(1, name);
        //ps.setString(2, description);
        ps.executeUpdate();

        */
        itemId++;
        if (!name.equals("")) {
            String SQLCommand = "INSERT INTO ITEMS " +
                    "VALUES (" + itemId + ",'" + description + "','" + name + "'" + ")";

            PreparedStatement ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
        }
    }

    public List getItems() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");

        List<String> items = new ArrayList<>();

        while (rs.next()) {
            items.add(rs.getString("ITEMNAME"));
        }
        return items;
    }

    public void importSQL(InputStream in) throws SQLException {
        Scanner s = new Scanner(in);
        s.useDelimiter("(;(\r)?\n)|(--\n)");
        Statement st = conn.createStatement();
        while (s.hasNext()) {
            String line = s.next();
            if (line.startsWith("/*!") && line.endsWith("*/")) {
                int i = line.indexOf(' ');
                line = line.substring(i + 1, line.length() - " */".length());
            }

            if (line.toLowerCase().contains("insert")){
                st.executeUpdate(line);
            }else{
                if (line.trim().length() > 0) {
                    st.execute(line);
                }
            }
        }
        st.close();
    }

    public int getItemIDByName(String selectedItem) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");
        while (rs.next()) {
            if (rs.getString("ITEMNAME").equals(selectedItem)) {
                return rs.getInt("ITEMID");
            }
        }
        return -1;
    }

    public String getIemDescriptionByID(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");
        while (rs.next()) {
            if (rs.getInt("ITEMID") == id) {
                return rs.getString("DESCRIPTION");
            }
        }
        return "";
    }

    public int getQuantityByID(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");
        while (rs.next()) {
            if (rs.getInt("ITEMID") == id) {
                return rs.getInt("QUANTITY");
            }
        }
        return -1;
    }
}