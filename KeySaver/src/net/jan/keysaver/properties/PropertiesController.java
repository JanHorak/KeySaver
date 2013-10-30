/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Reflection;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
import net.jan.aes.keygenerationmanager.KeyGenerationManager;
import net.jan.keysaver.manager.LoggingManager;
import net.jan.keysaver.manager.SettingManager;
import net.jan.keysaver.beans.Language_Singleton;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.manager.ValidationManager;

import net.jan.keysaver.sources.PageLoadHelper;
import net.jan.keysaver.sources.Utilities;
import net.jan.keysaver.validationentities.PropertiesEntity;

/**
 * FXML Controller class
 *
 * @author Jan Horak
 */
public class PropertiesController implements Initializable {

    // ==== Attributes =====
    //Buttons
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_createNewKey;
    
    //TextFields
    @FXML
    public TextField tf_name;
    
    //PasswordFields
    @FXML
    public PasswordField confirm_pwfield;
    @FXML
    public PasswordField pwfield;
    
    //Labels
    @FXML
    private Label lb_username;
    @FXML
    private Label lb_pw;
    @FXML
    private Label lb_confirmPW;
    @FXML
    private Label lb_image;
    @FXML
    private Label lb_debugmode;
    @FXML
    private Label lb_createNewKey;
    @FXML
    public Label errorLabel;
    
    //Others
    @FXML
    private ImageView statusImage;
    @FXML
    private ListView<Label> iconList;
    @FXML
    private CheckBox chk_debug;
    @FXML
    private Tooltip debugTooltip;
    @FXML
    
    //NotFX- Components
    private Tooltip encKeyTooltip;
    private String selectedAvatar = "";
    private String selectedInitialAvatar = "";
    SettingManager sm_main = new SettingManager("settings.ini");
    private int debug = 0;
    Language_Singleton language_singelton;

    // ==== END OF ATTRIBUTES ====
    
    @FXML
    private void save(ActionEvent actionEvent) {

        PropertiesEntity validationObject = new PropertiesEntity();
        validationObject.setUserName(tf_name.getText());
        validationObject.setPassword(pwfield.getText());
        validationObject.setPassword_confirm(confirm_pwfield.getText());

        if (ValidationManager.isValid(validationObject)) {
            errorLabel.setVisible(false);
            if (chk_debug.isSelected()) {
                debug = 1;
            } else {
                debug = 0;
            }
            try {
                sm_main = new SettingManager("settings.ini");
                sm_main.storeProperty("USERNAME", tf_name.getText());
                sm_main.storeProperty("MPW", Utilities.getHash(confirm_pwfield.getText()).trim());
                if (selectedAvatar.equals("")) {
                    sm_main.storeProperty("AVATAR", selectedInitialAvatar);
                } else {
                    sm_main.storeProperty("AVATAR", "AppData/Images/Avatars/" + selectedAvatar);
                }
                sm_main.storeProperty("DEBUG", String.valueOf(debug));
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("Cant find Error-File", ex);
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile(null, ex);
            }

            //closeEvent
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

            new PageLoadHelper().loadInfoRestartDialog();
        } else {
            errorLabel.setVisible(true);
        }
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Reflection r = new Reflection();
        r.setFraction(0.7f);
        errorLabel.setEffect(r);
        errorLabel.setVisible(false);
        language_singelton = Language_Singleton.getInstance();
        initLanguage();
        try {
            debug = Integer.decode(sm_main.returnProperty("DEBUG"));
            chk_debug.setSelected(getSelectedDebug(debug));
        } catch (IOException ex) {
            Logger.getLogger(PropertiesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        debugTooltip.setText(language_singelton.getValue("DEBUGMODE"));
        try {
            tf_name.setText(sm_main.returnProperty("USERNAME"));
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        }
        try {
            selectedInitialAvatar = sm_main.returnProperty("AVATAR");
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        }
        ObservableList<Label> avatarList = FXCollections.observableArrayList();

        File dir = new File("AppData/Images/Avatars");
        String[] files = dir.list();
        int tmpcounter = 0;
        int position = 0;
        for (String s : files) {
            Label l = new Label(s);
            l.setGraphic(FileManager.getImageViewFromPath("AppData/Images/Avatars/" + s));
            if (("AppData/Images/Avatars/" + s).equals(selectedInitialAvatar)) {
                position = tmpcounter;
            }
            avatarList.add(l);
            tmpcounter++;
        }

        iconList.setItems(avatarList);
        iconList.getSelectionModel().select(position);
        iconList.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent t) {
                selectedAvatar = iconList.getSelectionModel().getSelectedItem().getText();
                if (("AppData/Images/Avatars/" + selectedAvatar).equals(selectedInitialAvatar)) {
                    btn_save.setDisable(true);
                }
            }
        });
        SettingManager sm_icon = new SettingManager("AppData/icons.properties");
        try {
            lb_createNewKey.setGraphic(FileManager.getImageViewFromPath(sm_icon.returnProperty("RECREATEKEY")));
        } catch (IOException ex) {
            Logger.getLogger(PropertiesController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void initLanguage() {
        lb_username.setText(language_singelton.getValue("USERNAME"));
        lb_pw.setText(language_singelton.getValue("PASSWORD"));
        lb_confirmPW.setText(language_singelton.getValue("CONFPASSWORD"));
        lb_image.setText(language_singelton.getValue("AVATAR"));
        lb_debugmode.setText(language_singelton.getValue("DEBUG"));
        chk_debug.setText(language_singelton.getValue("ACTIVATE"));
        btn_save.setText(language_singelton.getValue("SAVE"));
        btn_cancel.setText(language_singelton.getValue("CANCEL"));
        btn_createNewKey.setText(language_singelton.getValue("CREATE"));
        lb_createNewKey.setText(language_singelton.getValue("CREATENEWENCKEY"));
        encKeyTooltip.setText(language_singelton.getValue("ENCKEY"));
        debugTooltip.setText(language_singelton.getValue("DEBUGMODE"));
        errorLabel.setText(language_singelton.getValue("ERROR_PROPERTIESINVALID"));
    }

    @FXML
    public void recreateKey() {
        File structure = new File("AppData/structure.xml");
        File key = new File("AppData/private.key");

        structure = new Decryption().returnDecryptedFile(structure, structure.getAbsolutePath(), key.getAbsolutePath());

        KeyGenerationManager keyGenManager = new KeyGenerationManager();
        keyGenManager.generateAndStoreKey("AppData/private.key");

        structure = new Encryption().returnEncryptedFile(structure, structure.getAbsolutePath(), key.getAbsolutePath());
        new PageLoadHelper().loadRecreateKeyDialog();
    }

    private boolean getSelectedDebug(int debugValue) {
        if (debugValue == 1) {
            return true;
        } else {
            return false;
        }
    }

    @FXML
    private void changeStyle() {
        if (confirm_pwfield.getText().equals(pwfield.getText())) {
            confirm_pwfield.setStyle("-fx-background-color: #00FF00");
        } else {
            confirm_pwfield.setStyle("-fx-background-color: #FE2E2E");
        }
    }
}
