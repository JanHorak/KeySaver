/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.jan.keysaver.manager.ErrorManager;

/**
 *
 * @author Jan Horak
 */
public class PageLoadHelper {

    String pathString;
    String title;
    double width;
    double height;
    Parent root = null;
    Stage stage;

    public PageLoadHelper(String pathString, String title, double width, double height) {
        this.height = height;
        this.pathString = pathString;
        this.title = title;
        this.width = width;
        loadPage();
    }

    private void loadPage() {
        try {
            Parent root;
            root = FXMLLoader.load(new File(pathString).toURL());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PageLoadHelper.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
