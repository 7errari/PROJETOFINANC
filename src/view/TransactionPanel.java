package view;

import controller.CategoryController;
import controller.TransactionController;
import model.Category;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionPanel extends JPanel {
    private TransactionController transactionController;
    private CategoryController categoryController;
    
    private JTextField txtAmount;
    private JTextField txtDescription;
    private JComboBox<TransactionType> cmbType;
    private JComboBox<Category> cmbCategory;
    private JSpinner dateSpinner;
    private JButton btnSave;
    private JButton btnClear;

    public TransactionPanel(TransactionController transactionController, CategoryController categoryController) {
        this.transactionController = transactionController;
        this.categoryController = categoryController;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título
        JLabel lblTitle = new JLabel("Cadastro de Transações");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitle.setHorizontalAlignment(JLabel.CENTER);
        add(lblTitle, BorderLayout.NORTH);
        
        // Painel de formulário
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        formPanel.add(new JLabel("Tipo:"));
        cmbType = new JComboBox<>(TransactionType.values());
        formPanel.add(cmbType);
        
        formPanel.add(new JLabel("Valor:"));
        txtAmount = new JTextField();
        formPanel.add(txtAmount);
        
        formPanel.add(new JLabel("Descrição:"));
        txtDescription = new JTextField();
        formPanel.add(txtDescription);
        
        formPanel.add(new JLabel("Categoria:"));
        cmbCategory = new JComboBox<>();
        updateCategoryComboBox();
        formPanel.add(cmbCategory);
        
        formPanel.add(new JLabel("Data:"));
        SpinnerDateModel dateModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "dd/MM/yyyy");
        dateSpinner.setEditor(dateEditor);
        formPanel.add(dateSpinner);
        
        btnSave = new JButton("Salvar");
        btnClear = new JButton("Limpar");
        formPanel.add(btnSave);
        formPanel.add(btnClear);
        
        // Adiciona o painel de formulário ao centro
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(formPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // Adiciona os eventos
        setupEvents();
    }
    
    private void updateCategoryComboBox() {
        cmbCategory.removeAllItems();
        for (Category category : categoryController.getAllCategories()) {
            cmbCategory.addItem(category);
        }
    }
    
    private void setupEvents() {
        btnSave.addActionListener(e -> {
            try {
                // Valida os campos
                if (txtDescription.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Por favor, insira uma descrição para a transação!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                double amount;
                try {
                    amount = Double.parseDouble(txtAmount.getText().replace(",", "."));
                    if (amount <= 0) {
                         throw new NumberFormatException();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Por favor, insira um valor válido maior que zero!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                TransactionType type = (TransactionType) cmbType.getSelectedItem();
                Category category = (Category) cmbCategory.getSelectedItem();
                
                if (category == null) {
                    JOptionPane.showMessageDialog(this, "Por favor, selecione uma categoria!", 
                        "Erro", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                java.util.Date date = (java.util.Date) dateSpinner.getValue();
                LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                
                // Adiciona a transação
                transactionController.addTransaction(amount, txtDescription.getText(), localDate, type, category);
                
                JOptionPane.showMessageDialog(this, "Transação cadastrada com sucesso!", 
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                
                // Limpa os campos
                clearFields();
                
                // Atualiza outras abas se necessário
                if (getParent() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) getParent();
                    Component comp = pane.getComponentAt(0); // Dashboard
                    if (comp instanceof DashboardPanel) {
                        ((DashboardPanel) comp).refresh();
                    }
                    
                    comp = pane.getComponentAt(2); // Histórico
                    if (comp instanceof HistoryPanel) {
                        ((HistoryPanel) comp).refreshTable();
                    }
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar transação: " + ex.getMessage(), 
                    "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnClear.addActionListener(e -> {
            clearFields();
        });
    }
    
    private void clearFields() {
        txtAmount.setText("");
        txtDescription.setText("");
        cmbType.setSelectedIndex(0);
        cmbCategory.setSelectedIndex(0);
        dateSpinner.setValue(new java.util.Date());
    }
    
    // Método para atualizar o painel
    public void refresh() {
        updateCategoryComboBox();
    }
}
