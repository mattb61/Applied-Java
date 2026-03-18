package test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ChatServletTest {
    @Mock private Connection conn;
    Timestamp now;

    private ChatServlet servlet;

    @BeforeAll
    void init(){
        servlet = new ChatServlet();
    }

    // Chat
    @Mock private PreparedStatement chatStmt;
    @Mock private ResultSet chatRs;

    // Post
    @Mock PreparedStatement doPostGetUserByTokenStmt;
    @Mock PreparedStatement doPostInsertMsgStmt;
    @Mock ResultSet doPostGetUserByTokenRs;

    @BeforeEach
    void setConnection(){
        now = new Timestamp(Instant.now().getEpochSecond());
        try {
            Field connection = servlet.getClass().getField("connection");
            connection.set(servlet, conn);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void test_getChat() {
        assertDoesNotThrow(() -> {
            when(conn.prepareStatement(
                matches(
                    "\\s*(SELECT|select|Select)\\s+"+
                    "((u(sers)?\\.(Name|name|NAME)|"+
                    "m(essages)?\\.(Sent_at|sent_at|SENT_AT)|"+
                    "m(essages)?\\.Message),\\s*){2}"+
                    "(u(sers)?\\.(Name|name|NAME)|"+
                    "m(essages)?\\.(Sent_at|sent_at|SENT_AT)|"+
                    "m(essages)?\\.Message)\\s+"+
                    "FROM\\s+messages\\s+"+
                    "(m\\s+)?INNER\\s+JOIN\\s+"+
                    "users\\s+(u\\s+)?"+
                    "ON\\s+u(sers)?\\.(ID|Id|id)\\s*"+
                    "=\\s*m(essages)?\\.(Sender_ID|sender_id|Sender_Id|SENDER_ID)\\s*;?\\s*"))
            ).thenReturn(chatStmt);
            when(chatStmt.executeQuery()).thenReturn(chatRs);
            when(chatRs.next())
                .thenReturn(true, true, false);
            when(chatRs.getString(anyInt()))
                .thenReturn("Scotty", "hey", "Jimmy", "hello");
            when(chatRs.getTimestamp(anyInt()))
                .thenReturn(now);
            final String chatLog = servlet.getChat();
            System.out.println(chatLog);
            assertTrue(() -> {
                return chatLog.matches("(.*?(Scotty|Jimmy|hey|hello|"+now.toString()+").*?\n?){6}.*");
            });
            verify(chatRs, times(4)).getString(anyInt());
            verify(chatRs, times(2)).getTimestamp(anyInt());
        });
    }

    @Mock HttpServletRequest doGetReq;
    @Mock HttpServletResponse doGetResp;
    @Mock PrintWriter doGetRespPw;

    @Test
    void test_doGet() {
        assertDoesNotThrow(() -> {
            // Chat
            when(conn.prepareStatement(
                matches(
                    "\\s*(SELECT|select|Select)\\s+"+
                    "((u(sers)?\\.(Name|name|NAME)|"+
                    "m(essages)?\\.(Sent_at|sent_at|SENT_AT)|"+
                    "m(essages)?\\.Message),\\s*){2}"+
                    "(u(sers)?\\.(Name|name|NAME)|"+
                    "m(essages)?\\.(Sent_at|sent_at|SENT_AT)|"+
                    "m(essages)?\\.Message)\\s+"+
                    "FROM\\s+messages\\s+"+
                    "(m\\s+)?INNER\\s+JOIN\\s+"+
                    "users\\s+(u\\s+)?"+
                    "ON\\s+u(sers)?\\.(ID|Id|id)\\s*"+
                    "=\\s*m(essages)?\\.(Sender_ID|sender_id|Sender_Id|SENDER_ID)\\s*;?\\s*"))
            ).thenReturn(chatStmt);
            when(chatStmt.executeQuery()).thenReturn(chatRs);
            when(chatRs.next())
                .thenReturn(true, true, false);
            when(chatRs.getString(anyInt()))
                .thenReturn("Scotty", "hey", "Jimmy", "hello");
            when(chatRs.getTimestamp(anyInt()))
                .thenReturn(now);

            // Get
            when(doGetReq.getParameter("token"))
                .thenReturn("token_value");
            when(doGetResp.getWriter()).thenReturn(doGetRespPw);

            servlet.doGet(doGetReq, doGetResp);

            verify(doGetReq).getParameter("token");
            verify(doGetRespPw).println(anyString());
        });
    }

    @Mock HttpServletRequest doPostReq;
    @Mock HttpServletResponse doPostResp;
    @Mock PrintWriter doPostRespPw;

    @Test
    void test_doPost(){
        assertDoesNotThrow(() -> {
            // Chat
            when(conn.prepareStatement(
                matches(
                    "\\s*(SELECT|select|Select)\\s+"+
                    "((u(sers)?\\.(Name|name|NAME)|"+
                    "m(essages)?\\.(Sent_at|sent_at|SENT_AT)|"+
                    "m(essages)?\\.Message),\\s*){2}"+
                    "(u(sers)?\\.(Name|name|NAME)|"+
                    "m(essages)?\\.(Sent_at|sent_at|SENT_AT)|"+
                    "m(essages)?\\.Message)\\s+"+
                    "FROM\\s+messages\\s+"+
                    "(m\\s+)?INNER\\s+JOIN\\s+"+
                    "users\\s+(u\\s+)?"+
                    "ON\\s+u(sers)?\\.(ID|Id|id)\\s*"+
                    "=\\s*m(essages)?\\.(Sender_ID|sender_id|Sender_Id|SENDER_ID)\\s*;?\\s*"))
            ).thenReturn(chatStmt);
            when(chatStmt.executeQuery()).thenReturn(chatRs);
            when(chatRs.next())
                .thenReturn(true, true, false);
            when(chatRs.getString(anyInt()))
                .thenReturn("Scotty", "hey", "Jimmy", "hello");
            when(chatRs.getTimestamp(anyInt()))
                .thenReturn(now);

            // Post
            when(conn.prepareStatement(
                matches(
                    "\\s*(SELECT|select|Select)\\s+"+
                    "(ID|Id|id)\\s+"+
                    "FROM\\s+users\\s+"+
                    "WHERE\\s+(token|Token|TOKEN)\\s*=\\s*\\?\\s*;?\\s*"))
            ).thenReturn(doPostGetUserByTokenStmt);
            when(doPostGetUserByTokenStmt.executeQuery())
                .thenReturn(doPostGetUserByTokenRs);
            when(doPostGetUserByTokenRs.next())
                .thenReturn(true, false);
            when(doPostGetUserByTokenRs.getInt(anyInt()))
                .thenReturn(42);
            when(conn.prepareStatement(
                matches(
                    "\\s*(INSERT|insert|Insert)\\s+(INTO|into|Into)\\s+"+
                    "messages\\s*"+
                    "\\(\\s*"+
                    "(((SENDER_ID|Sender_ID|Sender_Id|sender_id)|(Sent_at|Sent_At|SENT_AT|sent_at)|(Message|message|MESSAGE)),\\s*){2}"+
                    "((SENDER_ID|Sender_ID|Sender_Id|sender_id)|(Sent_at|Sent_At|SENT_AT|sent_at)|(Message|message|MESSAGE))\\s*"+
                    "\\)\\s*"+
                    "(VALUES|values|Values)\\s*"+
                    "\\(\\?,\\s*\\?,\\s*\\?\\s*\\)\\s*;?\\s*"))
            ).thenReturn(doPostInsertMsgStmt);
            when(doPostInsertMsgStmt.execute())
                .thenReturn(false);
            when(doPostReq.getParameter("token"))
                .thenReturn("token_value");
            when(doPostReq.getParameter("message"))
                .thenReturn("some message");
            when(doPostResp.getWriter()).thenReturn(doPostRespPw);

            servlet.doPost(doPostReq, doPostResp);

            verify(doPostReq, times(2)).getParameter("token");
            verify(doPostReq).getParameter("message");
            verify(doPostRespPw).println(anyString());
            verify(doPostGetUserByTokenStmt).setString(1, "token_value");
            verify(doPostGetUserByTokenStmt).executeQuery();
            verify(doPostGetUserByTokenRs).next();
            verify(doPostGetUserByTokenRs).getInt(1);
            verify(doPostInsertMsgStmt).setInt(anyInt(), eq(42));
            verify(doPostInsertMsgStmt).setTimestamp(anyInt(), any());
            verify(doPostInsertMsgStmt).setString(anyInt(), eq("some message"));
            verify(doPostInsertMsgStmt).execute();
        });
    }

    @AfterAll
    void destroy() {
        servlet.destroy();
    }
}
