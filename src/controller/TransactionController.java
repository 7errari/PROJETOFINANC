package controller;

import model.Category;
import model.Transaction;
import model.TransactionType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionController {
    private List<Transaction> transactions;

    public TransactionController() {
        this.transactions = new ArrayList<>();
    }

    public boolean addTransaction(double amount, String description, LocalDate date, 
                                 TransactionType type, Category category) {
        transactions.add(new Transaction(amount, description, date, type, category));
        return true;
    }

    public boolean updateTransaction(int id, double amount, String description, 
                                    LocalDate date, TransactionType type, Category category) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) {
                transaction.setAmount(amount);
                transaction.setDescription(description);
                transaction.setDate(date);
                transaction.setType(type);
                transaction.setCategory(category);
                return true;
            }
        }
        return false;
    }

    public boolean deleteTransaction(int id) {
        for (int i = 0; i < transactions.size(); i++) {
            if (transactions.get(i).getId() == id) {
                transactions.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions);
    }

    public List<Transaction> getTransactionsByPeriod(LocalDate start, LocalDate end) {
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByType(TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }

    public List<Transaction> getTransactionsByCategory(Category category) {
        return transactions.stream()
                .filter(t -> t.getCategory().getId() == category.getId())
                .collect(Collectors.toList());
    }
    
    public List<Transaction> filterTransactions(LocalDate startDate, LocalDate endDate, 
                                               TransactionType type, Category category) {
        return transactions.stream()
                .filter(t -> startDate == null || !t.getDate().isBefore(startDate))
                .filter(t -> endDate == null || !t.getDate().isAfter(endDate))
                .filter(t -> type == null || t.getType() == type)
                .filter(t -> category == null || t.getCategory().getId() == category.getId())
                .collect(Collectors.toList());
    }

    public double getTotalIncome(LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .filter(t -> startDate == null || !t.getDate().isBefore(startDate))
                .filter(t -> endDate == null || !t.getDate().isAfter(endDate))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalExpense(LocalDate startDate, LocalDate endDate) {
        return transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .filter(t -> startDate == null || !t.getDate().isBefore(startDate))
                .filter(t -> endDate == null || !t.getDate().isAfter(endDate))
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getBalance(LocalDate startDate, LocalDate endDate) {
        return getTotalIncome(startDate, endDate) - getTotalExpense(startDate, endDate);
    }
}
