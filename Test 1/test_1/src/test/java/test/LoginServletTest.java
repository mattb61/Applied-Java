package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class LoginServletTest {
    @Mock private Connection conn;

    private LoginServlet servlet;

    @BeforeAll
    void init() {
        servlet = new LoginServlet();
    }

    @BeforeEach
    void setConnection(){
        try {
            Field connection = servlet.getClass().getField("connection");
            connection.set(servlet, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Mock private PreparedStatement stmt;
    @Mock private HttpServletRequest req;
    @Mock private HttpServletResponse resp;
    @Mock private ResultSet existsRs;
    @Mock private ResultSet notExistsRs;
    @Mock private PrintWriter pw;

    @Test
    public void test_doGet() {
        assertDoesNotThrow(()->{
            when(conn.prepareStatement(matches(
                "\\s*(SELECT|select|Select)\\s+"+
                "(PW_HASH|PW_Hash|pw_hash|Pw_Hash|token|Token|TOKEN),\\s*"+
                "(PW_HASH|PW_Hash|pw_hash|Pw_Hash|token|Token|TOKEN)\\s+"+
                "FROM\\s+users\\s+"+
                "WHERE\\s+(Name|NAME|name)\\s*=\\s*\\?\\s*;?\\s*"
            ))).thenReturn(stmt);
            when(req.getParameter("uname"))
                .thenReturn("Scotty", "Jimmy", "Scotty");
            when(req.getParameter("pw"))
                .thenReturn(
                    "A really, and I mean really, long password. Just an absolute unit of a password, for sure.",
                    "password123"
                );
            when(stmt.executeQuery())
                .thenReturn(existsRs, notExistsRs, existsRs);
            when(existsRs.next())
                .thenReturn(true, false);
            when(notExistsRs.next())
                .thenReturn(false);
            when(req.getContextPath())
                .thenReturn("testing");
            when(resp.getWriter())
                .thenReturn(pw);
            byte[] pwHash = BCrypt.withDefaults().hash(12, "password123".getBytes());
            when(existsRs.getBytes(anyInt()))
                .thenReturn(pwHash);
            when(existsRs.getString(anyInt()))
                .thenReturn("real_token_ong");

            servlet.doGet(req, resp);
            servlet.doGet(req, resp);
            servlet.doGet(req, resp);

            verify(pw, times(2)).print(anyString());
            verify(resp, times(2)).setStatus(422);
            verify(resp).sendRedirect("testing/chat?token=real_token_ong");
        });
    }
}
