package br.com.main;

import java.sql.*;

public class ResetDB
{
    private static final String BASE_URL = DatabaseConfig.getUrl();
    private static final String DB_NAME = DatabaseConfig.getName();
    private static final String USER = DatabaseConfig.getUser();

    // -------------------------------- ! ALTERAR ! --------------------------------
    private static final String PASSWORD = DatabaseConfig.getPassword() ;
    // -------------------------------- ! ALTERAR ! --------------------------------

    private static Connection getRootConnection() throws SQLException
    {
        return DriverManager.getConnection(BASE_URL, USER, PASSWORD);
    }

    public static void resetDatabase()
    {
        try (Connection rootConn = getRootConnection(); Statement rootStmt = rootConn.createStatement()) {
            // Comando para dar drop no banco de dados
            String dropDB = "DROP DATABASE IF EXISTS " + DB_NAME;
            rootStmt.execute(dropDB);
            System.out.println("Banco de dados " + DB_NAME + " deletado com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao deletar o banco: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        resetDatabase();
    }
}