/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.sources;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.jan.keysaver2.controller.RegisterDialogController;
import net.jan.keysaver2.controller.ImportDialogController;
import net.jan.keysaver2.controller.InfoDialogController;
import net.jan.keysaver2.controller.LoginDialogController;
import net.jan.keysaver2.controller.RecreateKeyDialogController;
import net.jan.keysaver2.controller.PropertiesController;

/**
 *
 * @author Jan Horak
 */
public class PageLoadHelper {

    private String pathString;
    private String title;
    private double width;
    private double height;

    private Class c;
    private Parent root;

    public PageLoadHelper(String pathString, String title, double width, double height, Class c) {
        this.height = height;
        this.pathString = pathString;
        this.title = title;
        this.width = width;
        this.c = c;
    }

    public PageLoadHelper() {
        
    }

    public void loadPage() {
        try {
            root = FXMLLoader.load(new URL(pathString) );
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
//            stage.getIcons().add(FileManager.getImageFromPath("AppData/Images/Logo_icon_16x16.png"));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PageLoadHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void loadInfoRestartDialog() {
        pathString = "InfoDialog.fxml";
        c = InfoDialogController.class;

        height = 59;
        width = 403;
        loadPage();
    }

    public void loadRecreateKeyDialog() {
        pathString = "RecreateKeyDialog.fxml";
        c = RecreateKeyDialogController.class;

        height = 59;
        width = 403;
        loadPage();
    }

    public void loadRegisterDialog() {
        pathString = getClass().getClassLoader().getResource("fxml/RegisterDialog.fxml").toString();
        c = RegisterDialogController.class;
        title = "Neuen User anlegen";
        height = 188;
        width = 258;
        loadPage();
    }

    /*
     * The Importdialog has to be static, because the Logindialog and the
     * Loginpage have no access to the settings.ini.
     * This is important because the KeySaver on Windows have a lot of 
     * Problems with the read- and write- rights.
     */
    public void loadImportDialog() {
        pathString = "ImportDialog.fxml";
        c = ImportDialogController.class;
        title = "Importdialog";
        height = 68;
        width = 573;
        loadPage();
    }

    public void loadLoginDialog() {
        pathString = getClass().getClassLoader().getResource("fxml/LoginDialog.fxml").toString();
        c = LoginDialogController.class;
        title = "Login";
        height = 192;
        width = 285;
        loadPage();
    }

    public void loadPropertiesDialog() {
        pathString = "Properties.fxml";
        c = PropertiesController.class;
        height = 319;
        width = 428;
        loadPage();
    }

}
