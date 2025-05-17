package br.com.main.controller;

import br.com.main.Auth;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class TelaLoginController
{
    @FXML
    private
    TextField txtFieldUser;

    @FXML
    private
    PasswordField txtFieldPassword;

    @FXML
    private void handleLoginButton(ActionEvent event)
    {
        String usuario = txtFieldUser.getText();
        String senha = txtFieldPassword.getText();

        boolean autenticado = Auth.logar(usuario, senha);

        if (autenticado) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/TelaPrincipal.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                stage.setTitle("Tela Principal");
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Erro ao carregar a tela principal: " + e.getMessage());
            }
        } else {
            System.out.println("Falha no login.");
        }

    }
}