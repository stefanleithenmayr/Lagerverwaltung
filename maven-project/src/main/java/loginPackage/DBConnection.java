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

    private final String DRIVER_STRING = "org.apache.derby.jdbc.ClientDriver";
    private final String CONNECTION_STRING = "jdbc:derby://localhost:1527/db;create=true";
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
        ResultSet rs = stmt.executeQuery("SELECT * FROM RENT");
        if (!rs.next()){
            return 1;
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

        Integer biggestID = rs.getInt("PRODUCTNR");
        while (rs.next()){
            Integer cache = rs.getInt("PRODUCTNR");
            if (cache > biggestID){
                biggestID = cache;
            }
        }
        return biggestID;
    }
    public Integer getLastProductID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
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

    public Integer getLastProductTypeID() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCTTYPE");
        if (!rs.next()){
            return 1001;
        }

        Integer biggestID = rs.getInt("PRODUCTTYPENR");
        while (rs.next()){
            int cache = rs.getInt("PRODUCTTYPENR");
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
        conn.setAutoCommit(true);
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

    public void addProduct(Integer productID) throws SQLException {
        String SQLCommand = "INSERT INTO Product " +
                    "VALUES ("+ getLastProductID() + ","+ productID +",null"+ ",'ProductEAN', "+ "null,"+ 2 +")";

        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        itemId++;
    }

    public void InsertTestDatas() throws SQLException {
        String SQLCommand = "insert into producttype values (1, 'Monitor', 'LG - Monitor 400Hz')";
        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "insert into producttype values (2, 'Rechner', 'Lenovo Rechner')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "insert into producttype values (3, 'HDMI-Kabel', 'Kabel zum Bildlichen übertragen')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();


        SQLCommand = "insert into STATUS values(1,'Ausgeliehen')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "insert into STATUS values(2,'Im Lager')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();

        SQLCommand = "insert into product values(1, 1, NULL, '1', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "insert into product values(2, 1, NULL, '2', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "insert into product values(4, 3, NULL, '4', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "insert into product values(5, 3, NULL, '5', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();

        //Insert a TestSet
        SQLCommand = "insert into producttype values (4, 'HDMI Adapter', 'Stecker für ein HDMI-Kabel')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        SQLCommand = "insert into producttype values (5, 'HDMI-Kabel-Adapter Set', 'HDMI Kabel mit Adapter')";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();

        SQLCommand = "insert into product values(3, 5, NULL, '3', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();

        //Dummy Product/Set Header
        SQLCommand = "insert into product values(6, 2, NULL, '6', NULL, 2)";
        ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        // Set Products
            //HDMI-Kabel
            SQLCommand = "insert into product values(7, 3, NULL, '7', 3, 2)";
            ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
            //HDMI-Adapter
            SQLCommand = "insert into product values(8, 4, NULL, '8', 3, 2)";
            ps = conn.prepareStatement(SQLCommand);
            ps.executeUpdate();
    }

    public List<Product> getAllProductsList() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Product");
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

    public String getProductTypeNameByID(Integer id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCTTYPE ");
        while (rs.next()){
            if (rs.getInt("PRODUCTTYPENR") == id){
                return rs.getString("TYPENAME");
            }
        }
        return  "";
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

    public int getAvailAbleProductsByProductType(Integer productTypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE " +
                                                "PRODUCTTYPENR = "+productTypeID +
                                                " AND  STATUS = 2");
        int counter = 0;
        while (rs.next()){
            counter++;
        }
        return counter;
    }

    public List<ProductType> getAllProductTypes() throws SQLException {
        List<ProductType> productTypes = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCTTYPE");
        while (rs.next()){
            productTypes.add(new ProductType(rs.getInt("PRODUCTTYPENR"), rs.getString("TYPENAME"), rs.getString("TYPEDESCRIPTION"), null));
        }
        return  productTypes;
    }

    public List<Product> getProductsByProductTypeIdWhichAraNotInaSet(Integer productTypeID) throws SQLException {
        List<Product> products = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT where PRODUCTTYPENR = "+productTypeID +" AND SUPERPRODUCTNR is null");

        while (rs.next()){
            products.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), "", rs.getString("PRODUCTEAN"), rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }
        return  products;
    }

    public int getTotalProductsByProductTypeID(Integer productTypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE " +
                "PRODUCTTYPENR = "+productTypeID );
        int counter = 0;
        while (rs.next()){
            counter++;
        }
        return  counter;
    }

    public int getProductTypeIdByProductID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE PRODUCTNR = "+productID);
        if (rs.next()){
            return rs.getInt("PRODUCTTYPENR");
        }
        return -1;
    }

    public String getProductEanByProductID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE PRODUCTNR = "+productID);
        if (rs.next()){
            return  rs.getString("PRODUCTEAN");
        }
        return "";
    }

    public int getProductStatusByProductID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE PRODUCTNR = "+productID);
        if (rs.next()){
            return rs.getInt("STATUS");
        }
        return -1;
    }

    public int getSuperProductIDByProductID(int productID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE PRODUCTNR = "+productID);
        if (rs.next()){
            return rs.getInt("SUPERPRODUCTNR");
        }
        return -1;
    }

    public int createNewSetHeaderProductType(String setName, String description) throws SQLException {
        int id = this.getLastProductTypeID()+1;
        String SQLCommand = "INSERT INTO PRODUCTTYPE VALUES ("+id +", '" + setName + "', '" + description + "')" ;

        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        return  id;
    }

    public void addProductToSetHeader(Integer productID, int setHeader) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = "UPDATE PRODUCT SET SUPERPRODUCTNR = "+setHeader +" WHERE PRODUCTNR = "+productID;
        stmt.executeUpdate(sql);
    }

    public int createNewSetHeaderProduct(int productTypeID) throws SQLException {
        int productID = this.getLastProductID()+1;
        String SQLCommand = "INSERT INTO PRODUCT VALUES ("+productID + ", " + productTypeID + ", NULL, NULL, Null, "+ 2+")" ;
        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        return  productID;
    }

    //Diese Methode returnt eine Liste an Products die der Head eines Sets sind
    //jedoch returnt sie nicht alle Sets Headers, da ein Set aus mehreren Sets bestehen kann
    //und diese "Untersets" nicht mitgezähl werden
    public List<Product> getHighestSetHeaders() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT WHERE SUPERPRODUCTNR is NULL");
        List<Product> products = this.getAllProducts();
        List<Product> setsHeaders = new ArrayList<>();
        while (rs.next()){
            for (int i = 0; i < products.size(); i++){
                if (products.get(i).getSuperProductID() == rs.getInt("PRODUCTNR")){
                    Product p = new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                            rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS"));
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
        for (int i = 0; i < setsHeaders.size(); i++){
            if (setsHeaders.get(i).getProductID() == product.getSuperProductID()){
                return true;
            }
        }
        return false;
    }

    private List<Product> getAllProducts() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");
        List<Product> products = new ArrayList<>();
        while (rs.next()){
            products.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }
        return  products;
    }



    public List<Product> getProductsChildrenByProductID(Product parentProduct) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT where SUPERPRODUCTNR = "+parentProduct.getProductID());
        List<Product> juniors = new ArrayList<>();

        while (rs.next()){
            juniors.add(new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS")));
        }
        return juniors;

    }

    public String getProductTypeDescriptionByID(Integer producttypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from PRODUCTTYPE where PRODUCTTYPENR = "+ producttypeID);
        if (rs.next()){
            return rs.getString("TYPEDESCRIPTION");
        }
        return "";
    }

    public Product getProductPerProductTypeID(Integer productTypeID) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from PRODUCT where PRODUCTTYPENR = "+ productTypeID);
        if (rs.next()){
            return  this.getProductByProductID(rs.getInt("PRODUCTNR"));
        }
        return null;
    }

    public Product getProductByProductID(int productnr) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from PRODUCT where PRODUCTNR = "+ productnr);
        if (rs.next()){
            return new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS"));
        }
        return null;
    }

    public int addNewProductType(String name, String descriptionText) throws SQLException {
        int id = this.getLastProductTypeID()+1;
        String SQLCommand = "INSERT INTO PRODUCTTYPE VALUES (" + id + ",'" + name + "','" + descriptionText + "')" ;
        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        return  id;
    }

    public int  addNewProduct(int productTypeID) throws SQLException {

        int id = this.getLastProductID()+1;
        String SQLCommand = "INSERT INTO PRODUCT VALUES (" + id + "," + productTypeID + ", NULL,'"+id+"', null, " + 2 + ")" ;
        PreparedStatement ps = conn.prepareStatement(SQLCommand);
        ps.executeUpdate();
        return id;
    }

    public void createRent(List<Product> products, DataPackage actualDataPackage) throws SQLException, ParseException {
        products = products.stream().distinct().collect(Collectors.toList());
        
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date parsed = format.parse(actualDataPackage.getStartDate().toString());
        java.sql.Date sql = new java.sql.Date(parsed.getTime());

        int id = this.getLastLendID() + 1;

        PreparedStatement p = conn.prepareStatement("insert into rent values(?, ?, ? ,?)");
        p.setInt(1, id);
        p.setString(2, actualDataPackage.getUser().getUsername().getText());
        p.setDate(3, sql);
        parsed = format.parse(actualDataPackage.getEndDate().toString());
        sql = new java.sql.Date(parsed.getTime());
        p.setDate(4,sql);
        p.executeUpdate();

        for (Product prod : products) {
            PreparedStatement stmt = conn.prepareStatement("insert into item values(?,?)");
            stmt.setInt(1, id);
            stmt.setInt(2, prod.getProductID());
            stmt.executeUpdate();

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT PRODUCTNR FROM PRODUCT WHERE SUPERPRODUCTNR="+ prod.getProductID());
            while(rs.next()){
                stmt = conn.prepareStatement("insert into item values(?,?)");
                stmt.setInt(1, id);
                stmt.setInt(2, rs.getInt(1));
                stmt.executeUpdate();
            }
        }
        this.setAllProductsToRent(true, products);
    }

    private void setAllProductsToRent(boolean rent, List<Product> products) throws SQLException {
        for (Product p: products) {
            Statement stmt = conn.createStatement();
            if (rent){
                ResultSet rs = stmt.executeQuery("SELECT PRODUCTNR FROM PRODUCT WHERE SUPERPRODUCTNR="+ p.getProductID());
                Statement updateStat = conn.createStatement();
                while(rs.next()){
                    updateStat.executeUpdate("update PRODUCT set status = 1 where PRODUCTNR = " + rs.getInt(1));
                }
                updateStat.executeUpdate("update PRODUCT set status = 1 where PRODUCTNR = " + p.getProductID());
            }else{
                ResultSet rs = stmt.executeQuery("SELECT PRODUCTNR FROM PRODUCT WHERE SUPERPRODUCTNR="+ p.getProductID());
                Statement updateStat = conn.createStatement();
                while(rs.next()){
                    updateStat.executeUpdate("update PRODUCT set status = 2 where PRODUCTNR = " + rs.getInt(1));
                }
                updateStat.executeUpdate("update PRODUCT set status = 2 where PRODUCTNR = " + p.getProductID());
            }
        }
    }

    public boolean isProductRented(Product product) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from PRODUCT where PRODUCTNR = "+ product.getProductID());
        if(rs.next()) {
            return rs.getInt(6) == 1;
        }
        return false;
    }

    public Product getProductByProductEanNotInASet(String eanCode) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * from PRODUCT where PRODUCTEAN = '"+ eanCode+"' and SUPERPRODUCTNR is NULL ");
        if (rs.next()){
            return new Product(rs.getInt("PRODUCTNR"), rs.getInt("PRODUCTTYPENR"), null, rs.getString("PRODUCTEAN"),
                    rs.getInt("SUPERPRODUCTNR"), rs.getInt("STATUS"));
        }
        return null;
    }
}