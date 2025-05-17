package br.com.main.controller;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.effect.GaussianBlur;
import javafx.util.Duration;

public class SplashScreenController
{
    @FXML
    private AnchorPane anchorPane;

    @FXML
    public void initialize() {
        // DESFOQUE
        GaussianBlur blur = new GaussianBlur(15);
        anchorPane.setEffect(blur);

        // DURAÇÃO DO DESFOQUE
        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(actionEvent -> anchorPane.setEffect(null));
        pause.play();

        // SUAVIZA O DESFOQUE
        Timeline blurAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(blur.radiusProperty(), 25)),
                new KeyFrame(Duration.seconds(1), new KeyValue(blur.radiusProperty(), 0))
        );

        // REMOVE O EFEITO
        blurAnimation.setOnFinished(e -> anchorPane.setEffect(null));
        blurAnimation.play();
    }
}