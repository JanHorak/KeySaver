package net.jan.keysaver2.main;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import javafx.application.Application;
import javafx.stage.Stage;
import net.jan.keysaver2.sources.PageLoadHelper;

/**
 *
 * @author Jan Horak
 */
public class KeySaver2 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        new PageLoadHelper().loadLoginDialog();
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