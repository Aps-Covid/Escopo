package br.com.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

public class TelaLoginController
{
    @FXML
    private
    TextField usernameField;

    @FXML
    private
    PasswordField passwordField;

    @FXML
    private void handleLoginButton(ActionEvent event)
    {
        String usuario = usernameField.getText();
        String senha = passwordField.getText();

        System.out.println("Usuário: " + usuario);
        System.out.println("Senha: " + senha);
    }


}