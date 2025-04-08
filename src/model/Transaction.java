package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private int id;
    private double amount;
    private String description;
    private LocalDate date;
    private TransactionType type;
    private Category category;
    private static int nextId = 1;

    public Transaction(double amount, String description, LocalDate date, 
                      TransactionType type, Category category) {
        this.id = nextId++;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.type = type;
        this.category = category;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    }
}
