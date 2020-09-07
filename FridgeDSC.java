import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;
import java.time.LocalDate;

import java.text.SimpleDateFormat;
import java.sql.Date;

public class FridgeDSC {

    // the date format we will be using across the application
    public static final String DATE_FORMAT = "dd/MM/yyyy";
	static final String COLUMN_ID = "id";
	static final String COLUMN_ITEMNAME = "itemName";
	static final String COLUMN_DATE = "date";
	static final String COLUMN_QUANTITY = "quantity";
	static final String COLUMN_SECTION = "section";
	static final String COLUMN_ITEM_NAME= "name";
	static final String COLUMN_ITEM_EXPIRES = "expires";
	
	
	//static final 

    /*
        FREEZER, // freezing cold
        MEAT, // MEAT cold
        COOLING, // general fridge area
        CRISPER // veg and fruits section

        note: Enums are implicitly public static final
    */
    public enum SECTION {
        FREEZER,
        MEAT,
        COOLING,
        CRISPER
    };

    private static Connection connection;
    private static Statement statement;
    private static PreparedStatement preparedStatement;

    public static void connect() throws SQLException {
        try {
				Class.forName("com.mysql.jdbc.Driver"); // You may need to comment out this line


            /* TODO 1-01 - TO COMPLETE ****************************************
             * set the value of the string for the following 3 lines:
             * - url
             * - user
             * - password
             *
             * i.e. for latcs7 with a user id of 12342344
             * 
             * String url = "jdbc:mysql://latcs7.cs.latrobe.edu.au:3306/12342344"; // the database is the user id
             * String user = "12342344";  // user is the student id
             * String password = "JKJSFTRWVGV&"; // look in your unix account for a text file with your password details
             *
             * For a local mysql installation use something like 
             * 
             * String url = "jdbc:mysql://localhost:3306/fridgedb";
             * String user = "root";
             * String password = "1234";
             *
             * 
             */
			 String url = "jdbc:mysql://localhost:3306/fridgedb";
             String user = "root";
             String password = "admin";

            connection = DriverManager.getConnection(url, user, password);
            statement = connection.createStatement();
        } catch(Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }       
    }

    public static void disconnect() throws SQLException {
		
			if(preparedStatement != null) preparedStatement.close();
			if(statement != null) statement.close();
			if(connection != null) connection.close();
		
		
    }



    public Item searchItem(String name) throws Exception {
        connect();
		
		String queryString = "SELECT * FROM item WHERE name = ?";
        
		// TODO 1-02 - TO COMPLETE ****************************************
        // preparedStatement to add argument name to the queryString
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.setString(1, name);
		ResultSet rs = preparedStatement.executeQuery();
        //resultSet to execute the preparedStatement query
		 
         //iterate through the resultSet result
          


        Item item = null;

        if (rs.next()) { // i.e. the item exists

            /* TODO 1-03 - TO COMPLETE ****************************************
             * - if resultSet has result, get data and create an Item instance
             */     
			 item = new Item(
			 rs.getString(COLUMN_ITEM_NAME),
			 rs.getBoolean(COLUMN_ITEM_EXPIRES));

        }   
		disconnect();


        return item;
    }

    public Grocery searchGrocery(int id) throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
		connect();
        String queryString = "SELECT * FROM grocery WHERE id = ?";
		


        // TODO 1-04 - TO COMPLETE ****************************************
         // preparedStatement to add argument name to the queryString
		 preparedStatement = connection.prepareStatement(queryString);
		 preparedStatement.setInt(1, id);
         // resultSet to execute the preparedStatement query
		 ResultSet rs = preparedStatement.executeQuery();
         // iterate through the resultSet result
        


        Grocery grocery = null;
		LocalDate date ;
		//Item item = null ;
		
		

        if (rs.next()) { // i.e. the grocery exists

            /* TODO 1-05 - TO COMPLETE ****************************************
             * - if resultSet has result, get data and create a Grocery instance
             * - making sure that the item name from grocery exists in 
             *   item table (use searchItem method)
             * - pay attention about parsing the date string to LocalDate
             */ 
			 
			 grocery = new Grocery(
			 rs.getInt(COLUMN_ID),
			 searchItem(rs.getString(COLUMN_ITEMNAME)),
			 LocalDate.parse(rs.getString(COLUMN_DATE),dtf),
			 rs.getInt(COLUMN_QUANTITY),
			 SECTION.valueOf(rs.getString(COLUMN_SECTION)));
				
        }

