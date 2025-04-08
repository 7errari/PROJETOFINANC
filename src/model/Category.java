package model;

public class Category {
    private int id;
    private String name;
    private String description;
    private static int nextId = 1;

    public Category(String name, String description) {
        this.id = nextId++;
        this.name = name;
        this.description = description;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
