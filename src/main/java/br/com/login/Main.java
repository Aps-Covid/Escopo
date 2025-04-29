package br.com.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {

    public static Stage primaryStage;

    public static void trocarCena(String fxmlPath)
    {
        try
        {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlPath)); // Carrega o novo FXML
            Stage stage = (Stage) primaryStage.getScene().getWindow(); // Obtém o palco atual (a janela)
            stage.setScene(new Scene(root)); // Define a nova cena
        } catch (IOException e)
        {
            e.printStackTrace(); // Mostra erro se o FXML não for carregado
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        primaryStage = stage;
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/TelaLogin.fxml")));
        Scene scene = new Scene(root);
        stage.setTitle("APS - Projeto Covid Analytics");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}