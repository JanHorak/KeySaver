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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.jan.keysaver.manager.ErrorManager;
import net.jan.keysaver.manager.SettingManager;

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
    private Image imageOK;
    private Image imageNOK;
    private String nameBuffer;
    private final String PATH_INFODIALOG = "src\\net\\jan\\keysaver\\infodialog\\InfoDialog.fxml";
    
    

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
            SettingManager sm = new SettingManager();
            sm.storeProperty("USERNAME", tf_name.getText());
            sm.storeProperty("MPW", confirm_pwfield.getText());
        } catch (FileNotFoundException ex) {
            ErrorManager.writeToErrorFile("Cant find Error-File", ex);
        } catch (IOException ex) {
            ErrorManager.writeToErrorFile(null, ex);
        }

        //close
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
        
        loadPage(PATH_INFODIALOG, "Information", 343, 59);
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        SettingManager sm = new SettingManager();
        try {
            imageOK = new Image(new FileInputStream("AppData\\Images\\intern\\Ok_32x32.png"));
            imageNOK = new Image(new FileInputStream("AppData\\Images\\intern\\NOk_32x32.png"));
        } catch (FileNotFoundException ex) {
            ErrorManager.writeToErrorFile("Error at loading Images", ex);
        }
        tf_name.setText(sm.returnProperty("USERNAME"));
        String mpw = sm.returnProperty("MPW");
        pwfield.setText(mpw);
        confirm_pwfield.setText(mpw);
        nameBuffer = tf_name.getText();
    }
    
    private void loadPage(String pathString, String title, double width, double height ) {
        Parent root;
        try {
            root = FXMLLoader.load(new File(pathString).toURL());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
