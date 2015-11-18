/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.jan.keysaver2.manager.SettingManager;

/**
 * FXML Controller class
 *
 * @author Jan Horak
 */
public class InfoDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private Label messageLabel;
    
    @FXML
    private ImageView imageView;
    
    @FXML
    private void cancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

       
    }    
}
