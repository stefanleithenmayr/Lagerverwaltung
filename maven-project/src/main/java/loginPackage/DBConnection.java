package loginPackage;

import model.Product;
import model.Rent;
import model.User;

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
    private Integer productID;

    private Connection conn;

    private DBConnection() {
    }

    public static DBConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBConnection();
        }
        return INSTANCE;
    }

    private Integer getLastLendID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM LEND");
        if (!rs.next()){
            return 10000;
        }

        Integer biggestID = rs.getInt("LENDID");
        while (rs.next()){
            Integer cache = rs.getInt("LENDID");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }

    private Integer getLastItemID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");
        if (!rs.next()){
            return 100000;
        }

        Integer biggestID = rs.getInt("ITEMID");
        while (rs.next()){
            Integer cache = rs.getInt("ITEMID");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }
    public Integer getLastProductID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCTS");
        if (!rs.next()){
            return 1000;
        }

        Integer biggestID = rs.getInt("PRODUCTID");
        while (rs.next()){
            int cache = rs.getInt("PRODUCTID");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }

    public boolean login(String userName, String password) throws ClassNotFoundException, IOException, SQLException {
        //userName = "stuetz";
        //password = "12345";
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
        itemId = this.getLastItemID()+1;
        productID = this.getLastProductID()+1;
        return existUser(userName, password);
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

    public void addProduct(String name, String description) throws SQLException {
        if (!name.equals("")) {
            String SQLCommand = "INSERT INTO PRODUCTS " +
                    "VALUES (" + productID + ",'" + description + "','" + name + "'" + ")";

            PreparedStatement ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
        }
        productID++;
    }

    public void addItem(Integer itemID) throws SQLException {
        String SQLCommand = "INSERT INTO ITEMS " +
                    "VALUES ("+ itemId + ","+ itemID + ")";

        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        itemId++;
    }

    public List<Product> getProductsList() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCTS");
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            products.add(new Product(rs.getString("PRODUCTNAME"), rs.getString("DESCRIPTION"), Integer.toString(rs.getInt("PRODUCTID" +""))));
        }
        return products;
    }

    private void importSQL(InputStream in) throws SQLException {
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

    public String countItems(Integer id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(ITEMID) FROM ITEMS WHERE PRODUCTID = " + id + " GROUP BY PRODUCTID");
        if (rs.next()){
            return Integer.toString(rs.getInt(1));
        }
        return "";
    }

    public List<User> getUsers() throws SQLException {
        ResultSet users = DBConnection.getInstance().GetUsers();
        List<User> userList = new ArrayList<>();
        while (users.next()) {
            userList.add(new User(users.getString("username"), users.getString("password"), users.getString("name")));
        }
        return userList;
    }

    public void removeUser(String username) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE " +
                "FROM USERS " +
                "WHERE USERNAME = '" + username + "'" );

        Statement secStmt = conn.createStatement();
        secStmt.execute("DELETE " +
                "FROM LEND " +
                "WHERE USERNAME = '" + username + "'");
    }

    public void upDateUser(User user, String name, String userName, String password) throws SQLException {
        Statement stmt = conn.createStatement();
        if (name != null && !name.equals("") && password != null && !password.equals("") &&
                userName != null && !userName.equals("")){
            stmt.executeUpdate("UPDATE users set NAME = '" + name +"'where username = '"+ user.getUsername().getText()+"'");
            stmt.executeUpdate("UPDATE users set PASSWORD = '" + password +"'where username = '"+ user.getUsername().getText()+"'");
            stmt.executeUpdate("UPDATE users set USERNAME = '" + userName +"'where username = '"+ user.getUsername().getText()+"'");
        }
    }
    public void saveNewUser(String name, String userName, String password) throws SQLException {
        if (name != null && !name.equals("") && password != null && !password.equals("") &&
                userName != null && !userName.equals("")){
            String SQLCommand = "INSERT INTO USERS " +
                    "VALUES ('"+ userName + "','"+ password+ "','"+ name+"')";
            PreparedStatement ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
        }
    }

    public void rentItem(String name) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO LEND VALUES (" + (DBConnection.getInstance().getLastLendID() + 1) + ", '"+ userName +"'," + name +")");
        conn.commit();
    }

    /**
     * Löscht das ausgewählte Item
     * @param id
     * @throws SQLException
     */
    public void deleteItem(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM ITEMS WHERE ITEMID = " + id);
        conn.commit();
    }
    

    public List<Integer> getAvailableItems(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT i.ITEMID \n" +
                "FROM ITEMS i \n" +
                "WHERE PRODUCTID = "+ id +" AND \n" +
                "    i.ITEMID NOT IN \n" +
                "        (SELECT ITEMID \n" +
                "        FROM LEND)");

        List<Integer> ids = new ArrayList<>();
        while (rs.next()){
                ids.add(rs.getInt("ITEMID"));
        }
        return ids;
    }

    public String getAvailableItemsCount(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select count(l.itemid)\n" +
                "from items i\n" +
                "    join lend l on l.ITEMID = i.ITEMID\n" +
                "where productid =" + id);
        if (rs.next()){
             return Integer.toString(rs.getInt(1));
        }
        return "";
    }

    public List<Rent> getUserRents() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT p.productname, l.itemid, l.USERNAME, u.name\n" +
                "        FROM lend l \n" +
                "                JOIN items i ON l.ITEMID = i.ITEMID\n" +
                "                JOIN products p ON p.PRODUCTID = i.PRODUCTID\n" +
                "                JOIN users u ON l.USERNAME = u.username\n"+
                "WHERE l.USERNAME = '" + userName + "'");
        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
               rents.add(new Rent(rs.getString("PRODUCTNAME"),rs.getString("ITEMID"), rs.getString("USERNAME"), rs.getString("NAME")));
        }
        return rents;
    }

    public void removeRent(String itemID) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM LEND WHERE ITEMID = " + itemID);
    }
    public List<Rent> getAllRents() throws SQLException {
        Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.productname, l.itemid, l.USERNAME, u.name\n" +
                "        FROM lend l \n" +
                "                JOIN items i ON l.ITEMID = i.ITEMID\n" +
                "                JOIN products p ON p.PRODUCTID = i.PRODUCTID\n" +
                "                JOIN users u ON l.USERNAME = u.username");

        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
        rents.add(new Rent(rs.getString("PRODUCTNAME"),rs.getString("ITEMID"), rs.getString("username"), rs.getString("name")));
        }
        return rents;
    }
    public String getActualUser() {
        return this.userName;
    }


    public List<Rent> getRentsByUsername(String username) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from LEND " +
                "   where USERNAME = '" + username+"'");


        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
            String realName = this.getRealNameByUserName(rs.getString("USERNAME"));
            String itemName = this.getProductnameByItemID(rs.getString("ITEMID"));
            rents.add(new Rent(itemName,rs.getString("LENDID"),rs.getString("USERNAME"),realName));
        }
        return  rents;
    }

    private String getRealNameByUserName(String username) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT NAME FROM USERS WHERE USERNAME = '" + username+ "'");
        if (rs.next()){
            return rs.getString("NAME");
        }
        return  "";
    }

    private String getProductnameByItemID(String itemid) throws SQLException {
        String itemName = "";
        Statement stmt = conn.createStatement();
        ResultSet rs1 = stmt.executeQuery("SELECT PRODUCTID FROM ITEMS WHERE ITEMID = " + Integer.parseInt(itemid));
        if (rs1.next()){
            ResultSet rs2 = stmt.executeQuery("SELECT PRODUCTNAME FROM PRODUCTS WHERE PRODUCTID = " + rs1.getInt("PRODUCTID"));
            if (rs2.next()){
                itemName = rs2.getString("PRODUCTNAME");
            }
        }
        return  itemName;
    }

    public void deleteProductWithItems(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM ITEMS WHERE PRODUCTID = " + id);
        Statement secStmt = conn.createStatement();
        secStmt.execute("DELETE FROM PRODUCTS WHERE PRODUCTID = " + id);
    }
}