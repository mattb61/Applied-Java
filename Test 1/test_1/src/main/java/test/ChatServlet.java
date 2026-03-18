
package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Properties;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet({"/chat"})
public class ChatServlet extends HttpServlet {
    public Connection connection;
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        Properties properties = new Properties();
        try (InputStream propsInputStream = config.getServletContext().getResourceAsStream("WEB-INF/properties.config")) {
            properties.load(propsInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String dbServer = properties.getProperty("server");
        String dbPort = properties.getProperty("port");
        String db = properties.getProperty("db");
        try{
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(dbServer+":"+dbPort+"/"+db, properties);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
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

    private String getHTML(String token, String chatLog){
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n");
        sb.append("<html lang=\"en\">\n");
        sb.append("<head>\n");
        sb.append("    <meta charset=\"UTF-8\">\n");
        sb.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("    <title>Document</title>\n");
        sb.append("</head>\n");
        sb.append("<body>\n");
        sb.append("    <form action=\"/test_1/chat\" method=\"post\">\n");
        sb.append("        <label for=\"message\">Message: </label><input type=\"textarea\" name=\"message\" id=\"message\"><br/>\n");
        sb.append("        <input type=\"hidden\" name=\"token\" value=\"");
        sb.append(token);
        sb.append("\">\n");
        sb.append("        <button type=\"submit\">Send Message</button>\n");
        sb.append("    </form>\n");
        sb.append("    <div>");
        sb.append(chatLog);
        sb.append("    </div>");
        sb.append("</body>");
        sb.append("</html>");
        return "";
    }

    public String getChat() throws SQLException{
        String chatLog = "";
        try (PreparedStatement stmt =
            connection.prepareStatement("SELECT u.Name, m.Sent_at, m.Message FROM messages m INNER JOIN users u ON u.ID = m.Sender_ID")
        ) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String uname = rs.getString(1);
                Timestamp sentAt = rs.getTimestamp(2);
                String message = rs.getString(3);
                StringBuilder sb = new StringBuilder();
                sb.append("    <p>");
                sb.append(uname);
                sb.append("@");
                sb.append(sentAt.toString());
                sb.append("</p>\n");
                sb.append("    <p>");
                sb.append(message);
                sb.append("</p>\n");
                chatLog = chatLog.concat(sb.toString());
            }
        }
        return chatLog;
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String token = req.getParameter("token");
        try{
            String chatLog = getChat();
            resp.getWriter().println(getHTML(token, chatLog));
        } catch (SQLException e) {
            PrintWriter out = resp.getWriter();
            out.println("failed to get chat log from DB.");
            out.println(e.getMessage());
            resp.setStatus(500);
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String message = req.getParameter("message");
        String token = req.getParameter("token");
        int id;
        PrintWriter out = resp.getWriter();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT ID FROM users WHERE token=?")) {
            stmt.setString(1, token);
            ResultSet rs = stmt.executeQuery();
            if(!rs.next()){
                out.println("No user found for given login token.");
                resp.setStatus(422);
                System.err.println("No user found for given login token.");
                return;
            }
            id = rs.getInt(1);
        } catch (Exception e) {
            out.println("failed to get user information from login token.");
            out.println(e.getMessage());
            resp.setStatus(422);
            e.printStackTrace();
            return;
        }
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO messages (Sender_ID, Sent_at, Message) VALUES (?, ?, ?)")) {
            Timestamp timestamp = Timestamp.from(Instant.now());
            stmt.setInt(1, id);
            stmt.setTimestamp(2, timestamp);
            stmt.setString(3, message);
            stmt.execute();
        } catch (Exception e) {
            out.println("failed to get user information from login token.");
            out.println(e.getMessage());
            resp.setStatus(422);
            e.printStackTrace();
            return;
        }
        doGet(req, resp);
    }
}
