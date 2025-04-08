package view;

import controller.CategoryController;
import controller.TransactionController;
import controller.UserController;
import model.User;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private UserController userController;
    private CategoryController categoryController;
    private TransactionController transactionController;
    
    private JTabbedPane tabbedPane;
    private JLabel lblWelcome;
    private JButton btnLogout;

    public MainFrame(UserController userController) {
        this.userController = userController;
        this.categoryController = new CategoryController();
        this.transactionController = new TransactionController();
        
        setTitle("Sistema de Gerenciamento Financeiro");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Cabeçalho com informações do usuário e botão de logout
        JPanel headerPanel = new JPanel(new BorderLayout());
        User currentUser = userController.getCurrentUser();
        lblWelcome = new JLabel("Bem-vindo, " + currentUser.getName() + "!");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 14));
        headerPanel.add(lblWelcome, BorderLayout.WEST);
        
        btnLogout = new JButton("Sair");
        headerPanel.add(btnLogout, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Painel com abas
        tabbedPane = new JTabbedPane();
        
        // Adiciona as abas
        tabbedPane.addTab("Dashboard", new DashboardPanel(transactionController));
        tabbedPane.addTab("Transações", new TransactionPanel(transactionController, categoryController));
        tabbedPane.addTab("Histórico", new HistoryPanel(transactionController, categoryController));
        tabbedPane.addTab("Categorias", new CategoryPanel(categoryController));
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        // Adiciona os eventos
        setupEvents();
        
        add(mainPanel);
    }
    
    private void setupEvents() {
        btnLogout.addActionListener(e -> {
            userController.logout();
            LoginFrame loginFrame = new LoginFrame(userController);
            loginFrame.setVisible(true);
            this.dispose();
        });
    }
}
