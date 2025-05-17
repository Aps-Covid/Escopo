package br.com.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;

public class Main extends Application
{
    public static Stage primaryStage;

    public static void trocarCena(String fxmlPath)
    {
        try
        {
            Parent root = FXMLLoader.load(Main.class.getResource(fxmlPath));
            Stage stage = (Stage) primaryStage.getScene().getWindow();


            stage.setScene(new Scene(root));

        } catch (IOException e)
        {
            e.printStackTrace(); // Mostra erro se o FXML não for carregado
        }
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        primaryStage = stage;

        Parent splashRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/SplashScreen.fxml")));
        Scene splashScene = new Scene(splashRoot);
        stage.setScene(splashScene);
        stage.setTitle("APS - Projeto Covid Analytics");
        stage.setResizable(false);
        stage.show();

        Image icon = new Image(getClass().getResourceAsStream("/images/logo.png"));
        stage.getIcons().add(icon);

        // Espera 2 segundos com splash visível
        PauseTransition delay = new PauseTransition(Duration.seconds(5));
        delay.setOnFinished(event -> {
            // Fade-out da splash
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), splashRoot);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> {
                try {

                    // -------------------------------- ! ALTERAR ! --------------------------------
                    Parent loginRoot = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/TelaLogin1.fxml")));
                    // -------------------------------- ! ALTERAR ! --------------------------------

                    loginRoot.setOpacity(0.0); // Garante que comece invisível, evita piscada

                    FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), loginRoot);
                    fadeIn.setFromValue(0.0);
                    fadeIn.setToValue(1.0);

                    Scene loginScene = new Scene(loginRoot);
                    stage.setScene(loginScene);
                    fadeIn.play(); // Inicia o fade-in
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            fadeOut.play(); // Inicia o fade-out
        });
        delay.play();
    }

    public static void main(String[] args) {
        // Resetando o banco de dados antes de iniciar a interface gráfica
        ResetDB.resetDatabase();

        // Chama a inicialização do banco de dados e inserção dos dados da API
        DB_Conn.main(args);

        // Depois inicializa a interface gráfica
        launch(args);
    }
}