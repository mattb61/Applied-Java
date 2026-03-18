package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/signup/register"})
public class SignupServlet extends HttpServlet{
    public Connection connection;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Properties properties = new Properties();
        try (InputStream propsInputStream = config.getServletContext().getResourceAsStream("WEB-INF/properties.config")) {
            properties.load(propsInputStream);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        String dbServer = properties.getProperty("server");
        String dbPort = properties.getProperty("port");
        String db = properties.getProperty("db");
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(dbServer+":"+dbPort+"/"+db, properties);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(2);
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user;
        // TODO: get the string stored in the request parameters under the key "uname"
        String uName = req.getParameter("uname");
        user = uName;
        // TODO: assign the string to the user variable
        String password = req.getParameter("pw");
        byte[] pw;
        pw = password.getBytes();
        // TODO: get the string stored in the request parameters under the key "pw"
        // TODO: assign the pw variable to the result of calling getBytes() on the string above
        PrintWriter out;
        // TODO: Assign the out variable to the responses print writer
        if (pw.length > 72) {
            // TODO: print an error to the response's PrintWriter that informs the user that their password is too long
            // TODO: set the response status to 422
            return;
        }
        boolean unique = false;
        // TODO: Replace "null" with a call to create a PreparedStatment from the connection.
        //      The SQL for the PreparedStatement should be a "select" statement that will:
        //          get COUNT(*) from the "users" table, where Name equals a placement marker
        try (PreparedStatement stmt =
            null //TODO: Replace me!
        ) {
            // TODO: set the placement marker for the above select statement to the username retrieved from the request parameters
            // TODO: get the ResultSet for the above select statement by executing the Query
            // TODO: if the result set does not have a next row
                // TODO: print an error to the response's PrintWriter that informs the user that the database could not check their name
                // TODO: set the response status to 500
                // TODO: return from this method
            // set unique to true if the first column of the result set is 0; get the column by index
        } catch (SQLException e) {
            // TODO: print an error to the response's PrintWriter that informs the user that their name is not valid
            // TODO: set the response status to 422
            return;
        }
        if(!unique){
            // TODO: print an error to the response's PrintWriter that informs the user that their name is already used
            // TODO: set the response status to 422
            return;
        }
        // TODO: Replace "null" with a call to create a PreparedStatment from the connection.
        //      The SQL for the PreparedStatement should be an "insert" statement that will:
        //          insert values into the following columns of the "users" table:
        //              Name,
        //              PW_Hash,
        //              token
        //          Use Parameter Markers for the values to be inserted into "Name" and "PW_Hash"
        //          Use "SHA2(RAND(), 256)" for the value to be inserted into "token"
        try (PreparedStatement stmt =
            null // TODO: replace me!
        ) {
            byte[] hash = BCrypt.withDefaults().hash(12, pw);
            // TODO: set the Parameter Marker for "name" to the username retrieved from the request parameters
            // TODO: set the Parameter Marker for "PW_Hash" to the byte array called "hash" above
            // TODO: execute the insert statment
            // TODO: get the update count from the insert statement and store it in the integer "updates"
            int updates = 0; // TODO: replace the 0 here as directed above
            if (updates != 1) {
                // TODO: print an error to the response's PrintWriter that informs the user that the database failed to register them
                // TODO: set the response status to 500
                return;
            } else {
                // TODO: print an message to the response's PrintWriter that informs the user that the database successfully registered them
                // TODO: set the response status to 200
                return;
            }
        } catch (SQLException e) {
            // TODO: print an error to the response's PrintWriter that informs the user that the database failed to register them
            // TODO: set the response status to 500
            return;
        }
    }
}
