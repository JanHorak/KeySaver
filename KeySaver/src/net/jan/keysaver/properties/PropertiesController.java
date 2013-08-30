/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.properties;

import java.io.File;
import java.io.FileInputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.jan.keysaver.manager.LoggingManager;
import net.jan.keysaver.manager.SettingManager;
import net.jan.keysaver.beans.Language_Singleton;

import net.jan.keysaver.sources.PageLoadHelper;

/**
 * FXML Controller class
 *
 * @author Jan Horak
 */
public class PropertiesController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField tf_name;
    @FXML
    private PasswordField pwfield;
    @FXML
    private PasswordField confirm_pwfield;
    @FXML
    private ImageView statusImage;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_cancel;
    @FXML
    private ListView<Label> iconList;
    @FXML
    private CheckBox chk_debug;
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
    private Tooltip debugTooltip;
    private Image imageOK;
    private Image imageNOK;
    private String nameBuffer;
    private final String PATH_INFODIALOG = "src\\net\\jan\\keysaver\\infodialog\\InfoDialog.fxml";
    private String selectedAvatar = "";
    private String selectedInitialAvatar = "";
    SettingManager sm_main = new SettingManager("settings.ini");
    private int debug = 0;
    private int debugBuffer = 0;
    private String lang = "";
    Language_Singleton language_singelton;

    @FXML
    private void proofPW() {
        if (pwfield.getText().equals(confirm_pwfield.getText())) {
            statusImage.setImage(imageOK);
            btn_save.setDisable(false);
        } else {
            statusImage.setImage(imageNOK);
            btn_save.setDisable(true);
        }
    }

    @FXML
    private void proofName() {
        if (nameBuffer.equals(tf_name.getText())) {
            btn_save.setDisable(true);
        } else {
            btn_save.setDisable(false);
        }
    }

    @FXML
    private void proofDebug() {
        if (chk_debug.isSelected()) {
            debug = 1;
        } else {
            debug = 0;
        }
        if (debugBuffer == debug) {
            btn_save.setDisable(true);
        } else {
            btn_save.setDisable(false);
        }
    }

    @FXML
    private void save(ActionEvent actionEvent) {
        if (chk_debug.isSelected()) {
            debug = 1;
        } else {
            debug = 0;
        }
        try {
            sm_main = new SettingManager("settings.ini");
            sm_main.storeProperty("USERNAME", tf_name.getText());
            sm_main.storeProperty("MPW", confirm_pwfield.getText());
            if (selectedAvatar.equals("")) {
                sm_main.storeProperty("AVATAR", selectedInitialAvatar);
            } else {
                sm_main.storeProperty("AVATAR", "AppData\\Images\\Avatars\\" + selectedAvatar);
            }
            sm_main.storeProperty("DEBUG", String.valueOf(debug));
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile("Cant find Error-File", ex);
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        }

        //close
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        new PageLoadHelper(PATH_INFODIALOG, "Information", 343, 59);
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        language_singelton = Language_Singleton.getInstance();
        initLanguage();
        try {
            debug = Integer.decode(sm_main.returnProperty("DEBUG"));
            debugBuffer = debug;
            if (debug == 0) {
                chk_debug.setSelected(false);
            }
            if (debug == 1) {
                chk_debug.setSelected(true);
            }
        } catch (IOException ex) {
            Logger.getLogger(PropertiesController.class.getName()).log(Level.SEVERE, null, ex);
        }

        debugTooltip.setText(language_singelton.getValue("DEBUGMODE"));
        try {
            imageOK = new Image(new FileInputStream("AppData\\Images\\intern\\Ok_32x32.png"));
            imageNOK = new Image(new FileInputStream("AppData\\Images\\intern\\NOk_32x32.png"));
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile("Error at loading Images", ex);
        }
        try {
            tf_name.setText(sm_main.returnProperty("USERNAME"));
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        }
        String mpw = "";
        try {
            mpw = sm_main.returnProperty("MPW");
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        }
        try {
            selectedInitialAvatar = sm_main.returnProperty("AVATAR");
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
        }
        pwfield.setText(mpw);
        confirm_pwfield.setText(mpw);
        nameBuffer = tf_name.getText();
        ObservableList<Label> avatarList = FXCollections.observableArrayList();

        File dir = new File("AppData\\Images\\Avatars");
        String[] files = dir.list();
        int tmpcounter = 0;
        int position = 0;
        for (String s : files) {
            Label l = new Label(s);
            try {
                l.setGraphic(new ImageView(new Image(new FileInputStream("AppData\\Images\\Avatars\\" + s))));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PropertiesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (("AppData\\Images\\Avatars\\" + s).equals(selectedInitialAvatar)) {
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
                if (("AppData\\Images\\Avatars\\" + selectedAvatar).equals(selectedInitialAvatar)) {
                    btn_save.setDisable(true);
                } else {
                    proofName();
                    proofPW();
                    proofDebug();
                }
            }
        });
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
    }
}
