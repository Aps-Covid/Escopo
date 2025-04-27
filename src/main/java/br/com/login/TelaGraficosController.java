package br.com.login;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

public class TelaGraficosController {

    @FXML
    private PieChart pieChart; // Declaração do gráfico de pizza

    @FXML
    private Label lblStatus;

    @FXML
    public void initialize() {
        // Inicializa os dados do gráfico quando a tela for carregada
        atualizarDados();
    }

    @FXML
    public void atualizarDados() {
        // Limpa os dados atuais do gráfico
        pieChart.getData().clear();

        // Adiciona novos dados ao gráfico
        pieChart.getData().add(new PieChart.Data("Item 1", 25));
        pieChart.getData().add(new PieChart.Data("Item 2", 35));
        pieChart.getData().add(new PieChart.Data("Item 3", 40));

        // Atualiza a label com status
        lblStatus.setText("Dados do gráfico atualizados.");
    }
}
