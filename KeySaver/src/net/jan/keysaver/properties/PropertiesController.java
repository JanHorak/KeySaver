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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.jan.keysaver.manager.ErrorManager;
import net.jan.keysaver.manager.SettingManager;
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
    private Button saveButton;
    @FXML
    private ListView<Label> iconList;
    
    private Image imageOK;
    private Image imageNOK;
    private String nameBuffer;
    private final String PATH_INFODIALOG = "src\\net\\jan\\keysaver\\infodialog\\InfoDialog.fxml";
    private String selectedAvatar = ""; 
    private String selectedInitialAvatar ="";
    
    
    @FXML
    private void proofPW() {
        if (pwfield.getText().equals(confirm_pwfield.getText())) {
            statusImage.setImage(imageOK);
            saveButton.setDisable(false);
        } else {
            statusImage.setImage(imageNOK);
            saveButton.setDisable(true);
        }
    }

    @FXML
    private void proofName() {
        if (nameBuffer.equals(tf_name.getText())) {
            saveButton.setDisable(true);
        } else {
            saveButton.setDisable(false);
        }
    }

    @FXML
    private void save(ActionEvent actionEvent) {
        try {
            SettingManager sm = new SettingManager("settings.ini");
            sm.storeProperty("USERNAME", tf_name.getText());
            sm.storeProperty("MPW", confirm_pwfield.getText());
            sm.storeProperty("AVATAR", "AppData\\Images\\Avatars\\"+selectedAvatar);
        } catch (FileNotFoundException ex) {
            ErrorManager.writeToErrorFile("Cant find Error-File", ex);
        } catch (IOException ex) {
            ErrorManager.writeToErrorFile(null, ex);
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
        SettingManager sm = new SettingManager("settings.ini");
        try {
            imageOK = new Image(new FileInputStream("AppData\\Images\\intern\\Ok_32x32.png"));
            imageNOK = new Image(new FileInputStream("AppData\\Images\\intern\\NOk_32x32.png"));
        } catch (FileNotFoundException ex) {
            ErrorManager.writeToErrorFile("Error at loading Images", ex);
        }
        tf_name.setText(sm.returnProperty("USERNAME"));
        String mpw = sm.returnProperty("MPW");
        selectedInitialAvatar = sm.returnProperty("AVATAR");
        pwfield.setText(mpw);
        confirm_pwfield.setText(mpw);
        nameBuffer = tf_name.getText();
        ObservableList<Label> avatarList = FXCollections.observableArrayList();
        
        File dir = new File("AppData\\Images\\Avatars");
        String[] files = dir.list();
        int tmpcounter = 0;
        int position = 0;
        for (String s : files ){
            Label l = new Label(s);
            try {
                l.setGraphic(new ImageView(new Image(new FileInputStream("AppData\\Images\\Avatars\\" + s))));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PropertiesController.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (("AppData\\Images\\Avatars\\" + s).equals(selectedInitialAvatar)){
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
                if (("AppData\\Images\\Avatars\\" + selectedAvatar).equals(selectedInitialAvatar)){
                    saveButton.setDisable(true);
                }else{
                    proofName();
                    proofPW();
                }
            }
        });
    }
    
}
