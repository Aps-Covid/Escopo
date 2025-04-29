package br.com.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB_Conn {
    private static final String URL = "jdbc:mysql://localhost:3306/covid_db";
    private static final String USER = "root"; 
    private static final String PASSWORD = "kaiquegamer0302";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}