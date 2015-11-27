/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import net.jan.keysaver2.actions.LoginActions;

/**
 * FXML Controller class
 *
 * @author Jan Horak
 */
public class LoginDialogController implements Initializable, ActionController {

    private final static String KEYSAVER_LOGO_PNG = "images/Logo_key.png";

    @FXML
    private ImageView imageView;

    @FXML
    private PasswordField pw_password;

    @FXML
    private TextField tf_username;

    @FXML
    private Hyperlink hyp_loadPK;

    @FXML
    private Hyperlink hyp_register;

    @FXML
    private Label errorLabel;

    @FXML
    private Button btn_login;

    private File selectedPK;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initControlActions();
        Image image = new Image(getClass().getClassLoader().getResource(KEYSAVER_LOGO_PNG).toString());
        imageView.setImage(image);
    }

    @Override
    public void initControlActions() {
        btn_login.setOnAction(LoginActions.loginAction);

        hyp_register.setOnAction(LoginActions.openRegisterAction);

        hyp_loadPK.setOnAction((e) -> {
            final FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Private Key", "*.PK"));
            selectedPK = chooser.showOpenDialog(null);
        });
    }
}
