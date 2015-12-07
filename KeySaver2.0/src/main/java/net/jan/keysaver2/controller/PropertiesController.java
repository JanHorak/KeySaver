/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.jan.keysaver2.manager.LoggingManager;
import net.jan.keysaver2.manager.SettingManager;
import net.jan.keysaver2.manager.ValidationManager;

import net.jan.keysaver2.sources.PageLoadHelper;
import net.jan.keysaver2.sources.Utilities;
import net.jan.keysaver2.validationentities.PropertiesEntity;

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
    @FXML
    private Button btn_uploadImage;
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
    @FXML
    private Label lb_uploadOwnImage;
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

    // ==== END OF ATTRIBUTES ====
    @FXML
    private void save(ActionEvent actionEvent) {
        sm_main = new SettingManager("settings.ini");
        String bufferedUserName = "unknown";
        String bufferedPWHash = "unknown";
        errorLabel.setVisible(false);
        boolean changed = false;
        try {
            bufferedUserName = sm_main.returnProperty("USERNAME");
            bufferedPWHash = sm_main.returnProperty("MPW");
        } catch (IOException ex) {
            Logger.getLogger(PropertiesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (chk_debug.isSelected()) {
            debug = 1;
        } else {
            debug = 0;
        }
        try {
            if (!bufferedUserName.equals(tf_name.getText()) && !tf_name.getText().isEmpty()) {
                sm_main.storeProperty("USERNAME", tf_name.getText());
                changed = true;
            }
            if (!Utilities.getHash(confirm_pwfield.getText()).equals(bufferedPWHash) && !confirm_pwfield.getText().isEmpty()) {
                sm_main.storeProperty("MPW", Utilities.getHash(confirm_pwfield.getText()).trim());
                changed = true;
            }

            if (selectedAvatar.equals("")) {
                sm_main.storeProperty("AVATAR", selectedInitialAvatar);

            } else {
                sm_main.storeProperty("AVATAR", "AppData/Images/Avatars/" + selectedAvatar);
                changed = true;
            }
            sm_main.storeProperty("DEBUG", String.valueOf(debug));
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile("Cant find Error-File", ex);
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        }

        PropertiesEntity p = new PropertiesEntity();
        p.setUserName(tf_name.getText());
        p.setPassword(pwfield.getText());
        p.setPassword_confirm(confirm_pwfield.getText());
        if (changed && ValidationManager.isValid(p)) {
            new PageLoadHelper().loadInfoRestartDialog();
            //closeEvent
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
        if (!ValidationManager.isValid(p)) {
            errorLabel.setVisible(true);
        }
        if (!changed) {
            //closeEvent
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
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
        initLanguage();
        try {
            debug = Integer.decode(sm_main.returnProperty("DEBUG"));
            chk_debug.setSelected(getSelectedDebug(debug));
        } catch (IOException ex) {
            Logger.getLogger(PropertiesController.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
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
        updateAvatarList();

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

//            lb_createNewKey.setGraphic(FileManager.getImageViewFromPath(sm_icon.returnProperty("RECREATEKEY")));
    }

    private void initLanguage() {

    }

    @FXML
    public void recreateKey() {
        File structure = new File("AppData/structure.xml");
        File key = new File("AppData/private.key");
//
//        structure = new Decryption().returnDecryptedFile(structure, structure.getAbsolutePath(), key.getAbsolutePath());
//
//        KeyGenerationManager keyGenManager = new KeyGenerationManager();
//        keyGenManager.generateAndStoreKey("AppData/private.key");
//
//        structure = new Encryption().returnEncryptedFile(structure, structure.getAbsolutePath(), key.getAbsolutePath());
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

    @FXML
    private void uploadImage() {
        File file4Upload;

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Upload own");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Avatars", "*.bmp", "*.gif", "*.jpg", "*.png"));
        file4Upload = chooser.showOpenDialog(null);
        if (file4Upload != null) {
            try {
                Files.copy(file4Upload.getAbsoluteFile().toPath(), new File("AppData/Images/Avatars/" + file4Upload.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                Logger.getLogger(PropertiesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateAvatarList();
        }

    }

    private void updateAvatarList() {
        ObservableList<Label> avatarList = FXCollections.observableArrayList();

        File dir = new File("AppData/Images/Avatars");
        String[] files = dir.list();
        int tmpcounter = 0;
        int position = 0;
        for (String s : files) {
            Label l = new Label(s);
//            l.setGraphic(FileManager.getImageViewFromPath("AppData/Images/Avatars/" + s));
            if (("AppData/Images/Avatars/" + s).equals(selectedInitialAvatar)) {
                position = tmpcounter;
            }
            l.getGraphic().setScaleX(0.8);
            l.getGraphic().setScaleY(0.8);
            avatarList.add(l);
            tmpcounter++;

        }
        iconList.setItems(avatarList);
        iconList.getSelectionModel().select(position);
    }
}
