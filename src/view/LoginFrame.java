package view;

import controller.UserController;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private UserController userController;
    
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnRegister;

    public LoginFrame(UserController userController) {
        this.userController = userController;
        
        setTitle("Sistema de Gerenciamento Financeiro - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitle = new JLabel("Sistema de Gerenciamento Financeiro");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        
        formPanel.add(new JLabel("Usuário:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);
        
        formPanel.add(new JLabel("Senha:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);
        
        btnLogin = new JButton("Entrar");
        btnRegister = new JButton("Criar Conta");
        formPanel.add(btnLogin);
        formPanel.add(btnRegister);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Adiciona os eventos
        setupEvents();
        
        add(mainPanel);
    }
    
    private void setupEvents() {
        btnLogin.addActionListener(e -> {
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            
            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (userController.authenticateUser(username, password)) {
                // Abre a tela principal
                MainFrame mainFrame = new MainFrame(userController);
                mainFrame.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha incorretos!", 
                    "Erro de Autenticação", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnRegister.addActionListener(e -> {
            // Abre a tela de registro
            RegisterFrame registerFrame = new RegisterFrame(userController, this);
            registerFrame.setVisible(true);
            this.setVisible(false);
        });
    }
}
