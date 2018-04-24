package loginPackage;

import javafx.collections.ObservableList;
import model.Item;
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
        ResultSet rs = stmt.executeQuery("SELECT * FROM ST_RENT");
        if (!rs.next()){
            return 10000;
        }

        Integer biggestID = rs.getInt("RENTNR");
        while (rs.next()){
            Integer cache = rs.getInt("RENTNR");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }

    private Integer getLastItemID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
        if (!rs.next()){
            return 1001;
        }

        Integer biggestID = rs.getInt("ITEMNR");
        while (rs.next()){
            Integer cache = rs.getInt("ITEMNR");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }
    public Integer getLastProductID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Item");
        if (!rs.next()){
            return 1001;
        }

        Integer biggestID = rs.getInt("PRODUCTNR");
        while (rs.next()){
            int cache = rs.getInt("PRODUCTNR");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }

    public boolean login(String userName, String password) throws ClassNotFoundException, IOException, SQLException {
        userName = "stuetz";
        password = "12345";
        Class.forName(DRIVER_STRING);
        conn = DriverManager.getConnection(CONNECTION_STRING, "app", "app");
        this.userName = userName;
        this.password = password;

        try {    //test if table exist, if not than an exception occurs and the program read the sql statments from the file
            Statement stmt = conn.createStatement();
            stmt.executeQuery("SELECT * FROM ST_USER");
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
        return stmt.executeQuery("SELECT * FROM ST_USER");
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

    public boolean alreadyUser(String userName) throws SQLException {
        ResultSet rs = getInstance().GetUsers();
        while (rs.next()) {
            if(rs.getString("USERNAME").equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void addProduct(String name, String description) throws SQLException {
        if (!name.equals("")) {
            String SQLCommand = "INSERT INTO Item " +
                    "VALUES (" + productID + ",'" + name + "','" + description + "')" ;

            PreparedStatement ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
        }
        productID++;
    }

    public void addItem(Integer productID) throws SQLException {
        String SQLCommand = "INSERT INTO Product " +
                    "VALUES ("+ itemId + ","+ productID + ",'ProductEAN', "+ 1000 +")";

        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        itemId++;
    }

    public List<Product> getProductsList() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            products.add(new Product(rs.getString("NAME"), rs.getString("DESCRIPTION"), Integer.toString(rs.getInt("PRODUCTNR" +""))));
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
        ResultSet rs = stmt.executeQuery("SELECT COUNT(ITEMNR) FROM ITEM WHERE PRODUCTNR = " + id + " GROUP BY PRODUCTNR");
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
                "FROM ST_USER " +
                "WHERE USERNAME = '" + username + "'" );

        Statement secStmt = conn.createStatement();
        secStmt.execute("DELETE " +
                "FROM Rent " +
                "WHERE USERNAME = '" + username + "'");
    }

    public void upDateUser(User user, String name, String userName, String password) throws SQLException {
        Statement stmt = conn.createStatement();
        if (name != null && !name.equals("") && password != null && !password.equals("") &&
                userName != null && !userName.equals("")){
            stmt.executeUpdate("UPDATE ST_USER set NAME = '" + name +"'where username = '"+ user.getUsername().getText()+"'");
            stmt.executeUpdate("UPDATE ST_USER set PASSWORD = '" + password +"'where username = '"+ user.getUsername().getText()+"'");
            stmt.executeUpdate("UPDATE ST_USER set USERNAME = '" + userName +"'where username = '"+ user.getUsername().getText()+"'");
        }
    }
    public void saveNewUser(String name, String userName, String password) throws SQLException {
        if (name != null && !name.equals("") && password != null && !password.equals("") &&
                userName != null && !userName.equals("")){
            String SQLCommand = "INSERT INTO ST_USER " +
                    "VALUES ('"+ userName + "','"+ password+ "','"+ name+"')";
            PreparedStatement ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
        }
    }

    public void rentItem(String name) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO ST_RENT VALUES (" + (DBConnection.getInstance().getLastLendID() + 1) + ", '"+ userName +"'," + name + ",null" + ",null" + ")");
        conn.commit();
    }

    /**
     * Löscht das ausgewählte Item
     * @param id
     * @throws SQLException
     */
    public void deleteProduct(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM Product WHERE ProductNr = " + id);
        conn.commit();
    }
    

    public List<Integer> getAvailableProducts(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT i.ProductNr FROM Product i " +
                "WHERE ProductNr = "+ id +" AND" +
                "    i.ProductNr NOT IN \n" +
                "        (SELECT ProductNR \n" +
                "        FROM Item)");
        List<Integer> ids = new ArrayList<>();
        while (rs.next()){
                ids.add(rs.getInt("ProductNr"));
        }
        return ids;
    }

    public String getAvailableProductsCount(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select count(l.productnr)\n" +
                "from product i\n" +
                "    join rent l on l.productnr = i.productnr\n" +
                "where productid =" + id);
        if (rs.next()){
             return Integer.toString(rs.getInt(1));
        }
        return "";
    }

    public List<Rent> getUserRents() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT p.name, l.itemnr, l.USERNAME, u.name\n" +
                "        FROM ST_RENT l \n" +
                "                JOIN Product i ON l.productnr = i.productnr\n" +
                "                JOIN item p ON p.itemnr = i.itemnr\n" +

                "                JOIN ST_USER u ON l.USERNAME = u.username\n"+
                "WHERE l.USERNAME = '" + userName + "'");
        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
               rents.add(new Rent(rs.getString("NAME"),rs.getString("ITEMNR"), rs.getString("USERNAME"), rs.getString("NAME")));
        }
        return rents;
    }

    public void removeRent(String itemID) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM ST_RENT WHERE ITEMNR = " + itemID);
    }
    public List<Rent> getAllRents() throws SQLException {
        Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT p.productname, l.itemid, l.USERNAME, u.name\n" +
                "        FROM RENT r \n" +
                "                JOIN PRODUCT p ON r.productnr = i.ITEMNR\n" +
                "                JOIN ST_PRODUCT p ON p.PRODUCTNR = i.PRODUCTNR\n" +
                "                JOIN ST_USER u ON l.USERNAME = u.username");

        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
        rents.add(new Rent(rs.getString("NAME"),rs.getString("ITEMNR"), rs.getString("username"), rs.getString("name")));
        }
        return rents;
    }
    public String getActualUser() {
        return this.userName;
    }


    public List<Rent> getRentsByUsername(String username) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from ST_RENT " +
                "   where USERNAME = '" + username+"'");


        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
            String realName = this.getRealNameByUserName(rs.getString("USERNAME"));
            String itemName = this.getProductnameByItemID(rs.getString("ITEMNR"));
            rents.add(new Rent(itemName,rs.getString("RentNr"),rs.getString("USERNAME"),realName));
        }
        return  rents;
    }

    private String getRealNameByUserName(String username) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT NAME FROM ST_USER WHERE USERNAME = '" + username+ "'");
        if (rs.next()){
            return rs.getString("NAME");
        }
        return  "";
    }

    private String getProductnameByItemID(String itemid) throws SQLException {
        String itemName = "";
        Statement stmt = conn.createStatement();
        ResultSet rs1 = stmt.executeQuery("SELECT PRODUCTNR FROM ST_ITEM WHERE ITEMNR = " + Integer.parseInt(itemid));
        if (rs1.next()){
            ResultSet rs2 = stmt.executeQuery("SELECT NAME FROM ST_PRODUCT WHERE PRODUCTNR = " + rs1.getInt("PRODUCTNR"));
            if (rs2.next()){
                itemName = rs2.getString("NAME");
            }
        }
        return  itemName;
    }

    public void deleteProductWithItems(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM ST_ITEM WHERE PRODUCTNR = " + id);
        Statement secStmt = conn.createStatement();
        secStmt.execute("DELETE FROM ST_PRODUCT WHERE PRODUCTNR = " + id);
    }

    public void saveNewSet(String setName, String setDescription, List<Product> products) throws SQLException {
        Statement stmt = conn.createStatement();
        int prevItemId = 0, nextItemId = 0;
        for (int i = 0; i+1 < products.size(); i++){
            prevItemId = getFirstAvailableItemID(Integer.parseInt(products.get(i).getId()));
            nextItemId = getFirstAvailableItemID(Integer.parseInt(products.get(i+1).getId()));
            stmt.executeUpdate("UPDATE ST_ITEM set REFITEMNR = " + nextItemId +"where ITEMNR = "+ prevItemId);
        }
        stmt.executeUpdate("UPDATE ST_ITEM set REFITEMNR = " + null +" where ITEMNR = "+ nextItemId);
    }

    private int getFirstAvailableItemID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ST_ITEM WHERE PRODUCTNR = " + productID);
        while (rs.next()){
            if (rs.getInt("REFITEMNR") == 1000){
                return Integer.parseInt(rs.getString("ITEMNR"));
            }
        }
        return -1;
    }

    public int countSets() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ST_ITEM WHERE REFITEMNR is NULL ");
        int count = 0;
        while(rs.next()){
            count++;
        }
        return count;
    }

    public List<Item> getSet(int overStep) throws SQLException {
        List<Item> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ST_ITEM WHERE REFITEMNR is NULL ");
        int counter = 0;

        while (rs.next()){
            if (overStep == counter){
                int itemnr = rs.getInt("ITEMNR");
                items.add(new Item(rs.getInt("ITEMNR"), rs.getInt("PRODUCTNR"), rs.getString("EANCODE"), rs.getInt("REFITEMNR")));
                ResultSet rs1 = stmt.executeQuery("SELECT * FROM ST_ITEM WHERE REFITEMNR is not NULL ");
                while (rs1.next()){
                    if (rs1.getInt("REFITEMNR") == itemnr){
                        items.add(new Item(rs1.getInt("ITEMNR"), rs1.getInt("PRODUCTNR"), rs1.getString("EANCODE"), rs1.getInt("REFITEMNR")));
                        itemnr = rs1.getInt("ITEMNR");
                        rs1 = stmt.executeQuery("SELECT * FROM ST_ITEM WHERE REFITEMNR is not NULL ");
                    }
                }
                return items;
            }
            counter++;
        }
        return null;
    }

    public String getProductNameByItemID(int itemnr) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ST_ITEM WHERE ITEMNR = " + itemnr);
        if (rs.next()){
            productID = rs.getInt("PRODUCTNR");
        }
        ResultSet rs1 = stmt.executeQuery("SELECT * FROM ST_PRODUCT WHERE PRODUCTNR = " + productID);
        if (rs1.next()){
            return rs1.getString("NAME");
        }
        return  "";
    }
}