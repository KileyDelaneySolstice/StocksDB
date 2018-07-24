import java.net.URL;
import java.sql.*;
import java.util.Date;
import java.util.Scanner;

public class Main {

    // private variables to save information needed for establishing a connection
    private static final String USERNAME = "dbuser";
    private static final String PASSWORD = "dbpassword";
    private static final String CONN_STRING =
            "jdbc:hsqldb:data/stocks";


    public static void main(String[] args) throws Exception {

        // initialize tools for HSQL connection and user inputs
        Connection conn = null;
        Statement stmt = null;
        Scanner sc = new Scanner(System.in);
        URL jsonURL = new URL("https://bootcamp-training-files.cfapps.io/week1/week1-stocks.json");

        try {

            // establish HSQL connection
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);


            // code to create SQL table from data, commented out so as to avoid conflicts
            stmt.executeUpdate("CREATE TABLE stocks_table (symbol VARCHAR(50), price DECIMAL(10,2), volume DECIMAL, date TIMESTAMP)");

            ReadJson.saveToDB(jsonURL, stmt);
            stmt.executeUpdate("ALTER TABLE stocks_table ADD COLUMN date_only date");
            stmt.executeUpdate("UPDATE stocks_table SET date_only = cast(date as date)");


            // prompt user to input date and save in a variable for future use
            System.out.println("Please enter a date in format YYYY-MM-DD: \n");
            String dateString = sc.nextLine();
            java.sql.Date date = java.sql.Date.valueOf(dateString);


            System.out.println("Please enter a stock symbol: \n");
            String symbol = sc.nextLine();


            // retrieve data and save resulting QueryResultObject to a variable
            QueryResultObject qro = aggregateData(conn, stmt, date, symbol);

            // prompt user to select desired information, which is then printed to the console from the QueryResultObject
            System.out.println("What would you like to know? A: Highest price of a stock for given date. B: Lowest price of a stock for given date. C: Total volume traded of a stock for a given date. " +
                    "D: Closing price of a stock for given date. E: All of the above. \n");
            String choice = sc.nextLine();
            printData(qro, choice);


        } catch (Exception e) {
            System.err.println(e);

        } finally {
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }



    /**
     * Method to get a daily aggregated view of the following data:
     * Highest price of a stock for a given date,
     * lowest price of a stock for a given date,
     * total volume traded of a stock for a given date
     * @param conn
     * @param stmt
     * @param date
     * @return
     */
    public static QueryResultObject aggregateData(Connection conn, Statement stmt, Date date, String symbol) throws SQLException {
        // call SQL queries and save ResultSet Strings to a QueryResultObject
        String SQLmax = "SELECT price FROM stocks_table WHERE price = (SELECT MAX(price) FROM stocks_table WHERE date_only = '"+date+"' AND symbol = '"+symbol+"')";
        String SQLmin = "SELECT price FROM stocks_table WHERE price = (SELECT MIN(price) FROM stocks_table WHERE date_only = '"+date+"' AND symbol = '"+symbol+"')";
        String SQLct = "SELECT COUNT(volume) FROM stocks_table WHERE date_only = '"+date+"' AND symbol = '"+symbol+"'";
        String SQLcl = "SELECT price FROM stocks_table WHERE date = (SELECT MAX(date) FROM stocks_table WHERE date_only = '"+date+"' AND symbol = '"+symbol+"')";


        QueryResultObject qro = new QueryResultObject();
        qro.setDate(date.toString());
        qro.setSymbol(symbol);


        ResultSet rsMax = stmt.executeQuery(SQLmax);
        while (rsMax.next()) {
            String maxPri = rsMax.getString(1);
            qro.setMaxPrice(Double.parseDouble(maxPri));
        }

        ResultSet rsMin = stmt.executeQuery(SQLmin);
        while (rsMin.next()) {
            String minPri = rsMin.getString(1);
            qro.setMinPrice(Double.parseDouble(minPri));
        }

        ResultSet rsCt = stmt.executeQuery(SQLct);
        while (rsCt.next()) {
            String vol = rsCt.getString(1);
            qro.setTotalVol(Integer.parseInt(vol));
        }

        ResultSet rsCl = stmt.executeQuery(SQLcl);
        while (rsCl.next()) {
            String clPr = rsCl.getString(1);
            qro.setClosingPrice(Double.parseDouble(clPr));
        }


        return qro;
    }


    /**
     * Method to print resulting data from QRO
     */

    public static void printData(QueryResultObject qro, String choice) {
        switch (choice) {
            case "A": System.out.println(qro.maxToString());
                break;

            case "B": System.out.println(qro.minToString());
                break;

            case "C": System.out.println(qro.volToString());
                break;

            case "D": System.out.println(qro.closingToString());
                break;

            case "E": System.out.println(qro.toString());
                break;
        }
    }
}


