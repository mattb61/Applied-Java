package servlet_example;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(name = "PracticeServlet", value = "/practice")

public class PracticeServlet extends HttpServlet {

    private String greeting;

    public String getGreeting() {
        return greeting;
    }

    public void init() {
        greeting = "Practice Servlet";
    }

    public void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html><head></head><body>");
        out.println("<h1>"+ getGreeting() +"</h1>");
        out.println("</body></html>");
    }

    public void destroy(){

    }
}
