package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conn {
    private Connection conn = null;

    public Conn() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.conn = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "admin");
    }

    public Connection getConnection() {
        return this.conn;
    }

    public void close() throws SQLException {
        if (this.conn != null) this.conn.close();
    }

}