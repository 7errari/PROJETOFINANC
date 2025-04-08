import controller.UserController;
import view.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            UserController userController = new UserController();
            LoginFrame loginFrame = new LoginFrame(userController);
            loginFrame.setVisible(true);
        });
    }
}
