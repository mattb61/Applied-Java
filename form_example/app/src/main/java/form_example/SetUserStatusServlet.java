package form_example;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/set_user_status")
public class SetUserStatusServlet extends HttpServlet{

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext context = config.getServletContext();
        Data[] users = {
            new Data("scotty", Status.OFFLINE),
            new Data("jimmy", Status.OFFLINE),
            new Data("chucky", Status.OFFLINE),
        };
        context.setAttribute("users", users);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uname = req.getParameter("uname");
        Data[] users = (Data[])getServletContext().getAttribute("users");
        int uid = -1;
        for (int i =0; i<users.length; i++) {
            Data data = users[i];
            if(data.uname.equals(uname)){
                uid = i;
                break;
            }
        }
        if (uid != -1) {
            String statusString = req.getParameter("status");
            Status status = Status.valueOf(statusString);
            System.out.println(status);
            users[uid] = users[uid].changeStatus(status);
        }
        getServletContext().setAttribute("users", users);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
    
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
