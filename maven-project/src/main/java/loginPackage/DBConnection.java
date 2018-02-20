package loginPackage;

import model.Item;
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

    public static DBConnection getInstance() throws SQLException {
        if (INSTANCE == null) {
            INSTANCE = new DBConnection();
        }
        return INSTANCE;
    }

    public Integer getLastItemExemplarID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM EXEMPLAR");
        if (!rs.next()){
            return 1000000;
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
        //userName = "renedeicker";
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

    public void addItem(String name, String description, Integer quantity) throws SQLException {
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

    public List getItemsList() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ITEMS");
        List<Item> items = new ArrayList<Item>();
        while (rs.next()) {
            items.add(new Item(rs.getString("ITEMNAME"), rs.getString("DESCRIPTION"), rs.getInt("ITEMID" +"")));
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

    public int countExemplars(Integer id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(EXEMPLARID) FROM EXEMPLAR WHERE ITEMID = " + id + " GROUP BY ITEMID");
        if (rs.next()){
            return rs.getInt(1);
        }
        return 1;
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
        stmt.execute("DELETE FROM USERS WHERE USERNAME = '" + username + "'" );
    }

    public void upDateUser(User user, String name, String userName, String password) throws SQLException {
        Statement stmt = conn.createStatement();
        if (!(user.getUsername().getText().equals("Replace with Username") && user.getName().getText().equals("Replace with Name")
                &&user.getPassword().getText().equals("Replace with Password")) && name != null && !name.equals("") && password != null && !password.equals("") &&
                userName != null && !userName.equals("")){
            stmt.executeUpdate("UPDATE users set NAME = '" + name +"'where username = '"+ user.getUsername().getText()+"'");
            stmt.executeUpdate("UPDATE users set PASSWORD = '" + password +"'where username = '"+ user.getUsername().getText()+"'");
            stmt.executeUpdate("UPDATE users set USERNAME = '" + userName +"'where username = '"+ user.getUsername().getText()+"'");
        }
        else if(user.getUsername().getText().equals("Replace with Username") && user.getName().getText().equals("Replace with Name")
                &&user.getPassword().getText().equals("Replace with Password") && name != null && !name.equals("") && password != null && !password.equals("") &&
                userName != null && !userName.equals("")){

            String SQLCommand = "INSERT INTO USERS " +
                    "VALUES ('"+ userName + "','"+ password+ "','"+ name+"')";
            PreparedStatement ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
        }
    }
}