/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import net.jan.keysaver2.actions.GeneralActions;
import net.jan.keysaver2.entities.AppUser;
import net.jan.keysaver2.manager.UserManager;
import net.jan.keysaver2.security.KeyGenerationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author janhorak
 */
public class RegisterDialogController implements Initializable, ActionController {

    private static final int DEFAULT_ENCRYPTION_STRENGTH = 2048;

    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterDialogController.class);

    @FXML
    private Label byteLabel;
    
    @FXML
    private Label lb_path;

    @FXML
    private Slider byteSlider;

    @FXML
    private PasswordField pw_userpassword;

    @FXML
    private TextField tf_username;

    @FXML
    private Button btn_register;
    
    @FXML
    private Button btn_newPath;

    @FXML
    private Hyperlink hyp_savePubK;

    String pkPath;

    private AppUser user;

    private File pkFile;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        byteSlider.setValue(DEFAULT_ENCRYPTION_STRENGTH);
        byteLabel.setText(String.valueOf(Math.round(byteSlider.getValue())));
        initControlActions();
    }

    @Override
    public void initControlActions() {
        GeneralActions.fadeOut(btn_newPath);
        byteSlider.valueProperty().addListener((e) -> {
            byteLabel.setText(String.valueOf(Math.round(byteSlider.getValue())));
        });

        hyp_savePubK.setOnAction((e) -> {
            final FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Private Key", "*.pk"));
            pkFile = chooser.showSaveDialog(null);
            if (pkFile != null) {
                pkPath = pkFile.getAbsolutePath();
                lb_path.setText(pkPath);
                
                GeneralActions.fadeIn(lb_path);
                GeneralActions.fadeIn(btn_newPath);
                GeneralActions.fadeOut(hyp_savePubK);
                hyp_savePubK.setDisable(true);
            }
        });

        btn_register.setOnAction((e) -> {
            String pubPath = new File("public.key").getAbsolutePath();
            try {
                new KeyGenerationHelper(((int) byteSlider.getValue()), pubPath, pkPath).generateAndStoreKeys();
            } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException ex) {
                LOGGER.error("Error during creating key pair!");
            }
            new Thread(registerTask).start();
        });
        
        btn_newPath.setOnAction((e) -> {
            GeneralActions.fadeOut(btn_newPath);
            GeneralActions.fadeOut(lb_path);
            GeneralActions.fadeIn(hyp_savePubK);
            hyp_savePubK.setDisable(false);
        });
    }

    private final Task registerTask = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            user = new AppUser();
            user.setAvatar(new byte[5]);
            user.setPk(KeyGenerationHelper.readPrivateKeyFromFile(new File("public.key").getAbsolutePath()).getEncoded());
            user.setCatList(new ArrayList<>());
            user.setName(tf_username.getText());
            user.setPassword(pw_userpassword.getText().getBytes());
            new UserManager().registerUser(user);
            new File("public.key").delete();
            LOGGER.info("New User saved. Cleaned up!");
            return null;
        }
    };
    
    

}
