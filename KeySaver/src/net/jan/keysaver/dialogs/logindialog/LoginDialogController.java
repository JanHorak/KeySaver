/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.logindialog;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.jan.keysaver.beans.Settings_Singelton;
import net.jan.keysaver.mainpage.MainpageController;
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
    @FXML
    private Label versionLabel;
    private Settings_Singelton settingsBean;
    
    private final String PATH_MAIN_FRAME = "Mainpage.fxml";

    @FXML
    private void login(ActionEvent actionEvent) {
        if ((Utilities.getHash(pwField.getText()).trim()).equals(pw)) {
            new PageLoadHelper(PATH_MAIN_FRAME, "KeySaver2.0 " +"Version "+ settingsBean.getValue("VERSION"), 612, 464, MainpageController.class).loadPage();
            //close the Window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        } else {
            errorLabel.setText("Not the right password!");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        settingsBean = Settings_Singelton.getInstance();
        Image image = new Image(new File("AppData/Images/Logo_key.png").toURI().toString());
        imageView.setImage(image);
        pw = settingsBean.getValue("MPW");
        versionLabel.setText(settingsBean.getValue("VERSION"));
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
}
