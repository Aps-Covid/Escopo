package br.com.login;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import java.sql.Connection;
import java.sql.PreparedStatement;





public class Covid_APS {

    // Substitua com seu token real
    private static final String TOKEN = "b4203dbdd693125ebf4a2381ee809de692e17d12";

    public static void main(String[] args) {
        try {
            // URL da API com dados de COVID
            String apiUrl = "https://brasil.io/api/v1/dataset/covid19/caso/data/?epidemiological_week=&date=&order_for_place=&state=SP&city_ibge_code=&place_type=&last_available_date=&is_last=True&is_repeated=False  ";
            String apiUrl2 = "https://brasil.io/api/v1/dataset/covid19/obito_cartorio/data/?epidemiological_week=&date=&order_for_place=&state=SP&city_ibge_code=&place_type=&last_available_date=&is_last=True&is_repeated=False";

            // Criar conexão HTTP
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Token " + TOKEN);

            // Ler resposta
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            // Fechar conexão
            in.close();
            conn.disconnect();

            // Exibir a resposta
            System.out.println("Resposta da API:");
            System.out.println(content.toString());
            
         // Exibir a resposta
            String json = content.toString();

            // Parsear JSON com Gson
            Gson gson = new Gson();
            CovidResponse response = gson.fromJson(json, CovidResponse.class);

            // Mostrar os dados
            for (Covid_Dados data : response.results) {
                System.out.println(data.city + " - Confirmados: " + data.confirmed + ", Mortes: " + data.deaths);

                // Inserir no banco
                try (Connection connection = DB_Conn.getConnection()) {
                    String sql = "INSERT INTO covid_data (city, state, confirmed, deaths, date) VALUES (?, ?, ?, ?, ?)";
                    PreparedStatement stmt = connection.prepareStatement(sql);
                    stmt.setString(1, data.city);
                    stmt.setString(2, data.state);
                    stmt.setInt(3, data.confirmed);
                    stmt.setInt(4, data.deaths);
                    stmt.setDate(5, java.sql.Date.valueOf(data.date));
                    stmt.executeUpdate();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}