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

@WebServlet({"/login"})
public class LoginServlet extends HttpServlet{
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String user;
        // TODO: get the string stored in the request parameters under the key "uname"
        String uName = req.getParameter("uname");
        user = uName;
        // TODO: assign the string to the user variable
        String password = req.getParameter("pw");
        // TODO: get the string stored in the request parameters under the key "pw"
        byte[] pw;
        pw = password.getBytes();
        // TODO: assign the pw variable to the result of calling getBytes() on the string above

        PrintWriter out = resp.getWriter();
        // TODO: Assign the out variable to the responses print writer
        if (pw.length > 72) {
            // TODO: print an error to the response's PrintWriter that informs the user that their password is too long
            // TODO: set the response status to 422
            return;
        }
        // TODO: Replace "null" with a call to create a PreparedStatment from the connection.
        //      The SQL for the PreparedStatement should be a "select" statement that will:
        //          get "PW_Hash" and "token" from the "users" table, where Name equals a placement marker
        try (PreparedStatement stmt =
            connection.prepareStatement("SELECT FROM (users) VALUES (PW_Hash, token) WHERE (Name) IS (?)"); // TODO: replace me!
        ) {
            // TODO: set the placement marker for the above select statement to the username retrieved from the request parameters
            
            // TODO: get the ResultSet for the above select statement by executing the Query
            // TODO: if the result set does not have a next row
                // TODO: print an error to the response's PrintWriter that informs the user that their username was invalid
                // TODO: set the response status to 422
                // TODO: return from this method
            byte[] stored_hash;
            // TODO: get the bytes from the "PW_Hash" column by index and assign it to stored_hash
            String token;
            // TODO: get the string from the "token" column by index and assign it to token
            BCrypt.Result result = BCrypt.verifyer().verify(pw, stored_hash);
            if(result.verified){
                resp.sendRedirect(req.getContextPath()+"/chat?token="+token);
            }
        } catch (SQLException e) {
            // TODO: print an error to the response's PrintWriter that informs the user that their username was invalid
            // TODO: set the response status to 422
            return;
        }
    }
}
