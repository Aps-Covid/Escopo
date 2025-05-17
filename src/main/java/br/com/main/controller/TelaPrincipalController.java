package br.com.main.controller;

import br.com.main.CovidDataAnalyzer;
import br.com.main.DB_Conn;
import br.com.main.TabelaRank;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class TelaPrincipalController implements Initializable {
    // ============================ | INICIALIZA | ============================
        @Override public void initialize(URL location, ResourceBundle resources) {
            try {
                configurarAnimacaoBotaoFILTRO();
                configurarAnimacaoBotaoEXPORT();

                configurarAnimacaoBotaoAPLICAR();
                    btnAplicar.setOnAction(e -> aplicarFiltros());
                configurarAnimacaoBotaoLIMPAR();
                    btnLimpar.setOnAction(e -> limparFiltros());

                preencherColunas();
                inicializarGraficos();

                // PREENCHE A COMBOBOX DE TIPOS DE MORTE NO FILTRO
                comboTipoMorte.setItems(FXCollections.observableArrayList(
                    "Covid-19",
                    "Desconhecida",
                    "Pneumonia",
                    "Insuf. Respiratória",
                    "SARS",
                    "Septicemia"
                ));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    // ============================ | (FIM) INICIALIZA | ============================

    // ============================ VARIÁVEIS ============================
        // GRÁFICOS
            @FXML private VBox vboxPizza;
            @FXML private BarChart<String, Number> graficoBarras;
            @FXML private PieChart graficoPizza;
        // TABELA DE RANK
            @FXML private TableView<TabelaRank> tabelaRank;
            // COLUNAS DA TABELA
                @FXML private TableColumn<TabelaRank, Integer> colunaRank;
                @FXML private TableColumn<TabelaRank, String> colunaCidade;
                @FXML private TableColumn<TabelaRank, Integer> colunaCasos;
        // BOTÕES
            @FXML private Button btnAplicar;
            @FXML private Button btnLimpar;
            @FXML private Button btnFiltro;
            @FXML private Button btnExportar;
        // LISTA DE DADOS ORIGINAIS
            private ObservableList<TabelaRank> dadosOriginais;
        // SELEÇÃO DE FILTRO
            @FXML private VBox boxFiltro;
            @FXML private ComboBox<String> comboCidade;
            @FXML private ComboBox<String> comboTipoMorte;
        // ARMAZENANDO O TOTAL
            private int totalConfirmadosGeral = 0;
    // ============================ (FIM) VARIÁVEIS ============================

    // ============================ INTERFACE ============================
        // <btnFiltro> ANIMAÇÃO DO BOTÃO DE EXPORTAR
            @FXML private void configurarAnimacaoBotaoEXPORT() {
                DropShadow sombra = new DropShadow();
                sombra.setRadius(20.0);
                sombra.setOffsetX(0);
                sombra.setOffsetY(4);
                sombra.setColor(Color.rgb(33, 115, 70, 0.0)); // Sombra começa invisível

                btnExportar.setEffect(sombra);

                ScaleTransition aumenta = new ScaleTransition(Duration.seconds(0.1), btnExportar);
                aumenta.setToX(1.05);
                aumenta.setToY(1.05);

                ScaleTransition diminui = new ScaleTransition(Duration.seconds(0.1), btnExportar);
                diminui.setToX(1.0);
                diminui.setToY(1.0);

                ScaleTransition click = new ScaleTransition(Duration.seconds(0.1), btnExportar);
                click.setToX(1.0);
                click.setToY(1.0);

                ScaleTransition disclick = new ScaleTransition(Duration.seconds(0.1), btnExportar);
                disclick.setToX(1.05);
                disclick.setToY(1.05);

                Timeline acendeSombra =
                        new Timeline(
                                new KeyFrame(Duration.seconds(0.2), // DURAÇÃO
                                        new KeyValue(sombra.colorProperty(), // DEFINE A SOMBRA
                                                Color.rgb(33, 115, 70, 0.4) // COR DA SOMBRA
                                        )));

                Timeline apagaSombra =
                        new Timeline(
                                new KeyFrame(Duration.seconds(0.2),
                                        new KeyValue(sombra.colorProperty(),
                                                Color.rgb(33, 115, 70, 0.0)
                                        )));

                btnExportar.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                    acendeSombra.playFromStart();
                    aumenta.playFromStart();
                });

                btnExportar.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                    apagaSombra.playFromStart();
                    diminui.playFromStart();
                });

                btnExportar.setOnMousePressed(e -> {
                    click.playFromStart();
                });

                btnExportar.setOnMouseReleased(e -> {
                    disclick.playFromStart();
                    sombra.setColor(Color.rgb(33, 115, 70, 0.4));
                });
            }
                @FXML private void exportarBI() {
                    try {
                        String urlPowerBI = "https://app.powerbi.com/"; // ALTERAR
                        java.awt.Desktop.getDesktop().browse(new java.net.URI(urlPowerBI));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

    // <btnFiltro> ANIMAÇÃO DO BOTÃO DE FILTRO
            @FXML private void configurarAnimacaoBotaoFILTRO() {
                DropShadow sombra = new DropShadow();
                sombra.setRadius(20.0);
                sombra.setOffsetX(0);
                sombra.setOffsetY(4);
                sombra.setColor(Color.rgb(242, 69, 53, 0.0)); // Sombra começa invisível

                btnFiltro.setEffect(sombra);

                ScaleTransition aumenta = new ScaleTransition(Duration.seconds(0.1), btnFiltro);
                aumenta.setToX(1.05);
                aumenta.setToY(1.05);

                ScaleTransition diminui = new ScaleTransition(Duration.seconds(0.1), btnFiltro);
                diminui.setToX(1.0);
                diminui.setToY(1.0);

                ScaleTransition click = new ScaleTransition(Duration.seconds(0.1), btnFiltro);
                click.setToX(1.0);
                click.setToY(1.0);

                ScaleTransition disclick = new ScaleTransition(Duration.seconds(0.1), btnFiltro);
                disclick.setToX(1.05);
                disclick.setToY(1.05);

                Timeline acendeSombra =
                        new Timeline(
                        new KeyFrame(Duration.seconds(0.2), // DURAÇÃO
                        new KeyValue(sombra.colorProperty(), // DEFINE A SOMBRA
                        Color.rgb(242, 69, 53, 0.4) // COR DA SOMBRA
                        )));

                Timeline apagaSombra =
                        new Timeline(
                        new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(sombra.colorProperty(),
                        Color.rgb(242, 69, 53, 0.0)
                        )));

                btnFiltro.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                    acendeSombra.playFromStart();
                    aumenta.playFromStart();
                });

                btnFiltro.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                    apagaSombra.playFromStart();
                    diminui.playFromStart();
                });

                btnFiltro.setOnMousePressed(e -> {
                    click.playFromStart();
                });

                btnFiltro.setOnMouseReleased(e -> {
                    disclick.playFromStart();
                    sombra.setColor(Color.rgb(242, 69, 53, 0.4));
                });
            }
                @FXML private void apareceFiltro() {
                    if (boxFiltro.isVisible()) {
                        // Fade-out + slide para cima ao esconder
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(300), boxFiltro);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);

                        TranslateTransition slideUp = new TranslateTransition(Duration.millis(300), boxFiltro);
                        slideUp.setFromY(0);
                        slideUp.setToY(-10);

                        fadeOut.setOnFinished(e -> boxFiltro.setVisible(false));
                        slideUp.play();
                        fadeOut.play();
                    } else {
                        // Tornar visível e animar entrada
                        boxFiltro.setVisible(true);

                        boxFiltro.setOpacity(0.0);
                        boxFiltro.setTranslateY(-10);

                        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), boxFiltro);
                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);

                        TranslateTransition slideDown = new TranslateTransition(Duration.millis(300), boxFiltro);
                        slideDown.setFromY(-10);
                        slideDown.setToY(0);

                        slideDown.play();
                        fadeIn.play();
                    }
                }

    // <btnAplicar> ANIMAÇÃO DO BOTÃO DE APLICAR
            @FXML private void configurarAnimacaoBotaoAPLICAR() {
                ScaleTransition aumenta = new ScaleTransition(Duration.seconds(0.1), btnAplicar);
                aumenta.setToX(1.05);
                aumenta.setToY(1.05);

                ScaleTransition diminui = new ScaleTransition(Duration.seconds(0.1), btnAplicar);
                diminui.setToX(1.0);
                diminui.setToY(1.0);

                ScaleTransition click = new ScaleTransition(Duration.seconds(0.1), btnAplicar);
                click.setToX(1.0);
                click.setToY(1.0);

                ScaleTransition disclick = new ScaleTransition(Duration.seconds(0.1), btnAplicar);
                disclick.setToX(1.05);
                disclick.setToY(1.05);

                btnAplicar.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                    aumenta.playFromStart();
                });

                btnAplicar.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                    diminui.playFromStart();
                });

                btnAplicar.setOnMousePressed(e -> {
                    click.playFromStart();
                });

                btnAplicar.setOnMouseReleased(e -> {
                    disclick.playFromStart();
                });
            }
                private void aplicarFiltros() {
                    String cidadeSel = comboCidade.getValue();
                    ObservableList<TabelaRank> filtrada =
                            (cidadeSel == null || cidadeSel
                                    .isEmpty())
                                    ? dadosOriginais
                                    : dadosOriginais.filtered(t -> t.getCidade().equalsIgnoreCase(cidadeSel));
                    tabelaRank.setItems(filtrada);

                    NumberAxis eixoY = (NumberAxis) graficoBarras.getYAxis();
                    if (cidadeSel == null || cidadeSel.isEmpty()) {
                        eixoY.setAutoRanging(false);
                        eixoY.setLowerBound(0);
                        eixoY.setUpperBound(totalConfirmadosGeral);
                        eixoY.setTickUnit(totalConfirmadosGeral / 10.0);
                    } else {
                        eixoY.setAutoRanging(false);
                        int casosCidade = filtrada.get(0).getCasosConfirmados();
                        double proporcao = (double) casosCidade / totalConfirmadosGeral;
                        double fatorAmplificacao = Math.max(1.5, Math.min(50.0, 0.15 / proporcao));
                        double limiteY = casosCidade * fatorAmplificacao;
                        eixoY.setLowerBound(0);
                        eixoY.setUpperBound(Math.max(10, limiteY));
                        eixoY.setTickUnit(Math.max(1, limiteY / 5.0));
                    }

                    // 3. série do gráfico
                    if (cidadeSel == null || cidadeSel.isEmpty()) {
                        graficoBarras.setData(CovidDataAnalyzer.dadosGraficoBarras());
                    } else {
                        int casosCidade = filtrada.get(0).getCasosConfirmados();
                        XYChart.Series<String, Number> serie = new XYChart.Series<>();
                        serie.setName("Casos Confirmados");

                        serie.getData().add(new XYChart.Data<>(cidadeSel, casosCidade));

                        // Adiciona espaços vazios
                        serie.getData().add(new XYChart.Data<>(" ", 0));
                        serie.getData().add(new XYChart.Data<>("  ", 0));
                        serie.getData().add(new XYChart.Data<>("   ", 0));

                        // Adiciona a barra real
                        serie.getData().add(new XYChart.Data<>(cidadeSel, casosCidade));

                        // Mais espaços depois
                        serie.getData().add(new XYChart.Data<>("    ", 0));
                        serie.getData().add(new XYChart.Data<>("     ", 0));

                        graficoBarras.setData(FXCollections.observableArrayList(serie));
                    }

                    /* ---------- pizza (só tipo de morte) ---------- */
                    String tipoSel = comboTipoMorte.getValue();

                    if (tipoSel == null || tipoSel.isEmpty()) {
                        graficoPizza.setData(CovidDataAnalyzer.dadosGraficoPizza());
                    } else {
                        ObservableList<PieChart.Data> all = CovidDataAnalyzer.dadosGraficoPizza();
                        double totalMortes = all.stream().mapToDouble(PieChart.Data::getPieValue).sum();
                        double sel = all.stream()
                                .filter(d -> d.getName().startsWith(tipoSel))
                                .mapToDouble(PieChart.Data::getPieValue)
                                .findFirst().orElse(0);

                        ObservableList<PieChart.Data> pizza = FXCollections.observableArrayList(
                                new PieChart.Data(tipoSel, sel),
                                new PieChart.Data("Demais Tipos", totalMortes - sel)
                        );
                        graficoPizza.setData(pizza);
                    }
                }

    // <btnLimpar> ANIMAÇÃO DO BOTÃO DE LIMPAR
            @FXML private void configurarAnimacaoBotaoLIMPAR() {
                ScaleTransition aumenta = new ScaleTransition(Duration.seconds(0.1), btnLimpar);
                aumenta.setToX(1.05);
                aumenta.setToY(1.05);

                ScaleTransition diminui = new ScaleTransition(Duration.seconds(0.1), btnLimpar);
                diminui.setToX(1.0);
                diminui.setToY(1.0);

                ScaleTransition click = new ScaleTransition(Duration.seconds(0.1), btnLimpar);
                click.setToX(1.0);
                click.setToY(1.0);

                ScaleTransition disclick = new ScaleTransition(Duration.seconds(0.1), btnLimpar);
                disclick.setToX(1.05);
                disclick.setToY(1.05);

                btnLimpar.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> {
                    aumenta.playFromStart();
                });

                btnLimpar.addEventHandler(MouseEvent.MOUSE_EXITED, e -> {
                    diminui.playFromStart();
                });

                btnLimpar.setOnMousePressed(e -> {
                    click.playFromStart();
                });

                btnLimpar.setOnMouseReleased(e -> {
                    disclick.playFromStart();
                });
            }
                private void limparFiltros() {
                    comboCidade.getSelectionModel().clearSelection();
                    comboTipoMorte.getSelectionModel().clearSelection();
                    tabelaRank.setItems(dadosOriginais);
                    graficoBarras.setData(CovidDataAnalyzer.dadosGraficoBarras());
                    graficoPizza.setData(CovidDataAnalyzer.dadosGraficoPizza());

                    // restaura eixo Y automático
                    NumberAxis eixoY = (NumberAxis) graficoBarras.getYAxis();
                    eixoY.setAutoRanging(true);
                }

        // <graficoPizza>
            @FXML private void preencherGraficoPizza() {
                ObservableList<PieChart.Data> pieChartData = CovidDataAnalyzer.dadosGraficoPizza();
                graficoPizza.getData().clear();
                graficoPizza.setData(pieChartData);
            }

        // <graficoBarras>
            @FXML private void preencherGraficoBarras() {
                ObservableList<XYChart.Series<String, Number>> dadosBarras = CovidDataAnalyzer.dadosGraficoBarras();
                graficoBarras.getData().clear();
                graficoBarras.setData(dadosBarras);
            }

        // <tabelaRank>
            @FXML private void preencherTabelaRank() {
                    String url = "https://brasil.io/api/v1/dataset/covid19/caso/data/?state=SP&is_last=True&is_repeated=False&page_size=10000";
                    JSONObject resposta = DB_Conn.makeApiRequest(url);

                    if (resposta != null) {
                        JSONArray resultados = resposta.getJSONArray("results");
                        List<TabelaRank> listaTemporaria = new ArrayList<>();
                        // Preenche a lista com os dados brutos
                        for (int i = 0; i < resultados.length(); i++) {
                            JSONObject dados = resultados.getJSONObject(i);
                            String cidade = dados.optString("city", "Desconhecida");
                            int casosConfirmados = dados.optInt("confirmed", 0);

                            if (!cidade.equals("Desconhecida") && casosConfirmados > 0) {
                                listaTemporaria.add(new TabelaRank(0, cidade, casosConfirmados)); // posição será definida depois
                            }
                        }

                        // Ordena pela quantidade de casos (descendente)
                        listaTemporaria.sort((a, b) -> Integer.compare(b.getCasosConfirmados(), a.getCasosConfirmados()));

                        // Atribui posições (ranking)
                        for (int i = 0; i < listaTemporaria.size(); i++) {
                            listaTemporaria.get(i).setPosicao(i + 1);
                        }

                        dadosOriginais = FXCollections.observableArrayList(listaTemporaria);
                        tabelaRank.setItems(dadosOriginais);
                    }
                // PREENCHE A COMBOBOX DO FILTRO
                List<String> cidades = dadosOriginais
                        .stream()
                        .map(TabelaRank::getCidade)
                        .distinct()
                        .sorted()
                        .toList();
                comboCidade.setItems(FXCollections.observableArrayList(cidades));

                // CALCULANDO O TOTAL GERAL
                totalConfirmadosGeral = dadosOriginais
                        .stream()
                        .mapToInt(TabelaRank::getCasosConfirmados)
                        .sum();
            }
                @FXML private void preencherColunas() {
                        colunaRank.setCellValueFactory(new PropertyValueFactory<>("posicao"));
                        colunaCidade.setCellValueFactory(new PropertyValueFactory<>("cidade"));
                        colunaCasos.setCellValueFactory(new PropertyValueFactory<>("casosConfirmados"));
                        centralizarCelulas();
                    }

        // Inicializa os gráficos
            public void inicializarGraficos() {
                    preencherGraficoBarras();
                    ((CategoryAxis) graficoBarras.getXAxis()).setTickLabelRotation(-12); // ‑45° deixa legível
                    graficoBarras.setLegendVisible(false);

                    preencherGraficoPizza();
                    graficoPizza.setLegendVisible(false);

                    preencherTabelaRank();
                }

        // Centraliza as células
            private void centralizarCelulas() {
                // Centraliza a coluna de posição
                colunaRank.setCellFactory(coluna -> new TableCell<TabelaRank, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.toString());
                            setAlignment(Pos.CENTER);
                        }
                    }
                });

                // Centraliza a coluna de cidade
                colunaCidade.setCellFactory(coluna -> new TableCell<TabelaRank, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item);
                            setAlignment(Pos.CENTER);
                        }
                    }
                });

                // Centraliza a coluna de casos confirmados
                colunaCasos.setCellFactory(coluna -> new TableCell<TabelaRank, Integer>() {
                    @Override
                    protected void updateItem(Integer item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item.toString());
                            setAlignment(Pos.CENTER);
                        }
                    }
                });
            }
    // ============================ (FIM) INTERFACE ============================
}