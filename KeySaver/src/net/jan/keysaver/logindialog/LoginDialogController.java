/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.logindialog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import net.jan.keysaver.manager.SettingManager;

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
    private String pw ="";
    @FXML
    private Button loginButton;
    
    
    @FXML
    private void login(ActionEvent actionEvent) {
        if (pwField.getText().equals(pw)) {
            loadMainPage();
            //close the Window
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }
    
    private void loadMainPage() {
        Parent root;
        try {
            root = FXMLLoader.load(new File("src\\net\\jan\\keysaver\\mainpage\\Mainpage.fxml").toURL());
            Stage stage = new Stage();
            stage.setTitle("KeySaver2.0");
            stage.setScene(new Scene(root, 612, 464));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image(new File("AppData\\Images\\Logo_key.png").toURI().toString());
        imageView.setImage(image);
        pw = new SettingManager().returnProperty("MPW");
        pwField.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent t) {
                if ( t.getCode().equals(KeyCode.ENTER) ){
                    
                }
            }
        });
    }
}
