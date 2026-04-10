package assignment3;

import assignment3.model.Car;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.util.List;
import java.util.ArrayList;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.naming.NamingException;
import javax.sql.DataSource;
import jakarta.inject.Named;

// TODO: Annotate this class with a Path annotation, and set the path to "cars"
@Path("cars")
public class App {

    // TODO: Make a function called getCars that returns a List of Cars
    @Produces("application/json")
    @Path("all")
    @GET
    public List<Car> getCars() {
        
        List<Car> cars = new ArrayList<>();
        try {
            Context ctx = new InitialContext();
            DataSource ds = (DataSource)ctx.lookup("java:/comp/env/jdbc/Cars");
            try {
                Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT make, model, year FROM cars;")
                ResultSet rs = stmt.executeQuery();
                if (rs.next() == true) {
                    Car car = new Car();
                    String make = rs.getString(1);
                    String model = rs.getString(2);
                    int year = rs.getInt(3);
                    car.setMake(make);
                    car.setModel(model);
                    car.setYear(year);
                    cars.add(car);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } catch (SQLException | NamingException e) {
            System.out.println(e.getMessage());
        }
    }

    // TODO: Annotate that functions with a Produces annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to "all"
    // TODO: Annotate that functions with a GET annotation
        // TODO: Create a new List of Cars variable and set it to a new ArrayList of Cars
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
            // TODO: Use a try-with-resources block to create and auto-close:
                // TODO: a connection from the data
                // TODO: a prepared statement from the connection that selects the make, model and year columns from the cars table
                // TODO: a result set from executing the prepared statement as a query
            // TODO: then:
                // TODO: while the result set still has next rows to process
                    // TODO: Create a new Car object
                    // TODO: set its make to the one in this row of the result set
                    // TODO: set its model to the one in this row of the result set
                    // TODO: set its year to the one in this row of the result set
                    // TODO: add the car to the list you made at the start of this method
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
        // TODO: return the list you made at the start of this function

    // TODO: Make a function called getCarByMake that returns a List of Cars
    @Produces("application/json")
    @Path("make")
    public List<Car> getCarByMake() {

    }
    // TODO: Annotate that functions with a Produces annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to "{make}"
    // TODO: Annotate that functions with a GET annotation
    // TODO: give it a string parameter for the make of the car and annotate that parameter with PathParam("make")
        // TODO: Create a new List of Cars variable and set it to a new ArrayList of Cars
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
            // TODO: Use a try-with-resources block to create and auto-close:
                // TODO: a connection from the data
                // TODO: a prepared statement from the connection that selects the model and year columns from the cars table where the make equals a parameter marker
            // TODO: then:
                // TODO: Set the parameter marker to the string parameter passed into this method
                // TODO: Use a try-with-resources block to create and auto-close:
                    // TODO: a result set from executing the prepared statement as a query
                // TODO: then
                    // TODO: while the result set still has next rows to process
                        // TODO: Create a new Car object
                        // TODO: set its make to the one passed into this method
                        // TODO: set its model to the one in this row of the result set
                        // TODO: set its year to the one in this row of the result set
                        // TODO: add the car to the list you made at the start of this method
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
        // TODO: return the list you made at the start of this function

    // TODO: Make a function called getCarByMakeAndModel that returns a List of Cars
    // TODO: Annotate that functions with a Produces annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to "{make}/{model}"
    // TODO: Annotate that functions with a GET annotation
    // TODO: give it a string parameter for the make of the car and annotate that parameter with PathParam("make")
    // TODO: give it a string parameter for the model of the car and annotate that parameter with PathParam("model")
        // TODO: Create a new List of Cars variable and set it to a new ArrayList of Cars
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
            // TODO: Use a try-with-resources block to create and auto-close:
                // TODO: a connection from the data
                // TODO: a prepared statement from the connection that selects the year column from the cars table where the make and model equal parameter markers
            // TODO: then:
                // TODO: Set the parameter marker for the make to the make string parameter passed into this method
                // TODO: Set the parameter marker for the model to the model string parameter passed into this method
                // TODO: Use a try-with-resources block to create and auto-close:
                    // TODO: a result set from executing the prepared statement as a query
                // TODO: then
                    // TODO: while the result set still has next rows to process
                        // TODO: Create a new Car object
                        // TODO: set its make to the one passed into this method
                        // TODO: set its model to the one passed into this method
                        // TODO: set its year to the one in this row of the result set
                        // TODO: add the car to the list you made at the start of this method
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
        // TODO: return the list you made at the start of this function

    // TODO: Make a function called getCarByYear that returns a List of Cars
    // TODO: Annotate that functions with a Produces annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to "yearof/{year}"
    // TODO: Annotate that functions with a GET annotation
    // TODO: give it an int parameter for the year of the car and annotate that parameter with PathParam("year")
        // TODO: Create a new List of Cars variable and set it to a new ArrayList of Cars
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
            // TODO: Use a try-with-resources block to create and auto-close:
                // TODO: a connection from the data
                // TODO: a prepared statement from the connection that selects the make and model columns from the cars table where the year equals a parameter marker
            // TODO: then:
                // TODO: Set the parameter marker to the int parameter passed into this method
                // TODO: Use a try-with-resources block to create and auto-close:
                    // TODO: a result set from executing the prepared statement as a query
                // TODO: then
                    // TODO: while the result set still has next rows to process
                        // TODO: Create a new Car object
                        // TODO: set its make to the one in this row of the result set
                        // TODO: set its model to the one in this row of the result set
                        // TODO: set its year to the one passed into this method
                        // TODO: add the car to the list you made at the start of this method
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
        // TODO: return the list you made at the start of this function

    // TODO: Make a function called postCar that returns a String
    // TODO: Annotate that functions with a Consumes annotation, and set the type to "application/json"
    // TODO: Annotate that functions with a Path annotation, and set the path to ""
    // TODO: Annotate that functions with a POST annotation
    // TODO: give it a Car parameter
        // TODO: Use a try-catch block to create a new InitialContext and lookup the DataSource for jdbc/Cars
            // TODO: Use a try-with-resources block to create and auto-close:
                // TODO: a connection from the data
                // TODO: a prepared statement from the connection that inserts into cars's make, model and year columns the values of 3 parameter markers 
            // TODO: then:
                // TODO: get the make from the car passed into this method, and set it to the parameter marker for the make column
                // TODO: get the model from the car passed into this method, and set it to the parameter marker for the model column
                // TODO: get the year from the car passed into this method, and set it to the parameter marker for the year column
                // TODO: create an int and set it to the result of calling executeUpdate on the prepared statement
                // TODO: if the int equals 1, return "Car posted successfully", otherwise return "Failed to post new Car"
        // TODO: Catch NamingExceptions and SQLExceptions
            // TODO: print the stack trace of the exception
            // TODO: return "Failed to post new Car"
}
