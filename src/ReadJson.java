/**
 * Class containing helper methods to read JSON files into a database
 */


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

public class ReadJson {

    /**
     * Helper method to parse JSON data from input URL
     * as array of custom objects
     * @param jsonURL
     * @return
     * @throws IOException
     */
    public static List<Stock> jsonToList(URL jsonURL) throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        Stock[] stockList = mapper.readValue(jsonURL, Stock[].class);

        return Arrays.asList(stockList);
    }




    /**
     * Helper method that takes in JSON data from input URL,
     * converts said data to a list of quotes, and then creates
     * SQL code to save that to a database using JDBC
     * @param jsonURL
     */
    public static void saveToDB(URL jsonURL, Statement stmt) throws IOException, SQLException {
        // read in data as array of custom object Stocks and then convert to a list
        List<Stock> stocks = ReadJson.jsonToList(jsonURL);

        Stock currStock;
        String currSymbol;
        double currPrice;
        int currVolume;
        Timestamp currDate;


        // iterate through stockList to formulate SQL queries for inserting data into database
        for (int i=0; i<stocks.size(); i++) {
            currStock = stocks.get(i);
            currSymbol = currStock.getSymbol();
            currPrice = currStock.getPrice();
            currVolume = currStock.getVolume();
            currDate = currStock.getDate();

            stmt.executeUpdate("INSERT INTO stocks_table VALUES ('"+currSymbol+"', '"+currPrice+"', '"+currVolume+"', '"+currDate+"') ");
        }

    }

}


