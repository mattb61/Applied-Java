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

@WebServlet("/test.css")
public class cssServlet extends HttpServlet {
    private String indexText;

    public String getIndexText() {
        return indexText;
    }

    public void init() {
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(
                    getServletContext().getResourceAsStream("/WEB-INF/test.css")
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
        response.setContentType("text/css");

        PrintWriter out = response.getWriter();

    }

    public void destroy(){

    }
}