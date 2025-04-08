package controller;

import model.User;
import java.util.ArrayList;
import java.util.List;

public class UserController {
    private List<User> users;
    private User currentUser;

    public UserController() {
        this.users = new ArrayList<>();
        // Adiciona um usuário padrão para testes
        users.add(new User("admin", "admin", "Administrador"));
    }

    public boolean registerUser(String username, String password, String name) {
        // Verifica se o username já existe
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        
        users.add(new User(username, password, name));
        return true;
    }

    public boolean authenticateUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    
    public void logout() {
        currentUser = null;
    }
}
