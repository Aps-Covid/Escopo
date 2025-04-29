package br.com.login;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.awt.event.ActionEvent;

public class TelaCadastroController
{
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Label lblMensagem;

    public void initialize()
    {
        lblMensagem.setText("");
    }

    @FXML
    private void cadastrarUsuario() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty())
        {
            lblMensagem.setText("Preencha todos os campos!");
        }

        lblMensagem.setText("Usuário cadastrado com sucesso!"); // GEOVANNA - Salvar em um banco de dados!
    }

    public void voltarParaLogin() {
        br.com.login.Main.trocarCena("/view/TelaLogin.fxml");
    }
}