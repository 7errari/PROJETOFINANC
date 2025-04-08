package controller;

import model.Category;
import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    private List<Category> categories;

    public CategoryController() {
        this.categories = new ArrayList<>();
        // Adiciona algumas categorias padrão
        addDefaultCategories();
    }

    private void addDefaultCategories() {
        categories.add(new Category("Alimentação", "Gastos com comida e restaurantes"));
        categories.add(new Category("Transporte", "Gastos com transporte público, combustível, etc"));
        categories.add(new Category("Moradia", "Aluguel, contas de luz, água, etc"));
        categories.add(new Category("Lazer", "Entretenimento, passeios, etc"));
        categories.add(new Category("Saúde", "Gastos com medicamentos, consultas, etc"));
        categories.add(new Category("Salário", "Receitas provenientes de salário"));
        categories.add(new Category("Outros", "Outros tipos de receitas ou despesas"));
    }

    public boolean addCategory(String name, String description) {
        // Verifica se já existe uma categoria com o mesmo nome
        for (Category category : categories) {
            if (category.getName().equalsIgnoreCase(name)) {
                return false;
            }
        }
        
        categories.add(new Category(name, description));
        return true;
    }

    public boolean updateCategory(int id, String name, String description) {
        for (Category category : categories) {
            if (category.getId() == id) {
                category.setName(name);
                category.setDescription(description);
                return true;
            }
        }
        return false;
    }

    public boolean deleteCategory(int id) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).getId() == id) {
                categories.remove(i);
                return true;
            }
        }
        return false;
    }

    public List<Category> getAllCategories() {
        return new ArrayList<>(categories);
    }

    public Category getCategoryById(int id) {
        for (Category category : categories) {
            if (category.getId() == id) {
                return category;
            }
        }
        return null;
    }
}
