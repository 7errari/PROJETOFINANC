package view;

import controller.TransactionController;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardPanel extends JPanel {
    private TransactionController transactionController;
    
    private JLabel lblTotalIncome;
    private JLabel lblTotalExpense;
    private JLabel lblBalance;
    private JLabel lblPeriod;
    
    private JComboBox<String> cmbPeriod;
    
    private LocalDate startDate;
    private LocalDate endDate;

    public DashboardPanel(TransactionController transactionController) {
        this.transactionController = transactionController;
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Painel superior com filtro de período
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Período: "));
        
        String[] periods = {"Últimos 30 dias", "Este mês", "Mês passado", "Este ano", "Ano passado", "Todo o período"};
        cmbPeriod = new JComboBox<>(periods);
        topPanel.add(cmbPeriod);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Painel central com cards de resumo
        JPanel centerPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        
        // Card de Receitas
        JPanel incomeCard = createCard("Receitas", "0.00", Color.GREEN);
        lblTotalIncome = (JLabel) ((JPanel) incomeCard.getComponent(1)).getComponent(0);
        centerPanel.add(incomeCard);
        
        // Card de Despesas
        JPanel expenseCard = createCard("Despesas", "0.00", Color.RED);
        lblTotalExpense = (JLabel) ((JPanel) expenseCard.getComponent(1)).getComponent(0);
        centerPanel.add(expenseCard);
        
        // Card de Saldo
        JPanel balanceCard = createCard("Saldo", "0.00", Color.BLUE);
        lblBalance = (JLabel) ((JPanel) balanceCard.getComponent(1)).getComponent(0);
        centerPanel.add(balanceCard);
        
        add(centerPanel, BorderLayout.CENTER);
        
        // Painel inferior com informações adicionais
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblPeriod = new JLabel("Período: ");
        bottomPanel.add(lblPeriod);
        
        add(bottomPanel, BorderLayout.SOUTH);
        
        // Adiciona os eventos
        setupEvents();
        
        // Define o período inicial como os últimos 30 dias
        cmbPeriod.setSelectedItem("Últimos 30 dias");
    }
    
    private JPanel createCard(String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout(5, 5));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 16));
        card.add(lblTitle, BorderLayout.NORTH);
        
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lblValue = new JLabel("R$ " + value);
        lblValue.setFont(new Font("Arial", Font.BOLD, 22));
        lblValue.setForeground(color);
        valuePanel.add(lblValue);
        
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private void setupEvents() {
        cmbPeriod.addActionListener(e -> {
            updatePeriod();
            updateSummary();
        });
    }
    
    private void updatePeriod() {
        String selectedPeriod = (String) cmbPeriod.getSelectedItem();
        LocalDate today = LocalDate.now();
        
        switch (selectedPeriod) {
            case "Últimos 30 dias":
                startDate = today.minusDays(30);
                endDate = today;
                break;
            case "Este mês":
                startDate = today.withDayOfMonth(1);
                endDate = today;
                break;
            case "Mês passado":
                startDate = today.minusMonths(1).withDayOfMonth(1);
                endDate = today.withDayOfMonth(1).minusDays(1);
                break;
            case "Este ano":
                startDate = today.withDayOfYear(1);
                endDate = today;
                break;
            case "Ano passado":
                startDate = today.minusYears(1).withDayOfYear(1);
                endDate = today.withDayOfYear(1).minusDays(1);
                break;
            case "Todo o período":
                startDate = null;
                endDate = null;
                break;
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        if (startDate != null && endDate != null) {
            lblPeriod.setText("Período: " + startDate.format(formatter) + " até " + endDate.format(formatter));
        } else {
            lblPeriod.setText("Período: Todo o período");
        }
    }
    
    private void updateSummary() {
        double totalIncome = transactionController.getTotalIncome(startDate, endDate);
        double totalExpense = transactionController.getTotalExpense(startDate, endDate);
        double balance = totalIncome - totalExpense;
        
        lblTotalIncome.setText(String.format("R$ %.2f", totalIncome));
        lblTotalExpense.setText(String.format("R$ %.2f", totalExpense));
        lblBalance.setText(String.format("R$ %.2f", balance));
        
        // Muda a cor do saldo dependendo se é positivo ou negativo
        if (balance < 0) {
            lblBalance.setForeground(Color.RED);
        } else {
            lblBalance.setForeground(Color.BLUE);
        }
    }
    
    // Método para atualizar o painel
    public void refresh() {
        updateSummary();
    }
}
