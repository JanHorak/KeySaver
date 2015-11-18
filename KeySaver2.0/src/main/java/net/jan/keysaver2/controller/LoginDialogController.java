/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Jan Horak
 */
public class LoginDialogController implements Initializable {

    private final static String KEYSAVER_LOGO_PNG = "images/Logo_key.png";
    
    @FXML
    private ImageView imageView;
    
    @FXML
    private PasswordField pwField;
    
    @FXML
    private Label errorLabel;

    @FXML
    private void login(Event actionEvent) {
    }

    private void close(Event e) {
        Node source = (Node) e.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image(getClass().getClassLoader().getResource(KEYSAVER_LOGO_PNG).toString());
        imageView.setImage(image);
    }
}
