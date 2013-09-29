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
    Class c;

    public PageLoadHelper(String pathString, String title, double width, double height, Class c) {
        this.height = height;
        this.pathString = pathString;
        this.title = title;
        this.width = width;
        this.c = c;
    }

    public void loadPage() {
        try {
            Parent root;
            root = FXMLLoader.load(c.getResource(pathString));
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
