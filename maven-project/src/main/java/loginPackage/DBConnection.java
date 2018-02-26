package loginPackage;

import model.Item;
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
    private Integer itemExemplarId;
    private Integer itemID;

    private Connection conn;

    private DBConnection() {
    }

    public static DBConnection getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DBConnection();
        }
        return INSTANCE;
    }

    private Integer getLastLeihID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM LEIHE");
        if (!rs.next()){
            return 10000;
        }

        Integer biggestID = rs.getInt("LEIHID");
        while (rs.next()){
            Integer cache = rs.getInt("LEIHID");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }

    private Integer getLastItemExemplarID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM EXEMPLAR");
        if (!rs.next()){
            return 100000;
        }

        Integer biggestID = rs.getInt("EXEMPLARID");
        while (rs.next()){
            Integer cache = rs.getInt("EXEMPLARID");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }
    public Integer getLastItemID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");
        if (!rs.next()){
            return 1000;
        }

        Integer biggestID = rs.getInt("ITEMID");
        while (rs.next()){
            int cache = rs.getInt("ITEMID");
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
        itemExemplarId = this.getLastItemExemplarID()+1;
        itemID = this.getLastItemID()+1;
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

    public void addItem(String name, String description) throws SQLException {
        if (!name.equals("")) {
            String SQLCommand = "INSERT INTO ITEMS " +
                    "VALUES (" + itemID + ",'" + description + "','" + name + "'" + ")";

            PreparedStatement ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
        }
        itemID++;
    }

    public void addItemExemplar(Integer itemID) throws SQLException {
        String SQLCommand = "INSERT INTO EXEMPLAR " +
                    "VALUES ("+ itemExemplarId + ","+ itemID + ")";

        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        itemExemplarId++;
    }

    public List<Item> getItemsList() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");
        List<Item> items = new ArrayList<>();
        while (rs.next()) {
            items.add(new Item(rs.getString("ITEMNAME"), rs.getString("DESCRIPTION"), Integer.toString(rs.getInt("ITEMID" +""))));
        }
        return items;
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

    public String countExemplars(Integer id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(EXEMPLARID) FROM EXEMPLAR WHERE ITEMID = " + id + " GROUP BY ITEMID");
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
                "FROM LEIHE " +
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
        stmt.execute("INSERT INTO LEIHE VALUES (" + (DBConnection.getInstance().getLastLeihID() + 1) + ", '"+ userName +"'," + name +")");
        conn.commit();
    }

    /**
     * Löscht das ausgewählte Item
     * @param id
     * @throws SQLException
     */
    public void deleteExemplar(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM EXEMPLAR WHERE EXEMPLARID = " + id);
        conn.commit();
    }
    

    public List<Integer> getAvailableExemplars(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT e.EXEMPLARID \n" +
                "FROM EXEMPLAR e \n" +
                "WHERE ITEMID = "+ id +" AND \n" +
                "    e.EXEMPLARID NOT IN \n" +
                "        (SELECT EXEMPLARID \n" +
                "        FROM LEIHE)");

        List<Integer> ids = new ArrayList<>();
        while (rs.next()){
                ids.add(rs.getInt("EXEMPLARID"));
        }
        return ids;
    }

    public String getAvailableExemplarsCount(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select count(l.exemplarid)\n" +
                "from exemplar e\n" +
                "    join leihe l on l.EXEMPLARID = e.EXEMPLARID\n" +
                "where itemid =" + id);
        if (rs.next()){
             return Integer.toString(rs.getInt(1));
        }
        return "";
    }

    public List<Rent> getUserRents() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT i.itemname, l.exemplarid, l.USERNAME, u.name\n" +
                "        FROM leihe l \n" +
                "                JOIN exemplar e ON l.EXEMPLARID = e.EXEMPLARID\n" +
                "                JOIN items i ON i.ITEMID = e.ITEMID\n" +
                "                JOIN users u ON l.USERNAME = u.username\n"+
                "WHERE l.USERNAME = '" + userName + "'");
        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
               rents.add(new Rent(rs.getString("ITEMNAME"),rs.getString("EXEMPLARID"), rs.getString("USERNAME"), rs.getString("NAME")));
        }
        return rents;
    }

    public void removeRent(String exemplarID) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM LEIHE WHERE EXEMPLARID = " + exemplarID);
    }
    public List<Rent> getAllRents() throws SQLException {
        Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT i.itemname, l.exemplarid, l.USERNAME, u.name\n" +
                "        FROM leihe l \n" +
                "                JOIN exemplar e ON l.EXEMPLARID = e.EXEMPLARID\n" +
                "                JOIN items i ON i.ITEMID = e.ITEMID\n" +
                "                JOIN users u ON l.USERNAME = u.username");

        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
        rents.add(new Rent(rs.getString("ITEMNAME"),rs.getString("EXEMPLARID"), rs.getString("username"), rs.getString("name")));
        }
        return rents;
    }
    public String getActualUser() {
        return this.userName;
    }


    public List<Rent> getRentsByUsername(String username) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from LEIHE " +
                "   where USERNAME = '" + username+"'");


        List<Rent> rents = new ArrayList<>();
        while (rs.next()){
            String realName = this.getRealNameByUserName(rs.getString("USERNAME"));
            String itemName = this.getItemnameByExemplarID(rs.getString("EXEMPLARID"));
            rents.add(new Rent(itemName,rs.getString("LEIHID"),rs.getString("USERNAME"),realName));
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

    private String getItemnameByExemplarID(String exemplarid) throws SQLException {
        String itemName = "";
        Statement stmt = conn.createStatement();
        ResultSet rs1 = stmt.executeQuery("SELECT ITEMID FROM EXEMPLAR WHERE EXEMPLARID = " + Integer.parseInt(exemplarid));
        if (rs1.next()){
            ResultSet rs2 = stmt.executeQuery("SELECT ITEMNAME FROM ITEMS WHERE ITEMID = " + rs1.getInt("ITEMID"));
            if (rs2.next()){
                itemName = rs2.getString("ITEMNAME");
            }
        }
        return  itemName;
    }

    public void deleteItemWithExemplars(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM EXEMPLAR WHERE ITEMID = " + id);
        Statement secStmt = conn.createStatement();
        secStmt.execute("DELETE FROM ITEMS WHERE ITEMID = " + id);
    }
}