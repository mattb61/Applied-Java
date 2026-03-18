package test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class SignupServletTest {
    @Mock private Connection conn;

    private SignupServlet servlet;

    @BeforeAll
    void init() {
        servlet = new SignupServlet();
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
    @Mock private HttpServletRequest req;
    @Mock private HttpServletResponse resp;
    @Mock private PrintWriter pw;

    @Test
    public void test_doPost_pwTooLong() {
        assertDoesNotThrow(()->{
            when(req.getParameter("uname")).thenReturn("Scotty");
            when(req.getParameter("pw")).thenReturn(
                "A really, and I mean really, long password."+
                " Just an absolute unit of a password, for sure."
            );
            when(resp.getWriter()).thenReturn(pw);

            servlet.doPost(req, resp);

            verify(resp).getWriter();
            verify(pw).print(anyString());
            verify(resp).setStatus(422);
        });
    }

    @Mock private PreparedStatement selectStmt;
    @Mock private ResultSet rs;

    @Test
    public void test_doPost_nonUniqueUserName() {
        assertDoesNotThrow(()->{
            when(conn.prepareStatement(matches(
                "\\s*(SELECT|select|Select)\\s+"+
                "(COUNT|count|Count)\\(\\*\\)\\s+"+
                "FROM\\s+users\\s+"+
                "WHERE\\s+(Name|NAME|name)\\s*=\\s*\\?\\s*;?\\s*"
            ))).thenReturn(selectStmt);
            when(req.getParameter("uname")).thenReturn("Scotty");
            when(req.getParameter("pw")).thenReturn("password123");
            when(resp.getWriter()).thenReturn(pw);
            when(selectStmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true);
            when(rs.getInt(1)).thenReturn(1);

            servlet.doPost(req, resp);

            verify(resp).getWriter();
            verify(selectStmt).setString(1, "Scotty");
            verify(pw).print(anyString());
            verify(resp).setStatus(422);
        });
    }

    @Mock private PreparedStatement insertStmt;
    @Captor private ArgumentCaptor<byte[]> pw_hash_captor;

    @Test
    public void test_doPost_insertFailed() {
        assertDoesNotThrow(()->{
            when(conn.prepareStatement(matches(
                "\\s*(SELECT|select|Select)\\s+"+
                "(COUNT|count|Count)\\(\\*\\)\\s+"+
                "FROM\\s+users\\s+"+
                "WHERE\\s+(Name|NAME|name)\\s*=\\s*\\?\\s*;?\\s*"
            ))).thenReturn(selectStmt);
            when(req.getParameter("uname")).thenReturn("Scotty");
            when(req.getParameter("pw")).thenReturn("password123");
            when(resp.getWriter()).thenReturn(pw);
            when(selectStmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true);
            when(rs.getInt(1)).thenReturn(0);
            when(conn.prepareStatement(matches(
                "\\s*(INSERT|insert|Insert)\\s+"+
                "(INTO|into|Into)\\s+"+
                "users\\s*"+
                "\\(\\s*"+
                "(name|NAME|Name|PW_HASH|PW_Hash|pw_hash|Pw_Hash|token|Token|TOKEN),\\s*"+
                "(name|NAME|Name|PW_HASH|PW_Hash|pw_hash|Pw_Hash|token|Token|TOKEN),\\s*"+
                "(name|NAME|Name|PW_HASH|PW_Hash|pw_hash|Pw_Hash|token|Token|TOKEN)\\s*"+
                "\\)\\s*"+
                "VALUES\\s*"+
                "\\(\\s*"+
                "(\\?|((SHA2|sha2|Sha2)\\s*\\(\\s*(RAND|rand|Rand)\\s*\\(\\s*\\)\\s*,\\s*256\\))),\\s*"+
                "(\\?|((SHA2|sha2|Sha2)\\s*\\(\\s*(RAND|rand|Rand)\\s*\\(\\s*\\)\\s*,\\s*256\\))),\\s*"+
                "(\\?|((SHA2|sha2|Sha2)\\s*\\(\\s*(RAND|rand|Rand)\\s*\\(\\s*\\)\\s*,\\s*256\\)))\\s*"+
                "\\)\\s*;?\\s*"
            ))).thenReturn(insertStmt);
            when(insertStmt.getUpdateCount()).thenReturn(0);

            servlet.doPost(req, resp);

            verify(resp).getWriter();
            verify(selectStmt).setString(1, "Scotty");
            verify(insertStmt).setString(anyInt(), eq("Scotty"));
            verify(insertStmt).setBytes(anyInt(), pw_hash_captor.capture());
            assertTrue(BCrypt.verifyer().verify("password123".getBytes(), pw_hash_captor.getValue()).verified);
            verify(pw).print(anyString());
            verify(resp).setStatus(500);
        });
    }

    @Test
    public void test_doPost_insertSucceeded() {
        assertDoesNotThrow(()->{
            when(conn.prepareStatement(matches(
                "\\s*(SELECT|select|Select)\\s+"+
                "(COUNT|count|Count)\\(\\*\\)\\s+"+
                "FROM\\s+users\\s+"+
                "WHERE\\s+(Name|NAME|name)\\s*=\\s*\\?\\s*;?\\s*"
            ))).thenReturn(selectStmt);
            when(req.getParameter("uname")).thenReturn("Scotty");
            when(req.getParameter("pw")).thenReturn("password123");
            when(resp.getWriter()).thenReturn(pw);
            when(selectStmt.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true);
            when(rs.getInt(1)).thenReturn(0);
            when(conn.prepareStatement(matches(
                "\\s*(INSERT|insert|Insert)\\s+"+
                "(INTO|into|Into)\\s+"+
                "users\\s*"+
                "\\(\\s*"+
                "(name|NAME|Name|PW_HASH|PW_Hash|pw_hash|Pw_Hash|token|Token|TOKEN),\\s*"+
                "(name|NAME|Name|PW_HASH|PW_Hash|pw_hash|Pw_Hash|token|Token|TOKEN),\\s*"+
                "(name|NAME|Name|PW_HASH|PW_Hash|pw_hash|Pw_Hash|token|Token|TOKEN)\\s*"+
                "\\)\\s*"+
                "VALUES\\s*"+
                "\\(\\s*"+
                "(\\?|((SHA2|sha2|Sha2)\\s*\\(\\s*(RAND|rand|Rand)\\s*\\(\\s*\\)\\s*,\\s*256\\))),\\s*"+
                "(\\?|((SHA2|sha2|Sha2)\\s*\\(\\s*(RAND|rand|Rand)\\s*\\(\\s*\\)\\s*,\\s*256\\))),\\s*"+
                "(\\?|((SHA2|sha2|Sha2)\\s*\\(\\s*(RAND|rand|Rand)\\s*\\(\\s*\\)\\s*,\\s*256\\)))\\s*"+
                "\\)\\s*;?\\s*"
            ))).thenReturn(insertStmt);
            when(insertStmt.getUpdateCount()).thenReturn(1);

            servlet.doPost(req, resp);

            verify(resp).getWriter();
            verify(selectStmt).setString(1, "Scotty");
            verify(insertStmt).setString(anyInt(), eq("Scotty"));
            verify(insertStmt).setBytes(anyInt(), pw_hash_captor.capture());
            assertTrue(BCrypt.verifyer().verify("password123".getBytes(), pw_hash_captor.getValue()).verified);
            verify(pw).print(anyString());
            verify(resp).setStatus(200);
        });
    }

}
