package br.com.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;

import java.sql.*;

public class CovidDataAnalyzer {

    private static final String DB_URL = DatabaseConfig.getUrlCompleta();
    private static final String DB_USER = DatabaseConfig.getUser();
    private static final String DB_PASSWORD = DatabaseConfig.getPassword();  // Alterar sua senha

    // ============================ GRAFICO DE PIZZA ============================
    public static ObservableList<PieChart.Data> dadosGraficoPizza() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        String sql =
                "SELECT " +
                        "  (sum(deaths_covid19)) as Covid19, " +
                        "  (sum(deaths_indeterminate_2020)) as Mortes_Desconhecidas, " +
                        "  (sum(deaths_pneumonia_2020)) as Pneumonia, " +
                        "  (sum(deaths_respiratory_failure_2020)) as Insuficiencia_Respiratoria, " +
                        "  (sum(deaths_sars_2020)) as SARS, " +
                        "  (sum(deaths_septicemia_2020)) as Septicemia " +
                        "FROM covid_typedeaths";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                pieChartData.add(new PieChart.Data("Covid-19", rs.getInt("Covid19")));
                pieChartData.add(new PieChart.Data("Desconhecida", rs.getInt("Mortes_Desconhecidas")));
                pieChartData.add(new PieChart.Data("Pneumonia", rs.getInt("Pneumonia")));
                pieChartData.add(new PieChart.Data("Insuficiência Respiratória", rs.getInt("Insuficiencia_Respiratoria")));
                pieChartData.add(new PieChart.Data("SARS", rs.getInt("SARS")));
                pieChartData.add(new PieChart.Data("Septicemia", rs.getInt("Septicemia")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return pieChartData;
    }
    // ============================ (FIM) GRAFICO DE PIZZA ============================

    // ============================ GRÁFICO DE BARRAS ============================
    public static ObservableList<XYChart.Series<String, Number>> dadosGraficoBarras() {
        ObservableList<XYChart.Series<String, Number>> barChartData = FXCollections.observableArrayList();

        String sql =
                "SELECT city, confirmed FROM covid_data " +
                        "WHERE city IS NOT NULL AND confirmed IS NOT NULL " +
                        "ORDER BY confirmed DESC LIMIT 10";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Casos Confirmados");

            while (rs.next()) {
                String cidade = rs.getString("city");
                int casosConfirmados = rs.getInt("confirmed");
                series.getData().add(new XYChart.Data<>(cidade, casosConfirmados));
            }

            barChartData.add(series);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return barChartData;
    }
    // ============================ (FIM) GRÁFICO DE BARRAS ============================

}
