/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.logindialog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.jan.keysaver.beans.Settings_Singelton;
import net.jan.keysaver.mainpage.MainpageController;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.manager.SettingManager;
import net.jan.keysaver.sources.PageLoadHelper;
import net.jan.keysaver.sources.Utilities;

/**
 * FXML Controller class
 *
 * @author Jan Horak
 */
public class LoginDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ImageView imageView;
    @FXML
    private AnchorPane mainpane;
    @FXML
    private PasswordField pwField;
    private String pw = "";
    @FXML
    private Button loginButton;
    @FXML
    private Label errorLabel;
    private Settings_Singelton settingsBean;
    @FXML
    private TitledPane checkUppane;
    private final String PATH_MAIN_FRAME = "Mainpage.fxml";

    @FXML
    private Button btn_import;
    
    @FXML
    private Button btn_checkUp;
    
    @FXML
    private ProgressIndicator indicator;
    @FXML
    private ProgressBar bar;
    @FXML
    private TextArea checkupArea;
    
    @FXML
    private void login(ActionEvent actionEvent) {
        settingsBean = Settings_Singelton.getInstance();
        pw = settingsBean.getValue("MPW");
        if ((Utilities.getHash(pwField.getText()).trim()).equals(pw)) {
            new PageLoadHelper(PATH_MAIN_FRAME, "KeySaver2.0 " + "Version " + settingsBean.getValue("VERSION"), 612, 464, MainpageController.class).loadPage();
            //close the Window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } else {
            errorLabel.setText("Not the right password!");
        }
    }
    
    @FXML
    private void loadImportDialog(){
        new PageLoadHelper().loadImportDialog();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SettingManager sm_icons = new SettingManager("AppData/icons.properties");
        Image image = new Image(new File("AppData/Images/Logo_key.png").toURI().toString());
        imageView.setImage(image);
        try {
            btn_import.setGraphic(FileManager.getImageViewFromPath(sm_icons.returnProperty("FILEIMPORT")));
        } catch (IOException ex) {
            Logger.getLogger(LoginDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        pwField.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                if (t.getCode().equals(KeyCode.ENTER)) {
                }
            }
        });
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        errorLabel.setEffect(r);
    }

    @FXML
    private void updateSize(MouseEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        if (!checkUppane.isExpanded()) {
            stage.setWidth(285);
            stage.setHeight(171);
        } else {
            stage.setWidth(285);
            stage.setHeight(346);
        }
    }
    
    @FXML
    private void checkUp(){
        indicator.setBlendMode(BlendMode.DARKEN);
        bar.setProgress(-1);
        indicator.setProgress(-1);
        btn_checkUp.setDisable(true);
        checkupArea.appendText("Collecting data...");
        
        
        
        
    }
}
