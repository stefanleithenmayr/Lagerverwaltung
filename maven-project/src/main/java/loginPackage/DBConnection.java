package loginPackage;

import model.*;

import java.io.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.stream.Collectors;

public class DBConnection {
    private static DBConnection INSTANCE;
    private final String CONNECTION_STRING = "jdbc:mariadb://vm81.htl-leonding.ac.at:8114/LeoLager";
    private String userName;
    private String password;
    private Integer itemId;
    private Integer productID;
    private Integer setID;

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
        ResultSet rs = stmt.executeQuery("SELECT * FROM rent");
        if (!rs.next()) {
            return 1;
        }

        Integer biggestID = rs.getInt("RENTNR");
        while (rs.next()) {
            Integer cache = rs.getInt("RENTNR");
            if (cache > biggestID) {
                biggestID = cache;
            }
        }
        return biggestID;
    }

    private Integer getLastItemID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product");
        if (!rs.next()) {
            return 1001;
        }

        Integer biggestID = rs.getInt("PRODUCTNR");
        while (rs.next()) {
            Integer cache = rs.getInt("PRODUCTNR");
            if (cache > biggestID) {
                biggestID = cache;
            }
        }
        return biggestID;
    }

    public Integer getLastProductID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product");
        if (!rs.next()) {
            return 1001;
        }

        Integer biggestID = rs.getInt("PRODUCTNR");
        while (rs.next()) {
            int cache = rs.getInt("PRODUCTNR");
            if (cache > biggestID) {
                biggestID = cache;
            }
        }
        return biggestID;
    }

    public Integer getLastProductTypeID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM producttype");
        if (!rs.next()) {
            return 1001;
        }

        Integer biggestID = rs.getInt("PRODUCTTYPENR");
        while (rs.next()) {
            int cache = rs.getInt("PRODUCTTYPENR");
            if (cache > biggestID) {
                biggestID = cache;
            }
        }
        return biggestID;
    }

    public boolean login(String userName, String password) throws ClassNotFoundException, IOException, SQLException {
        userName = "stuetz";
        password = "12345";

        String userNameForDB = "", passwordForDB = "";

        BufferedReader br = null;
        try {
            String desktopPath = System.getProperty("user.home") + "/Desktop";
            br = new BufferedReader(new FileReader(desktopPath + "/security.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                String[] infos = line.split("=");
                if (infos[0].equals("username")) {
                    userNameForDB = infos[1];
                } else if (infos[0].equals("password")) {
                    passwordForDB = infos[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        conn = DriverManager.getConnection(CONNECTION_STRING, userNameForDB, passwordForDB);
        conn.setAutoCommit(true);
        this.userName = userName;
        this.password = password;
        itemId = this.getLastItemID() + 1;
        productID = this.getLastProductID() + 1;
        return existUser(userName, password);
    }

    private ResultSet GetUsers() throws SQLException {
        Statement stmt = conn.createStatement();
        return stmt.executeQuery("SELECT * FROM st_user");
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
            if (rs.getString("USERNAME").equals(userName)) {
                return true;
            }
        }
        return false;
    }

    public void addProduct(String name, String description) throws SQLException {
        if (!name.equals("")) {
            String SQLCommand = "INSERT INTO item " +
                    "VALUES (" + productID + ",'" + name + "','" + description + "')";

            PreparedStatement ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
        }
        productID++;
    }

    public void addProduct(Integer productID) throws SQLException {
        String SQLCommand = "INSERT INTO product " +
                "VALUES (" + getLastProductID() + "," + productID + ",null" + ",'ProductEAN', " + "null," + 2 + ")";

        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        itemId++;
    }

    public void InsertTestDatas() throws SQLException {
        String SQLCommand = "INSERT INTO producttype VALUES (1001, 'Monitor', 'LG - Monitor 400Hz')";
        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "INSERT INTO producttype VALUES (1002, 'Rechner', 'Lenovo Rechner')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "INSERT INTO producttype VALUES (1003, 'HDMI-Kabel', 'Kabel zum Bildlichen übertragen')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();


        SQLCommand = "INSERT INTO status VALUES(1,'Ausgeliehen')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "INSERT INTO status VALUES(2,'Im Lager')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        String ean = this.getEanByID(1);
        SQLCommand = "insert into product values(1001, 1001, NULL, '" + ean + "', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        ean = this.getEanByID(2);
        SQLCommand = "insert into product values(1002, 1001, NULL, '" + ean + "', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        ean = this.getEanByID(4);
        SQLCommand = "insert into product values(1004, 1003, NULL, '" + ean + "', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        ean = this.getEanByID(5);
        SQLCommand = "insert into product values(1005, 1003, NULL, '" + ean + "', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();

        //Insert a TestSet
        SQLCommand = "INSERT INTO producttype VALUES (1004, 'HDMI Adapter', 'Stecker für ein HDMI-Kabel')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "INSERT INTO producttype VALUES (1005, 'HDMI-Kabel-Adapter Set', 'HDMI Kabel mit Adapter')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        ean = this.getEanByID(3);
        SQLCommand = "insert into product values(1003, 1005, NULL, '" + ean + "', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();

        //Dummy Product/Set Header
        ean = this.getEanByID(6);
        SQLCommand = "insert into product values(1006, 1002, NULL, '" + ean + "', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        // Set Products
        //HDMI-Kabel
        ean = this.getEanByID(7);
        SQLCommand = "insert into product values(1007, 1003, NULL, '" + ean + "', 1003, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        //HDMI-Adapter
        ean = this.getEanByID(8);
        SQLCommand = "insert into product values(1008, 1004, NULL, '" + ean + "', 1003, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
    }

    private String getEanByID(int id) {
        StringBuilder ean = new StringBuilder(Integer.toString(id));
        for (int i = ean.length(); i < 11; i++) {
            ean.insert(0, "0");
        }
        return ean.toString();
    }

    public List<Product> getAllProductsList() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product");
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            products.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), "", rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
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

            if (line.toLowerCase().contains("insert")) {
                st.executeUpdate(line);
            } else {
                if (line.trim().length() > 0) {
                    st.execute(line);
                }
            }
        }
        st.close();
    }

    public String countItems(Integer id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT COUNT(ITEMNR) FROM item WHERE PRODUCTNR = " + id + " GROUP BY PRODUCTNR");
        if (rs.next()) {
            return Integer.toString(rs.getInt(1));
        }
        return "";
    }

    public List<User> getUsers() throws SQLException {
        ResultSet users = DBConnection.getInstance().GetUsers();
        List<User> userList = new ArrayList<>();
        while (users.next()) {
            userList.add(new User(users.getString("username"), users.getString("password"), users.getString("name"), users.getString("email"), users.getString("klasse"), users.getInt("userrolenr")));
        }
        return userList;
    }

    public void removeUser(String username) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE " +
                "FROM st_user " +
                "WHERE USERNAME = '" + username + "'");

        Statement secStmt = conn.createStatement();
        secStmt.execute("DELETE " +
                "FROM rent " +
                "WHERE USERNAME = '" + username + "'");
    }

    public void upDateUser(User user, String name, String userName, String password, String email, String klasse, int userrole) throws SQLException {
        Statement stmt = conn.createStatement();
        if (name != null && !name.equals("") && password != null && !password.equals("") &&
                userName != null && !userName.equals("")) {
            stmt.executeUpdate("UPDATE st_user set NAME = '" + name + "'where username = '" + user.getUsername().getText() + "'");
            stmt.executeUpdate("UPDATE st_user set PASSWORD = '" + password + "'where username = '" + user.getUsername().getText() + "'");
            stmt.executeUpdate("UPDATE st_user set USERNAME = '" + userName + "'where username = '" + user.getUsername().getText() + "'");
            stmt.executeUpdate("UPDATE st_user set email = '" + email + "'where username = '" + user.getUsername().getText() + "'");
            stmt.executeUpdate("UPDATE st_user set klasse = '" + klasse + "'where username = '" + user.getUsername().getText() + "'");
            stmt.executeUpdate("UPDATE st_user set userrolenr = " + userrole + " where username = '" + user.getUsername().getText() + "'");
        }
    }

    public void saveNewUser(String name, String userName, String password, String email, String klasse, int userrole) throws SQLException {
        if (name != null && !name.equals("") && password != null && !password.equals("") &&
                userName != null && !userName.equals("")) {
            String SQLCommand = "INSERT INTO st_user (username, password, name, email, klasse, userrolenr)" +
                    "VALUES ('" + userName + "','" + password + "','" + name + "','" + email + "','" + klasse + "','" + userrole + "')";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(SQLCommand);
        }
    }

    /*public void rentItem(String name) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("INSERT INTO ST_RENT VALUES (" + (DBConnection.getInstance().getLastLendID() + 1) + ", '"+ userName +"'," + name + ",null" + ",null" + ")");
        conn.commit();
    }*/

    /**
     * Löscht das ausgewählte Item
     *
     * @param id
     * @throws SQLException
     */
    public void deleteProduct(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM product WHERE productnr = " + id);
        conn.commit();
    }

    public List<Integer> getAvailableProducts(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT i.ProductNr FROM Product i " +
                "WHERE ProductNr = " + id + " AND" +
                "    i.ProductNr NOT IN \n" +
                "        (SELECT ProductNR \n" +
                "        FROM Item)");
        List<Integer> ids = new ArrayList<>();
        while (rs.next()) {
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
        if (rs.next()) {
            return Integer.toString(rs.getInt(1));
        }
        return "";
    }

    /*public List<Rent> getUserRents() throws SQLException {
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
    }*/

    public void removeRent(String itemID) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM ST_RENT WHERE ITEMNR = " + itemID);
    }

    public List<Rent> getAllRents() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM rent");

        List<Rent> rents = new ArrayList<>();
        while (rs.next()) {
            rents.add(new Rent(rs.getInt("RENTNR"), rs.getString("USERNAME"), rs.getString("RENTFROM"), rs.getString("RENTUNTIL")));
        }
        return rents;
    }

    public String getActualUser() {
        return this.userName;
    }


    /*public List<Rent> getRentsByUsername(String username) throws SQLException {
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
    }*/

    private String getRealNameByUserName(String username) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT NAME FROM st_user WHERE USERNAME = '" + username + "'");
        if (rs.next()) {
            return rs.getString("NAME");
        }
        return "";
    }

    private String getProductnameByItemID(String itemid) throws SQLException {
        String itemName = "";
        Statement stmt = conn.createStatement();
        ResultSet rs1 = stmt.executeQuery("SELECT PRODUCTNR FROM ST_ITEM WHERE ITEMNR = " + Integer.parseInt(itemid));
        if (rs1.next()) {
            ResultSet rs2 = stmt.executeQuery("SELECT NAME FROM ST_PRODUCT WHERE PRODUCTNR = " + rs1.getInt("PRODUCTNR"));
            if (rs2.next()) {
                itemName = rs2.getString("NAME");
            }
        }
        return itemName;
    }

    public void deleteProductWithItems(int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM ST_ITEM WHERE PRODUCTNR = " + id);
        Statement secStmt = conn.createStatement();
        secStmt.execute("DELETE FROM ST_PRODUCT WHERE PRODUCTNR = " + id);
    }

    public String getProductTypeNameByID(Integer id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM producttype ");
        while (rs.next()) {
            if (rs.getInt("PRODUCTTYPENR") == id) {
                return rs.getString("TYPENAME");
            }
        }
        return "";
    }

    public int countSets() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM st_item WHERE REFITEMNR IS NULL ");
        int count = 0;
        while (rs.next()) {
            count++;
        }
        return count;
    }

    public List<Item> getSet(int overStep) throws SQLException {
        List<Item> items = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM st_item WHERE REFITEMNR IS NULL ");
        int counter = 0;

        while (rs.next()) {
            if (overStep == counter) {
                int itemnr = rs.getInt("ITEMNR");
                items.add(new Item(rs.getInt("ITEMNR"), rs.getInt("PRODUCTNR"), rs.getString("EANCODE"), rs.getInt("REFITEMNR")));
                ResultSet rs1 = stmt.executeQuery("SELECT * FROM ST_ITEM WHERE REFITEMNR IS NOT NULL ");
                while (rs1.next()) {
                    if (rs1.getInt("REFITEMNR") == itemnr) {
                        items.add(new Item(rs1.getInt("ITEMNR"), rs1.getInt("PRODUCTNR"), rs1.getString("EANCODE"), rs1.getInt("REFITEMNR")));
                        itemnr = rs1.getInt("ITEMNR");
                        rs1 = stmt.executeQuery("SELECT * FROM ST_ITEM WHERE REFITEMNR IS NOT NULL ");
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
        if (rs.next()) {
            productID = rs.getInt("PRODUCTNR");
        }
        ResultSet rs1 = stmt.executeQuery("SELECT * FROM ST_PRODUCT WHERE PRODUCTNR = " + productID);
        if (rs1.next()) {
            return rs1.getString("NAME");
        }
        return "";
    }

    public int getAvailAbleProductsByProductType(Integer productTypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE " +
                "PRODUCTTYPENR = " + productTypeID +
                " AND  STATUS = 2");
        int counter = 0;
        while (rs.next()) {
            counter++;
        }
        return counter;
    }

    public List<ProductType> getAllProductTypes() throws SQLException {
        List<ProductType> productTypes = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM producttype");
        while (rs.next()) {
            productTypes.add(new ProductType(rs.getInt("PRODUCTTYPENR"), rs.getString("TYPENAME"), rs.getString("TYPEDESCRIPTION"), null));
        }
        return productTypes;
    }

    public List<Product> getProductsByProductTypeIdWhichAraNotInaSet(Integer productTypeID) throws SQLException {
        List<Product> products = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product where PRODUCTTYPENR = " + productTypeID + " AND SUPERPRODUCTNR is null");

        while (rs.next()) {
            products.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), "", rs.getString("PRODUCTEAN"), rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }

        for (Product p :
                products) {
            p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
            p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));
        }
        return products;
    }

    public int getTotalProductsByProductTypeID(Integer productTypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE " +
                "PRODUCTTYPENR = " + productTypeID);
        int counter = 0;
        while (rs.next()) {
            counter++;
        }
        return counter;
    }

    public int getProductTypeIdByProductID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE PRODUCTNR = " + productID);
        if (rs.next()) {
            return rs.getInt("PRODUCTTYPENR");
        }
        return -1;
    }

    public String getProductEanByProductID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE PRODUCTNR = " + productID);
        if (rs.next()) {
            return rs.getString("PRODUCTEAN");
        }
        return "";
    }

    public int getProductStatusByProductID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE PRODUCTNR = " + productID);
        if (rs.next()) {
            return rs.getInt("STATUS");
        }
        return -1;
    }

    public int getSuperProductIDByProductID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE PRODUCTNR = " + productID);
        if (rs.next()) {
            return rs.getInt("SUPERPRODUCTNR");
        }
        return -1;
    }

    public int createNewSetHeaderProductType(String setName, String description) throws SQLException {
        int id = this.getLastProductTypeID() + 1;
        String SQLCommand = "INSERT INTO producttype VALUES (" + id + ", '" + setName + "', '" + description + "')";

        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        return id;
    }

    public void addProductToSetHeader(Integer productID, int setHeader) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "UPDATE product SET SUPERPRODUCTNR = " + setHeader + " WHERE PRODUCTNR = " + productID;
        stmt.executeUpdate(sql);
    }

    public int createNewSetHeaderProduct(int productTypeID) throws SQLException {
        int productID = this.getLastProductID() + 1;
        String ean = this.getEanByID(productID);
        String SQLCommand = "INSERT INTO product VALUES (" + productID + ", " + productTypeID + ", NULL,'" + ean + "', Null, " + 2 + ")";
        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        return productID;
    }

    //Diese Methode returnt eine Liste an Products die der Head eines Sets sind
    //jedoch returnt sie nicht alle Sets Headers, da ein Set aus mehreren Sets bestehen kann
    //und diese "Untersets" nicht mitgezähl werden
    public List<Product> getHighestSetHeaders() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product WHERE SUPERPRODUCTNR IS NULL");
        List<Product> products = this.getAllProducts();
        List<Product> setsHeaders = new ArrayList<>();
        while (rs.next()) {
            for (Product product : products) {
                if (product.getSuperProductID() == rs.getInt("PRODUCTNR")) {
                    Product p = new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                            rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS"));
                    p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
                    p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));

                    if (!setsHeadersContainProduct(p, setsHeaders)) {
                        setsHeaders.add(p);
                    }
                    break;
                }
            }
        }
        return setsHeaders;
    }

    private boolean setsHeadersContainProduct(Product product, List<Product> setsHeaders) {
        for (Product setsHeader : setsHeaders) {
            if (setsHeader.getProductID().equals(product.getSuperProductID())) {
                return true;
            }
        }
        return false;
    }

    private List<Product> getAllProducts() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product");
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            products.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }
        for (Product p :
                products) {
            p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
            p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));
        }

        return products;
    }


    public List<Product> getProductsChildrenByProductID(Product parentProduct) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM product where SUPERPRODUCTNR = " + parentProduct.getProductID());
        List<Product> juniors = new ArrayList<>();

        while (rs.next()) {
            juniors.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }

        for (Product p :
                juniors) {
            p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
            p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));
        }
        return juniors;

    }

    public String getProductTypeDescriptionByID(Integer producttypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from producttype where PRODUCTTYPENR = " + producttypeID);
        if (rs.next()) {
            return rs.getString("TYPEDESCRIPTION");
        }
        return "";
    }

    public Product getProductPerProductTypeID(Integer productTypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from product where PRODUCTTYPENR = " + productTypeID);
        if (rs.next()) {
            return this.getProductByProductID(rs.getInt("PRODUCTNR"));
        }
        return null;
    }

    public Product getProductByProductID(int productnr) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from product where PRODUCTNR = " + productnr);
        Product p = null;
        if (rs.next()) {
            p = new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS"));
            p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
            p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));
        }
        return p;
    }

    public int addNewProductType(String name, String descriptionText) throws SQLException {
        int id = this.getLastProductTypeID() + 1;
        String SQLCommand = "INSERT INTO producttype VALUES (" + id + ",'" + name + "','" + descriptionText + "')";
        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        return id;
    }
    public Product  addNewProduct(int productTypeID) throws SQLException {
        int id = this.getLastProductID()+1;
        String ean = this.getEanByID(id);
        String SQLCommand = "INSERT INTO product VALUES (" + id + "," + productTypeID + ", NULL,'" + ean + "', null, " + 2 + ")";
        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        return new Product(id, productTypeID, null, ean, null, 2);
    }

    public void createRent(List<Product> products, DataPackage actualDataPackage) throws SQLException, ParseException {
        products = products.stream().distinct().collect(Collectors.toList());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = format.parse(actualDataPackage.getStartDate().toString());
        java.sql.Date sql = new java.sql.Date(parsed.getTime());

        int id = this.getLastLendID() + 1;

        PreparedStatement p = conn.prepareStatement("INSERT INTO rent VALUES(?, ?, ? ,?)");
        p.setInt(1, id);
        p.setString(2, actualDataPackage.getUser().getUsername().getText());
        p.setDate(3, sql);
        parsed = format.parse(actualDataPackage.getEndDate().toString());
        sql = new java.sql.Date(parsed.getTime());
        p.setDate(4, sql);
        p.executeUpdate();

        for (Product prod : products) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("WITH RECURSIVE ancestors AS \n" +
                    " ( SELECT productnr,superproductnr, 0 AS is_cycle FROM product\n" +
                    "   WHERE productnr=" + prod.getProductID() + " AND " + prod.getStatusID() + "= 2\n" +
                    "   UNION ALL\n" +
                    "   SELECT f.productnr,f.superproductnr, is_cycle + 1\n" +
                    "   FROM product as f\n" +
                    "   join ancestors on ancestors.productnr = f.superproductnr)\n" +
                    "SELECT * FROM ancestors");

            while (rs.next()) {
                Statement thirdStmt = conn.createStatement();
                ResultSet cacheRS = thirdStmt.executeQuery("select p.status from product p where productnr = " + rs.getInt("productnr"));
                if (cacheRS.next()) {
                    if (cacheRS.getInt("status") == 2) {
                        PreparedStatement stmt = conn.prepareStatement("INSERT INTO item VALUES(?,?)");
                        stmt.setInt(1, id);
                        stmt.setInt(2, rs.getInt("productnr"));
                        stmt.executeUpdate();

                        Statement secondSt = conn.createStatement();
                        secondSt.executeUpdate("update product set status = 1 where productnr = " + rs.getInt("productnr"));
                    }
                }
            }
        }
    }

    public boolean isProductRented(Product product) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from product where PRODUCTNR = " + product.getProductID());
        return rs.next() && rs.getInt(6) == 1;
    }

    public Product getProductByProductEanNotInASet(String eanCode) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from product where PRODUCTEAN = '" + eanCode + "' and SUPERPRODUCTNR is NULL ");
        Product p = null;
        if (rs.next()) {
            p = new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS"));

            p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
            p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));

        }
        return p;
    }

    public Product getProductPerEan(String eanCode) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from product where PRODUCTEAN = '" + eanCode + "'");
        Product p = null;
        if (rs.next()) {
            p = new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS"));

            p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
            p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));

        }
        return p;
    }

    public boolean isProductByThisRent(Product child, Rent rent) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from item where RENTNR = " + rent.getRentID() + " AND PRODUCTNR = " + child.getProductID());
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public List<Output> unrentAllProducts(Product p) throws SQLException {
        Statement stmt = conn.createStatement();
        List<Output> returnList = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("WITH RECURSIVE ancestors AS \n" +
                " ( SELECT productnr,superproductnr, 0 AS is_cycle FROM product\n" +
                "   WHERE productnr=" + p.getProductID() + "\n" +
                "   UNION ALL\n" +
                "   SELECT f.productnr,f.superproductnr, is_cycle + 1\n" +
                "   FROM product as f\n" +
                "   join ancestors on ancestors.productnr = f.superproductnr)\n" +
                "SELECT * FROM ancestors");
        while (rs.next()) {
            stmt.executeUpdate("UPDATE product SET Status = 2 WHERE PRODUCTNR = " + rs.getInt("productnr"));
            Output output = new Output(Integer.toString(rs.getInt("productnr")), DBConnection.getInstance().getProductTypeNameByProductNr(rs.getInt("productnr")),
                    DBConnection.getInstance().getRentUser(rs.getInt("productnr")), rs.getInt("is_cycle"));
            String s = DBConnection.getInstance().returnProduct(rs.getInt("productnr"));
            output.setSuccessfully(s);
            returnList.add(output);
        }
        DBConnection.getInstance().cleanRent();
        return returnList;
    }

    private void cleanRent() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT rentnr FROM rent");

        while (rs.next()) {
            ResultSet secondRS = stmt.executeQuery("select * from item where rentnr = " + rs.getInt("rentnr"));
            if (!secondRS.next()) {
                stmt.executeUpdate("delete from rent where rentnr = " + rs.getInt("rentnr"));
            }
        }
    }

    private String getProductTypeNameByProductNr(int productnr) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select typename from product p join producttype pt on pt.producttypenr = p.producttypenr where p.productnr = " + productnr);
        if (rs.next()) {
            return rs.getString("typename");
        }
        return "";
    }

    private String getRentUser(int productnr) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select username as username from item i join rent r on r.rentnr = i.rentnr where i.productnr=" + productnr);
        if (rs.next()) {
            return rs.getString("username");
        }
        return "Error";
    }

    private String returnProduct(int productnr) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("select productnr from item where productnr=" + productnr);
        if (rs.next()) {
            stmt.executeUpdate("delete from item where productnr=" + productnr);
            return "Returned";
        }
        return "Not Rented";
    }

    public void deleteAllDatas() throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("DELETE FROM item");
        stmt.executeUpdate("DELETE FROM rent");
        stmt.executeUpdate("DELETE FROM product");
        stmt.executeUpdate("DELETE FROM producttype");

    }

    public List<Product> getAllProductsByProductTypeID(Integer producttypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        List<Product> products = new ArrayList<>();
        ResultSet rs = stmt.executeQuery("SELECT * from product where producttypenr = " + producttypeID);
        while (rs.next()) {
            products.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }

        for (Product p :
                products) {
            p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
            p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));
        }
        return products;
    }

    public List<SimpleOutput> getItemsFromRent(int rentid) throws SQLException {
        Statement stmt = conn.createStatement();
        List<SimpleOutput> returnList = new ArrayList<>();

        ResultSet secondRs = stmt.executeQuery("select * from item where rentnr= " + rentid);
        while (secondRs.next()) {
            SimpleOutput output = new SimpleOutput(Integer.toString(secondRs.getInt("productnr")), DBConnection.getInstance().getProductTypeNameByProductNr(secondRs.getInt("productnr")));
            returnList.add(output);
        }

        return returnList;
    }

    public List<Rent> getCriticalRents() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM rent r WHERE DATEDIFF(CURRENT_DATE(), r.rentuntil) > 0");

        List<Rent> rents = new ArrayList<>();
        while (rs.next()) {
            rents.add(new Rent(rs.getInt("RENTNR"), rs.getString("USERNAME"), rs.getString("RENTFROM"), rs.getString("RENTUNTIL")));
        }
        return rents;
    }

    public List<Product> getallProductWhichAreNotRentet(Integer producttypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT  * from product where producttypenr = " + producttypeID + " and productnr not in(select productnr from item)");
        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            products.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), "", rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }

        for (Product p :
                products) {
            p.setProductTypeName(DBConnection.getInstance().getProductTypeNameByID(p.getProducttypeID()));
            p.setProductTypeDescription(DBConnection.getInstance().getProductTypeDescriptionByID(p.getProducttypeID()));
        }
        return products;
    }

    public List<Product> getAllChildsOfProduct(Integer productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT  * from product where  superproductnr= " + productID);

        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            products.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), "", rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }
        return products;
    }

    public List<Product> getAllSetHaders() throws SQLException {
        List<Product> headers = new ArrayList<>();
        List<Product> products = this.getAllProducts();
        for (Product product : products) {
            if (product.getSuperProductID() != null && !product.getSuperProductID().equals(0) && !IsInSetHeaders(product.getSuperProductID(), headers)) {
                headers.add(this.getProductByProductID(product.getSuperProductID()));
            }
        }
        return headers;
    }

    private boolean IsInSetHeaders(Integer productID, List<Product> headers) {
        for (Product header : headers) {
            if (header.getProductID().equals(productID)) return true;
        }
        return false;
    }

    public void setSuperProductNrNullBySuperProductNR(Integer superproductID) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("UPDATE product set superproductnr = " + null + " where superproductnr = " + superproductID);
    }

    public void deleteProductTypeByID(Integer producttypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("DELETE FROM producttype WHERE producttypenr = " + producttypeID);
        conn.commit();
    }

    public List<ProductType> getAllNotSetProductTypes() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rsProductTypes = stmt.executeQuery("SELECT * FROM producttype");
        List<ProductType> productTypes = new ArrayList<>();

        while (rsProductTypes.next()) {
            ProductType p = new ProductType(rsProductTypes.getInt("PRODUCTTYPENR"), rsProductTypes.getString("TYPENAME"), rsProductTypes.getString("TYPEDESCRIPTION"), null);
            if (IsNotASetProductType(p)) {
                productTypes.add(p);
            }
        }
        return productTypes;
    }


    private boolean IsNotASetProductType(ProductType p) throws SQLException {
        List<Product> productsWithProductType = this.getAllProductsByProductTypeID(p.getProductTypeID());
        List<Product> allProducts = this.getAllProducts();
        for (Product aProductsWithProductType : productsWithProductType) {
            for (Product allProduct : allProducts) {
                if (aProductsWithProductType.getProductID().equals(allProduct.getSuperProductID())) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean IsProductRented(Product product) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM item");
        while (rs.next()) {
            if (rs.getInt("productnr") == product.getProductID()) {
                return true;
            }
        }
        return false;
    }

    public List<ProductType> getAllSetProductTypes() throws SQLException {
        List<ProductType> allProductTypes = DBConnection.getInstance().getAllProductTypes();
        List<ProductType> notSetProductTypes = DBConnection.getInstance().getAllNotSetProductTypes();
        for (int i = 0; i < notSetProductTypes.size(); i++){
            for (int k = 0; k < allProductTypes.size(); k++){
                if (allProductTypes.get(k).getProductTypeID().equals(notSetProductTypes.get(i).getProductTypeID())){
                    allProductTypes.remove(k);
                }
            }
        }
        return  allProductTypes;
    }

    public String getUserRolleStringByID(int userrole) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM userrolle where userrollenr = " +userrole);
        if (rs.next()){
            return rs.getString("userrollename");
        }
        return  null;
    }
}