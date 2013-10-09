/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.recreatedialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.jan.keysaver.beans.Language_Singleton;
import net.jan.keysaver.dialogs.infodialog.InfoDialogController;
import net.jan.keysaver.manager.SettingManager;

/**
 * FXML Controller class
 *
 * @author janhorak
 */
public class RecreateKeyDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private ImageView imageView;
    
    @FXML
    private void cancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Language_Singleton langS = Language_Singleton.getInstance();
        messageLabel.setText(langS.getValue("RECREATEKEY_TEXT"));
        SettingManager iconManager = new SettingManager("AppData/icons.properties");
        try {
            imageView.setImage(new Image(new FileInputStream(new File(iconManager.returnProperty("INFO")))));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(InfoDialogController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(InfoDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
}
