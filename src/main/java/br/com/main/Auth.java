package br.com.main;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Auth {

    public static boolean logar(String user, String password) {
        String url = DatabaseConfig.getUrlCompleta();
        String dbUser = DatabaseConfig.getUser();
        String dbPassword = DatabaseConfig.getPassword();

        String hashedPassword = hashPasswordSHA256(password); // <- aplica o hash aqui

        String sql = "SELECT * FROM auth WHERE adminuser = ? AND senha = ?";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPassword);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user);
            stmt.setString(2, hashedPassword); // <- usa o hash no lugar da senha original

            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            System.out.println("Erro ao conectar ou consultar o banco de dados:");
            e.printStackTrace();
            return false;
        }
    }

    private static String hashPasswordSHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b)); // transforma byte em hexadecimal
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao fazer hash SHA-256 da senha", e);
        }
    }
}