        return grocery;
    }

    public List<Item> getAllItems() throws Exception {
        connect();
		String queryString = "SELECT * FROM item";
		
		preparedStatement = connection.prepareStatement(queryString);
		ResultSet rs = preparedStatement.executeQuery();
		
        /* TODO 1-06 - TO COMPLETE ****************************************
         * - resultSet to execute the statement query
         */ 

        List<Item> items = new ArrayList<Item>();
		Item tmp;
        /* TODO 1-07 - TO COMPLETE ****************************************
         * - iterate through the resultSet result, create intance of Item
         *   and add to list items
         */ 
		 while(rs.next()){
			 tmp = new Item(
			 rs.getString(COLUMN_ITEM_NAME),
			 rs.getBoolean(COLUMN_ITEM_EXPIRES));
			 items.add(tmp);
		}

        return items;
    }

    public List<Grocery> getAllGroceries() throws Exception {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        String queryString = "SELECT * FROM grocery";
		preparedStatement = connection.prepareStatement(queryString);
		

        /* TODO 1-08 - TO COMPLETE ****************************************
         * - resultSet to execute the statement query
         */ 
		ResultSet rs = preparedStatement.executeQuery();
		
        List<Grocery> groceries = new ArrayList<Grocery>();
		Grocery tmp;
        /* TODO 1-09 - TO COMPLETE ****************************************
         * - iterate through the resultSet result, create intance of Item
         *   and add to list items
         * - making sure that the item name from each grocery exists in 
         *   item table (use searchItem method)
         * - pay attention about parsing the date string to LocalDate
         */ 
		 
		 
		 
		 while(rs.next()){
			 tmp = new Grocery(
			 rs.getInt(COLUMN_ID),
			 searchItem(rs.getString(COLUMN_ITEMNAME)),
			 LocalDate.parse(rs.getString(COLUMN_DATE),dtf),
			 rs.getInt(COLUMN_QUANTITY),
			 SECTION.valueOf(rs.getString(COLUMN_SECTION)));
			 
			 groceries.add(tmp);
		 }


        return groceries;
    }


    public int addGrocery(String name, int quantity, SECTION section) throws Exception {
        
		
		
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate date = LocalDate.now();
        String dateStr = date.format(dtf);
		
		Item tmp;
		tmp = searchItem(name);
		boolean pre =  tmp==null;
		
        // NOTE: should we check if itemName (argument name) exists in item table?
             // --> adding a groceries with a non-existing item name should through an exception
        if (pre) {
            String msg = "Item name " + name + " does not exist in Item table";
            System.out.println("\nERROR: " + msg);
            throw new Exception(msg);
            // note: throwing exception terminates this method here, returning to the calling method.
        } 	
		connect();	
		//Adding Items 
		// if(!pre)
		// {
			// Item item = new Item(name,false);
			// String query = "INSERT INTO Item VALUES(?,?)";
			// preparedStatement = connection.prepareStatement(query);
			// preparedStatement.setString(1,item.getName());
			// preparedStatement.setBoolean(2,item.canExpire());
			// preparedStatement.executeUpdate();
		// }


		
		


		

        
		//Adding grocery
		
		String command = "INSERT INTO grocery VALUES(?, ?, ?, ?, ?)";

        /* TODO 1-10 - TO COMPLETE ****************************************
         * - preparedStatement to add arguments to the queryString
         * - resultSet to executeUpdate the preparedStatement query
         */
		 //SimpleDateFormat.parse(String)

		preparedStatement = connection.prepareStatement(command);
        preparedStatement.setInt(1, 0);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, dateStr);
		preparedStatement.setInt(4, quantity);
        preparedStatement.setString(5, section.toString());
        preparedStatement.executeUpdate();
		

		// retrieving & returning last inserted record id
		ResultSet rs = statement.executeQuery("SELECT LAST_INSERT_ID()");
        rs.next();
        int newId = rs.getInt(1);
        disconnect();


        return newId;       
    }

    public Grocery useGrocery(int id) throws Exception {

        /* TODO 1-11 - TO COMPLETE ****************************************
         * - search grocery by id
         * - check if has quantity is greater one; if not throw exception
         *   with adequate error message
         */ 
		
		Grocery tmp = searchGrocery(id);
		if(tmp.getQuantity()<1)
		{
			String msg = "Quantity " + tmp.getQuantity() + " must be greater than 1";
			System.out.println("\nERROR: " + msg);
			throw new Exception(msg);
			
		}
		connect();
		
		tmp.updateQuantity();
		
        String queryString = 
            "UPDATE grocery " +
            "SET quantity = quantity - 1 " +
            "WHERE quantity > 1 " + 
            "AND id = " + id + ";";
		preparedStatement = connection.prepareStatement(queryString);
		preparedStatement.executeUpdate();

        /* TODO 1-12 - TO COMPLETE ****************************************
         * - statement execute update on queryString
         * - should the update affect a row search grocery by id and
         *   return it; else throw exception with adequate error message
         *
         * NOTE: method should return instance of grocery
         */ 
		 return tmp;
    }

    public int removeGrocery(int id) throws Exception {
        
		
        /* TODO 1-13 - TO COMPLETE ****************************************
         * - search grocery by id
         * - if grocery exists, statement execute update on queryString
         *   return the value value of that statement execute update
         * - if grocery does not exist, throw exception with adequate 
         *   error message
         *
         * NOTE: method should return int: the return value of a
         *       stetement.executeUpdate(...) on a DELETE query
         */
		Grocery tmp = searchGrocery(id);
		boolean pre  = (tmp!=null);
		if(!pre)
		{
			String msg = "Id " + tmp.getId() + " doest not exist!";
            System.out.println("\nERROR: " + msg);
            throw new Exception(msg);
		}
		connect();
		String queryString = "DELETE FROM grocery WHERE id = " + id + ";";
		preparedStatement = connection.prepareStatement(queryString);
		int returnvalue = preparedStatement.executeUpdate();
		
		
		disconnect();
		return returnvalue;
		

    }

    // // STATIC HELPERS -------------------------------------------------------

    public static long calcDaysAgo(LocalDate date) {
        return Math.abs(Duration.between(LocalDate.now().atStartOfDay(), date.atStartOfDay()).toDays());
    }

    public static String calcDaysAgoStr(LocalDate date) {
        String formattedDaysAgo;
        long diff = calcDaysAgo(date);

        if (diff == 0)
            formattedDaysAgo = "today";
        else if (diff == 1)
            formattedDaysAgo = "yesterday";
        else formattedDaysAgo = diff + " days ago"; 

        return formattedDaysAgo;            
    }


    // To perform some quick tests  
    public static void main(String[] args) throws Exception {
		try 
		{
			  FridgeDSC myFridgeDSC = new FridgeDSC();

        myFridgeDSC.connect();

        System.out.println("\nSYSTEM:\n");

        System.out.println("\n\nshowing all of each:");
        System.out.println(myFridgeDSC.getAllItems());
        System.out.println(myFridgeDSC.getAllGroceries());

        int addedId = myFridgeDSC.addGrocery("Milk", 40, SECTION.COOLING);
        System.out.println("added: " + addedId);
        System.out.println("deleting " + (addedId - 1) + ": " + (myFridgeDSC.removeGrocery(addedId - 1) > 0 ? "DONE" : "FAILED"));
        System.out.println("using " + (addedId) + ": " + myFridgeDSC.useGrocery(addedId));
        System.out.println(myFridgeDSC.searchGrocery(addedId));

        myFridgeDSC.disconnect();
			
			
			
	    // FridgeDSC myFridgeDSC = new FridgeDSC();

        // myFridgeDSC.connect();

        // System.out.println("\nSYSTEM:\n");

        // System.out.println("\n\nshowing all of each:");
        // System.out.println(myFridgeDSC.getAllItems());
        // System.out.println(myFridgeDSC.getAllGroceries());

        // //int addedId = myFridgeDSC.addGrocery("Milk", 40, SECTION.COOLING);
        // //System.out.println("added: " + addedId);
        // //System.out.println(myFridgeDSC.removeGrocery(6));
        // //System.out.println(myFridgeDSC.useGrocery(33));
        // System.out.println(myFridgeDSC.searchGrocery(19));

        //myFridgeDSC.disconnect();
			
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}			
    }
}
