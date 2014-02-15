/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.logindialog;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.jan.keysaver.beans.Settings_Singleton;
import net.jan.keysaver.dialogs.exportdialog.ExportDialogController;
import net.jan.keysaver.mainpage.MainpageController;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.manager.SettingManager;
import net.jan.keysaver.manager.UserManager;
import net.jan.keysaver.manager.ValidationManager;
import net.jan.keysaver.sources.PageLoadHelper;
import net.jan.keysaver.sources.User;
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
    private PasswordField pwField;
    private String pw = "";
    @FXML
    private Label errorLabel;
    private Settings_Singleton settingsBean;
    @FXML
    private TitledPane registerPane;
    
    private final String PATH_MAIN_FRAME = "Mainpage.fxml";
    @FXML
    private Button btn_import;

    @FXML
    private Button btn_register;
    
    @FXML
    private TextField tf_username;

    @FXML
    private PasswordField tf_password;
    
    @FXML
    private Hyperlink helpLink;
    
    @FXML
    private void login(Event actionEvent) {
        settingsBean = Settings_Singleton.getInstance();
        pw = settingsBean.getValue("MPW");
        if ((Utilities.getHash(pwField.getText()).trim()).equals(pw)) {
            new PageLoadHelper(PATH_MAIN_FRAME, "KeySaver2.0 " + "Version " + settingsBean.getVersion(), 612, 464, MainpageController.class).loadPage();
            //close the Window
            close(actionEvent);
        } else {
            errorLabel.setText("Not the right password!");
        }
    }

    private void close(Event e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void loadImportDialog() {
        PageLoadHelper.loadImportDialog();
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
                    login(t);
                }
            }
        });
    }

    @FXML
    private void updateSize(MouseEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        if (!registerPane.isExpanded()) {
            stage.setWidth(285);
            stage.setHeight(151);
        } else {
            stage.setWidth(285);
            stage.setHeight(260);
        }
    }
    
    @FXML
    private void registerUser(){
        User newUser = new User();
        newUser.setName(tf_username.getText().trim());
        newUser.setMPW(tf_password.getText().trim());
        newUser.setIconPath("AppData/Images/Avatars/Unknown_56x56.png");
        
        if ( ValidationManager.isValid(newUser) ){
            new UserManager().registerUser(newUser);
            pwField.setText(tf_password.getText());
            tf_username.setText("");
            tf_password.setText("");
            errorLabel.setText("Registered!\nPlease log in.");
        } else {
            errorLabel.setText("Invalid Values!");
        }
    }
    
    @FXML
    private void getHelp() {
        try {
            Desktop.getDesktop().open(new File("AppData/Help/Register_help.html"));
        } catch (IOException ex) {
            Logger.getLogger(ExportDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
