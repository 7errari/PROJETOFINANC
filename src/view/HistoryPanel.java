package view;

import controller.CategoryController;
import controller.TransactionController;
import model.Category;
import model.Transaction;
import model.TransactionType;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class HistoryPanel extends JPanel {
    private TransactionController transactionController;
    private CategoryController categoryController;
    
    private JComboBox<String> cmbDateFilter;
    private JComboBox<String> cmbTypeFilter;
    private JComboBox<Category> cmbCategoryFilter;
    private JButton btnFilter;
    private JButton btnClearFilter;
    
    private JTable tblTransactions;
    private DefaultTableModel tableModel;
    
    private LocalDate startDate;
    private LocalDate endDate;
    private TransactionType typeFilter;
    private Category categoryFilter;

    public HistoryPanel(TransactionController transactionController, CategoryController categoryController) {
        this.transactionController = transactionController;
        this.categoryController = categoryController;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filtros"));
        
        filterPanel.add(new JLabel("Período:"));
        String[] dateOptions = {"Todos", "Hoje", "Últimos 7 dias", "Este mês", "Este ano"};
        cmbDateFilter = new JComboBox<>(dateOptions);
        filterPanel.add(cmbDateFilter);
        
        filterPanel.add(new JLabel("Tipo:"));
        String[] typeOptions = {"Todos", "Receita", "Despesa"};
        cmbTypeFilter = new JComboBox<>(typeOptions);
        filterPanel.add(cmbTypeFilter);
        
        filterPanel.add(new JLabel("Categoria:"));
        cmbCategoryFilter = new JComboBox<>();
        cmbCategoryFilter.addItem(null); // Opção "Todas"
        updateCategoryComboBox();
        filterPanel.add(cmbCategoryFilter);
        
        btnFilter = new JButton("Filtrar");
        filterPanel.add(btnFilter);
        
        btnClearFilter = new JButton("Limpar Filtros");
        filterPanel.add(btnClearFilter);
        
        add(filterPanel, BorderLayout.NORTH);
        
        // Tabela de transações
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição direta na tabela
            }
        };
        
        tableModel.addColumn("ID");
        tableModel.addColumn("Data");
        tableModel.addColumn("Tipo");
        tableModel.addColumn("Valor");
        tableModel.addColumn("Categoria");
        tableModel.addColumn("Descrição");
        
        tblTransactions = new JTable(tableModel);
        tblTransactions.getColumnModel().getColumn(0).setPreferredWidth(30);
        tblTransactions.getColumnModel().getColumn(1).setPreferredWidth(80);
        tblTransactions.getColumnModel().getColumn(2).setPreferredWidth(80);
        tblTransactions.getColumnModel().getColumn(3).setPreferredWidth(80);
        tblTransactions.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblTransactions.getColumnModel().getColumn(5).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(tblTransactions);
        add(scrollPane, BorderLayout.CENTER);
        
        // Painel de resumo
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        summaryPanel.add(new JLabel("Total Filtrado: "));
        JLabel lblTotal = new JLabel("R$ 0,00");
        summaryPanel.add(lblTotal);
        
        add(summaryPanel, BorderLayout.SOUTH);
        
        // Adiciona os eventos
        setupEvents();
        
        // Carrega as transações iniciais
        refreshTable();
    }
    
    private void updateCategoryComboBox() {
        // Mantém o item nulo (Todas) e adiciona as categorias
        Object selectedItem = cmbCategoryFilter.getSelectedItem();
        cmbCategoryFilter.removeAllItems();
        cmbCategoryFilter.addItem(null); // Opção "Todas"
        for (Category category : categoryController.getAllCategories()) {
            cmbCategoryFilter.addItem(category);
        }
        if (selectedItem != null) {
            cmbCategoryFilter.setSelectedItem(selectedItem);
        }
    }
    
    private void setupEvents() {
        btnFilter.addActionListener(e -> {
            applyFilters();
            refreshTable();
        });
        
        btnClearFilter.addActionListener(e -> {
            clearFilters();
            refreshTable();
        });
    }
    
    private void applyFilters() {
        // Filtro de data
        String dateFilter = (String) cmbDateFilter.getSelectedItem();
        LocalDate today = LocalDate.now();
        
        switch (dateFilter) {
            case "Hoje":
                startDate = today;
                endDate = today;
                break;
            case "Últimos 7 dias":
                startDate = today.minusDays(6);
                endDate = today;
                break;
            case "Este mês":
                startDate = today.withDayOfMonth(1);
                endDate = today;
                break;
            case "Este ano":
                startDate = today.withDayOfYear(1);
                endDate = today;
                break;
            default:
                startDate = null;
                endDate = null;
        }
        
        // Filtro de tipo
        String typeFilter = (String) cmbTypeFilter.getSelectedItem();
        
        switch (typeFilter) {
            case "Receita":
                this.typeFilter = TransactionType.INCOME;
                break;
            case "Despesa":
                this.typeFilter = TransactionType.EXPENSE;
                break;
            default:
                this.typeFilter = null;
        }
        
        // Filtro de categoria
        categoryFilter = (Category) cmbCategoryFilter.getSelectedItem();
    }
    
    private void clearFilters() {
        cmbDateFilter.setSelectedIndex(0);
        cmbTypeFilter.setSelectedIndex(0);
        cmbCategoryFilter.setSelectedIndex(0);
        startDate = null;
        endDate = null;
        typeFilter = null;
        categoryFilter = null;
    }
    
    public void refreshTable() {
        // Limpa a tabela
        tableModel.setRowCount(0);
        
        // Obtém as transações filtradas
        List<Transaction> filteredTransactions = transactionController.filterTransactions(
            startDate, endDate, typeFilter, categoryFilter);
        
        // Adiciona as transações à tabela
        double totalAmount = 0.0;
        
        for (Transaction transaction : filteredTransactions) {
            double amount = transaction.getAmount();
            // Para despesas, podemos mostrar o valor negativo na tabela
            if (transaction.getType() == TransactionType.EXPENSE) {
                amount = -amount;
            }
            
            totalAmount += amount;
            
            tableModel.addRow(new Object[]{
                transaction.getId(),
                transaction.getFormattedDate(),
                transaction.getType(),
                String.format("R$ %.2f", amount),
                transaction.getCategory().getName(),
                transaction.getDescription()
            });
        }
        
        // Atualiza o total
        JLabel lblTotal = (JLabel) ((JPanel) getComponent(2)).getComponent(1);
        lblTotal.setText(String.format("R$ %.2f", totalAmount));
        
        // Define a cor do total de acordo com o valor
        if (totalAmount < 0) {
            lblTotal.setForeground(Color.RED);
        } else {
            lblTotal.setForeground(new Color(0, 100, 0)); // Verde escuro
        }
    }
}
