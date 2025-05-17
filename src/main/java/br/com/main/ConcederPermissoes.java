package br.com.main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConcederPermissoes {

    private static final String DB_URL = DatabaseConfig.getUrl();

    public static void concederPermissoes(String usuario, String senha, String usuarioAlvo) {
        if (usuarioAlvo.equalsIgnoreCase("root")) {
            System.out.println("Usuário 'root' já possui todas as permissões. Nenhuma ação necessária.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, usuario, senha);
             Statement stmt = conn.createStatement()) {

            // Cria o usuário caso não exista
            String criarUsuario = "CREATE USER IF NOT EXISTS '" + usuarioAlvo + "'@'localhost' IDENTIFIED BY '1234';";

            // Concede todas as permissões para o banco chamado 'covidanalytics'
            String concederPermissoes = "GRANT ALL PRIVILEGES ON covidanalytics.* TO '" + usuarioAlvo + "'@'localhost';";

            // Aplica imediatamente as mudanças de permissão
            String flush = "FLUSH PRIVILEGES;";

            stmt.executeUpdate(criarUsuario);
            stmt.executeUpdate(concederPermissoes);
            stmt.executeUpdate(flush);

            System.out.println("Permissões concedidas com sucesso ao usuário '" + usuarioAlvo + "'.");

        } catch (SQLException e) {
            System.err.println("Erro ao conceder permissões: " + e.getMessage());
        }
    }
}
