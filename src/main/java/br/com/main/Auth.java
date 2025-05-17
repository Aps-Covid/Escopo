package br.com.main;

import java.sql.*;

public class Auth {

    public static boolean logar(String user, String password) {
        String url = DatabaseConfig.getUrlCompleta();
        String dbUser = DatabaseConfig.getUser();
        String dbPassword = DatabaseConfig.getPassword();

        String sql = "SELECT * FROM auth WHERE adminuser = ? AND senha = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();


            return rs.next(); // true se encontrou uma linha, false se não encontrou

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ou consultar o banco de dados:");
            e.printStackTrace();
            return false; // em caso de erro também retorna false
        }
    }
}
