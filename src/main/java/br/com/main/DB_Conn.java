package br.com.main;

import java.sql.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONArray;
import org.json.JSONObject;

public class DB_Conn {
    private static final String BASE_URL = "jdbc:mysql://localhost:3306/";
    private static final String DB_NAME = "covidDB";
    private static final String USER = "root";

    // -------------------------------- ! ALTERAR ! --------------------------------
    private static final String PASSWORD = "coloquesenha";
    // -------------------------------- ! ALTERAR ! --------------------------------

    private static final String API_TOKEN = "62db0dc0196657b3e5e8414d6b2f02b79e51a026";

    private static Connection getRootConnection() throws SQLException {
        return DriverManager.getConnection(BASE_URL, USER, PASSWORD);
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(BASE_URL + DB_NAME, USER, PASSWORD);
    }

    public static void initializeDatabase() {
        try (Connection rootConn = getRootConnection(); Statement rootStmt = rootConn.createStatement()) {
            String createDB = "CREATE DATABASE IF NOT EXISTS " + DB_NAME;
            rootStmt.execute(createDB);
            System.out.println("Banco de dados verificado/criado.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar o banco: " + e.getMessage());
            return;
        }

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            String createCovidData =
                    "CREATE TABLE IF NOT EXISTS covid_data (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "city VARCHAR(255), " +
                    "state VARCHAR(45), " +
                    "confirmed INT, " +
                    "deaths INT, " +
                    "estimated_population INT, " +
                    "confirmed_per_100k INT, " +
                    "date DATE" +
                    ")";

            String createTypeDeaths =
                    "CREATE TABLE IF NOT EXISTS covid_typedeaths (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "date DATE, " +
                    "deaths_covid19 INT, " +
                    "deaths_indeterminate_2020 INT, " +
                    "deaths_pneumonia_2020 INT, " +
                    "deaths_respiratory_failure_2020 INT, " +
                    "deaths_sars_2020 INT, " +
                    "deaths_septicemia_2020 INT, " +
                    "deaths_total_2020 INT, " +
                    "new_deaths_total_2020 INT" +
                    ")";

            String createAuth =
                    "CREATE TABLE IF NOT EXISTS auth (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "adminuser VARCHAR(100) NOT NULL UNIQUE, " +
                    "senha VARCHAR(255) NOT NULL" +
                    ")";

            stmt.execute(createCovidData);
            stmt.execute(createTypeDeaths);
            stmt.execute(createAuth);

            System.out.println("Tabelas verificadas/criadas.");
        } catch (SQLException e) {
            System.err.println("Erro ao criar tabelas: " + e.getMessage());
        }
    }

    public static JSONObject makeApiRequest(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Token " + API_TOKEN);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return new JSONObject(response.toString());
        } catch (Exception e) {
            System.err.println("Erro na requisição da API: " + e.getMessage());
            return null;
        }
    }

    public static void insertCovidData(JSONObject jsonResponse) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO covid_data (city, state, confirmed, deaths, estimated_population, confirmed_per_100k, date) " + "VALUES (?, ?, ?, ?, ?, ?, ?)")) {

            JSONArray results = jsonResponse.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject data = results.getJSONObject(i);
                stmt.setString(1, data.optString("city", null));
                stmt.setString(2, data.getString("state"));
                stmt.setInt(3, data.optInt("confirmed", 0));
                stmt.setInt(4, data.optInt("deaths", 0));
                stmt.setInt(5, data.optInt("estimated_population", 0));
                stmt.setInt(6, data.optInt("confirmed_per_100k_inhabitants", 0));
                stmt.setString(7, data.getString("date"));
                stmt.executeUpdate();
            }

            System.out.println("Dados inseridos na tabela covid_data.");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir dados na tabela covid_data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void insertTypeDeaths(JSONObject jsonResponse) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO covid_typedeaths (date, deaths_covid19, deaths_indeterminate_2020, deaths_pneumonia_2020, " +
                        "deaths_respiratory_failure_2020, deaths_sars_2020, deaths_septicemia_2020, deaths_total_2020, new_deaths_total_2020) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            JSONArray results = jsonResponse.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject data = results.getJSONObject(i);
                stmt.setString(1, data.getString("date"));
                stmt.setInt(2, data.optInt("deaths_covid19", 0));
                stmt.setInt(3, data.optInt("deaths_indeterminate_2020", 0));
                stmt.setInt(4, data.optInt("deaths_pneumonia_2020", 0));
                stmt.setInt(5, data.optInt("deaths_respiratory_failure_2020", 0));
                stmt.setInt(6, data.optInt("deaths_sars_2020", 0));
                stmt.setInt(7, data.optInt("deaths_septicemia_2020", 0));
                stmt.setInt(8, data.optInt("deaths_total_2020", 0));
                stmt.setInt(9, data.optInt("new_deaths_total_2020", 0));
                stmt.executeUpdate();
            }

            System.out.println("Dados inseridos na tabela covid_typedeaths.");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir dados na tabela covid_typedeaths: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void insertAdminUser() {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(
                "INSERT IGNORE INTO auth (adminuser, senha) VALUES (?, ?)")) {
            stmt.setString(1, "admin");
            stmt.setString(2, "admin123");
            stmt.executeUpdate();
            System.out.println("Usuário admin inserido.");
        } catch (SQLException e) {
            System.err.println("Erro ao inserir o usuário admin: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        initializeDatabase();

        String covidUrl = "https://brasil.io/api/v1/dataset/covid19/caso/data/?state=SP&is_last=True&is_repeated=False&page_size=10000";
        JSONObject covidResponse = makeApiRequest(covidUrl);
        if (covidResponse != null) {
            insertCovidData(covidResponse);
        }

        String deathUrl = "https://brasil.io/api/v1/dataset/covid19/obito_cartorio/data/?state=SP&is_last=True&is_repeated=False";
        JSONObject deathResponse = makeApiRequest(deathUrl);
        if (deathResponse != null) {
            insertTypeDeaths(deathResponse);
        }

        insertAdminUser();

        // Concede permissões
        ConcederPermissoes.concederPermissoes(USER, PASSWORD, USER);
    }
}