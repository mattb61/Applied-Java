package servlet_example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(value = {"/test", "/test.html"})
public class testServlet extends HttpServlet {
    private String indexText;

    public String getIndexText() {
        return indexText;
    }

    public void init() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    getServletContext().getResourceAsStream("/WEB-INF/test.html")
                )
            )
            
        ) { 
            indexText = in.lines().reduce("", String::concat);
        } catch (Exception e) {

        }
        
    }

    public void doGet(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println(getIndexText());
    }

    public void destroy(){

    }
}
