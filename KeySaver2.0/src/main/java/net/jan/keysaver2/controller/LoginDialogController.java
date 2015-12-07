/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.jan.keysaver2.actions.GeneralActions;
import net.jan.keysaver2.actions.LoginActions;
import net.jan.keysaver2.entities.AppUser;
import net.jan.keysaver2.manager.UserManager;
import net.jan.keysaver2.security.Decryption;

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
    private Label lb_pkPath;

    @FXML
    private Button btn_login;

    @FXML
    private Button btn_newPK;

    private File selectedPK;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initControlActions();
        Image image = new Image(getClass().getClassLoader().getResource(KEYSAVER_LOGO_PNG).toString());
        imageView.setImage(image);
    }

    @Override
    public void initControlActions() {
        btn_login.setOnAction((e) -> {
            String decryptedPw = "";
            UserManager um = new UserManager();
            AppUser user = um.getUserByName(tf_username.getText());
            if (user != null) {
                try {
                    decryptedPw = Decryption.decrypt(selectedPK.getAbsolutePath(), user.getPassword());
                    System.out.println(decryptedPw);
                } catch (IOException | InvalidKeyException ex) {
                    Logger.getLogger(LoginDialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (pw_password.getText().equals(decryptedPw)) {
                    System.out.println("password correct");
                }
            } else {

            }

        });

        hyp_register.setOnAction(LoginActions.openRegisterAction);

        hyp_loadPK.setOnAction((e) -> {
            final FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Private Key", "*.key"));
            selectedPK = chooser.showOpenDialog(null);
            if (selectedPK.getAbsolutePath() != null) {
                lb_pkPath.setText(selectedPK.getAbsolutePath());
                hyp_loadPK.setDisable(true);
                GeneralActions.fadeOut(hyp_loadPK);
                GeneralActions.fadeIn(lb_pkPath);
                GeneralActions.fadeIn(btn_newPK);
            }

        });

        btn_newPK.setOnAction((e) -> {
            hyp_loadPK.setDisable(false);
            GeneralActions.fadeIn(hyp_loadPK);
            GeneralActions.fadeOut(btn_newPK);
            GeneralActions.fadeOut(lb_pkPath);

        });
    }
}
