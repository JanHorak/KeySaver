/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.jan.keysaver.beans.Language_Singleton;
import net.jan.keysaver.dialogs.exportdialog.ExportDialogController;
import net.jan.keysaver.dialogs.importdialog.ImportDialogController;
import net.jan.keysaver.dialogs.infodialog.InfoDialogController;
import net.jan.keysaver.dialogs.logindialog.LoginDialogController;
import net.jan.keysaver.dialogs.recreatedialog.RecreateKeyDialogController;
import net.jan.keysaver.dialogs.updatedialog.UpdateDialogController;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.properties.PropertiesController;

/**
 *
 * @author Jan Horak
 */
public class PageLoadHelper {

    private static String pathString;
    private static String title;
    private static double width;
    private static double height;
    private static Language_Singleton langSingleton;
    private static Class c;
    private static Parent root;

    public PageLoadHelper(String pathString, String title, double width, double height, Class c) {
        this.height = height;
        this.pathString = pathString;
        this.title = title;
        this.width = width;
        this.c = c;
    }
    
    public PageLoadHelper(){
        langSingleton = Language_Singleton.getInstance();
    }

    public static void loadPage() {
        try {
            root = FXMLLoader.load(c.getResource(pathString));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.getIcons().add(FileManager.getImageFromPath("AppData/Images/Logo_icon_16x16.png"));
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(PageLoadHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void loadInfoRestartDialog(){
        pathString = "InfoDialog.fxml";
        c = InfoDialogController.class;
        title = langSingleton.getValue("INFODIALOG_TITLE");
        height = 59;
        width = 403;
        loadPage();
    }
    
    public void loadRecreateKeyDialog(){
        pathString = "RecreateKeyDialog.fxml";
        c = RecreateKeyDialogController.class;
        title = langSingleton.getValue("RECREATEKEY_TITLE");
        height = 59;
        width = 403;
        loadPage();
    }
    
    public void loadExportDialog(){
        pathString = "ExportDialog.fxml";
        c = ExportDialogController.class;
        title = langSingleton.getValue("EXPORTDIALOG_TITLE");
        height = 133;
        width = 359;
        loadPage();
    }
    
    /*
     * The Importdialog has to be static, because the Logindialog and the
     * Loginpage have no access to the settings.ini.
     * This is important because the KeySaver on Windows have a lot of 
     * Problems with the read- and write- rights.
     */
    public static void loadImportDialog(){
        pathString = "ImportDialog.fxml";
        c = ImportDialogController.class;
        title = "Importdialog";
        height = 68;
        width = 573;
        loadPage();
    }
    
    public void loadUpdateDialog(){
        pathString = "UpdateDialog.fxml";
        c = UpdateDialogController.class;
        title = langSingleton.getValue("UPDATEDIALOG_TITLE");
        height = 138;
        width = 558;
        loadPage();
    }
    
    public static void loadLoginDialog(){
        pathString = "LoginDialog.fxml";
        c = LoginDialogController.class;
        title = "Login";
        height = 151;
        width = 285;
        loadPage();
    }
    
    public void loadPropertiesDialog(){
        pathString = "Properties.fxml";
        c = PropertiesController.class;
        title = langSingleton.getValue("PROPERTIESDIALOG");
        height = 319;
        width = 428;
        loadPage();
    }

}
