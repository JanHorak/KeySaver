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
import net.jan.keysaver.dialogs.recreatedialog.RecreateKeyDialogController;
import net.jan.keysaver.dialogs.updatedialog.UpdateDialogController;
import net.jan.keysaver.manager.FileManager;

/**
 *
 * @author Jan Horak
 */
public class PageLoadHelper {

    private String pathString;
    private String title;
    private double width;
    private double height;
    private Language_Singleton langSingleton;
    Class c;

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

    public void loadPage() {
        try {
            Parent root;
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
        height = 156.1;
        width = 359;
        loadPage();
    }
    
    public void loadImportDialog(){
        pathString = "ImportDialog.fxml";
        c = ImportDialogController.class;
        title = langSingleton.getValue("IMPORTDIALOG_TITLE");
        height = 193.1;
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

}
