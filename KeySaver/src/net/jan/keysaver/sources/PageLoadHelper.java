/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Jan Horak
 */
public class PageLoadHelper {

    private String pathString;
    private String title;
    private double width;
    private double height;
    private Parent root = null;
    private Stage stage;

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
            stage.getIcons().add(new Image(new FileInputStream(new File("AppData/Images/Logo_icon_16x16.png"))));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PageLoadHelper.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
}
