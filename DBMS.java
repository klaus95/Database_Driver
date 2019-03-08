import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.Random;

public class DBMS {

    public static void main(String[] args) {
        // CSV file names to be uploaded to database
        String[] CSVFiles = {"Customer", "ShippingAddress", "CreditCard", "Member", "Product",
                                    "Invoice", "purchased", "Track", "TrackingStatus"};
        // Connect to database
        Connection db = establishDBConnection();
        // Generate all CSV files
        generateCSVFiles();
        // Insert CSV files into the database
        populateDatabase(db, CSVFiles);
        // Close database connection
        closeDBConnection(db);

    }
    private static void generateCSVFiles() {
        Costumer cus = new Costumer();
        ShippingAddress ships = new ShippingAddress(cus.getCostumerIDs());
        CreditCard cc = new CreditCard(cus.getCostumerIDs());
        Member mem = new Member(cus.getCostumerIDs());
        Product prod = new Product();
        Invoice receipt = new Invoice(mem.getID(), cc.getCC(), cus.getCostumerIDs());
        Purchased purchased = new Purchased(receipt.getPurchasedID(), prod.getProductID(), prod.getPrice(), receipt.getNumberOfMemberships());
        Track track = new Track(receipt.getPurchasedID());
        new TrackingStatus(ships.getAddress(), track.getTracking_number());
    }
    private static Connection establishDBConnection() {
        Connection conn = null;
        String myDriver = "com.mysql.cj.jdbc.Driver";
        String myUrl = "jdbc:mysql://localhost/OnlineGamingStore" +
                "?useUnicode=true&useJDBCCompliantTimezoneShift=true" +
                "&useLegacyDatetimeCode=false&serverTimezone=UTC";
        try {
            Class.forName(myDriver);
            conn = DriverManager.getConnection(myUrl, "root", "goPanthers");
        } catch (Exception e) {
            System.out.println("ERROR: Unable to connect to database!");
            System.exit(0);
        }

        System.out.println("CONNECT: Database connected successfully!");
        return conn;
    }
    private static void closeDBConnection(Connection db) {
        try {
            db.close();
        } catch (Exception e) {
            System.out.println("ERROR: Unable to close database connection!");
            System.exit(0);
        }
        System.out.println("DISCONNECT: Connection with database closed successfully!");
    }
    private static void uploadCSVFile(Connection conn, String filename) {
        String loadQuery = "LOAD DATA LOCAL INFILE '" + filename + ".csv' INTO TABLE " + filename + " FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(loadQuery);
        } catch (Exception e) {
            System.out.println("ERROR: Unable to upload " + filename + "!");
            e.printStackTrace();
            System.exit(0);
        }

        System.out.println("INSERT: " + filename + " uploaded successfully!");
    }
    private static void populateDatabase(Connection db, String[] files) {
        for (String str : files) {
            uploadCSVFile(db, str);
        }
    }
    public static String generateID(String domain, int idLength) {
        char[] id = new char[idLength];
        Random position = new Random();

        for (int i = 0; i < id.length; i++) {
            id[i] = domain.charAt(position.nextInt(domain.length() - 1));
        }
        return new String(id);
    }
    public static String userApiInterface(String query) {
        String line;

        try {

            URL url = new URL(query);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(15 * 1000);
            con.setReadTimeout(15 *1000);

            InputStream is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            line = br.readLine();

        } catch (Exception e) {
            line = null;
            System.out.println("ERROR: Unable to retrieve JSON information from User API!");
            System.exit(0);
        }
        return line;
    }
}
