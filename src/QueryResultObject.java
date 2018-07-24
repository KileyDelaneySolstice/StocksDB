/**
 * Object to represent summarized data from SQL queries
 */


public class QueryResultObject {

    private String date;

    private String symbol;

    private double maxPrice;

    private double minPrice;

    private int totalVol;

    private double closingPrice;



    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }


    public String getSymbol() { return symbol; }

    public void setSymbol(String symbol) { this.symbol = symbol; }


    public double getMaxPrice() { return maxPrice; }

    public void setMaxPrice(double price) { this.maxPrice = price; }


    public double getMinPrice() { return minPrice; }

    public void setMinPrice(double price) { this.minPrice = price; }


    public int getTotalVol() { return totalVol; }

    public void setTotalVol(int vol) { this.totalVol = vol; }


    public double getClosingPrice() { return closingPrice; }

    public void setClosingPrice(double closingPrice) { this.closingPrice = closingPrice; }



    public String toString() {
        return ("The highest price of stock " + symbol + " on " + date + " is $" + maxPrice + "\n" +
                "The lowest price of stock " + symbol + " on " + date + " is $" + minPrice + "\n" +
                "The total volume of stock " + symbol + " traded on " + date + " is " + totalVol + " units \n" +
                "The closing price of stock " + symbol + " on " + date + "is $" + closingPrice);
    }

    public String maxToString() {
        return ("The highest price of stock " + symbol + " on " + date + " is $" + maxPrice);
    }

    public String minToString() {
        return ("The lowest price of stock " + symbol + " on " + date + " is $" + minPrice);
    }

    public String volToString() {
        return ("The total volume of stock " + symbol + " traded on " + date + " is " + totalVol + " units");
    }

    public String closingToString() {
        return ("The closing price of stock " + symbol + " on " + date + " is $" + closingPrice);
    }

}