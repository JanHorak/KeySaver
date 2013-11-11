package net.jan.keysaver.main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.application.Application;
import javafx.stage.Stage;
import net.jan.keysaver.dialogs.logindialog.LoginDialogController;
import net.jan.keysaver.sources.PageLoadHelper;

/**
 *
 * @author Jan Horak
 */
public class KeySaver extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        new PageLoadHelper("LoginDialog.fxml", "Login",285, 151, LoginDialogController.class).loadPage();
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}