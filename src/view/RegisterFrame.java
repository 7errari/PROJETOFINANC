package view;

import controller.UserController;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {
    private UserController userController;
    private JFrame parentFrame;
    
    private JTextField txtName;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JPasswordField txtConfirmPassword;
    private JButton btnRegister;
    private JButton btnCancel;

    public RegisterFrame(UserController userController, JFrame parentFrame) {
        this.userController = userController;
        this.parentFrame = parentFrame;
        
        setTitle("Criar Nova Conta");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Título
        JLabel lblTitle = new JLabel("Criar Nova Conta");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        mainPanel.add(lblTitle, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        
        formPanel.add(new JLabel("Nome Completo:"));
        txtName = new JTextField();
        formPanel.add(txtName);
        
        formPanel.add(new JLabel("Nome de Usuário:"));
        txtUsername = new JTextField();
        formPanel.add(txtUsername);
        
        formPanel.add(new JLabel("Senha:"));
        txtPassword = new JPasswordField();
        formPanel.add(txtPassword);
        
        formPanel.add(new JLabel("Confirmar Senha:"));
        txtConfirmPassword = new JPasswordField();
        formPanel.add(txtConfirmPassword);
        
        btnRegister = new JButton("Registrar");
        btnCancel = new JButton("Cancelar");
        formPanel.add(btnRegister);
        formPanel.add(btnCancel);
        
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        // Adiciona os eventos
        setupEvents();
        
        add(mainPanel);
    }
    
    private void setupEvents() {
        btnRegister.addActionListener(e -> {
            String name = txtName.getText();
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());
            
            if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "As senhas não coincidem!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (userController.registerUser(username, password, name)) {
                JOptionPane.showMessageDialog(this, "Usuário registrado com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
                parentFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Nome de usuário já existe!", 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnCancel.addActionListener(e -> {
            this.dispose();
            parentFrame.setVisible(true);
        });
    }
}
