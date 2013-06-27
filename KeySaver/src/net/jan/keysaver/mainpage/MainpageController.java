/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.mainpage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;

/**
 *
 * @author Jan Horak
 */
public class MainpageController implements Initializable {

    @FXML
    private TextField tf_keyname;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_description;
    @FXML
    private PasswordField tf_password;
    @FXML
    private PasswordField tf_passwordConfirm;
    @FXML 
    private TreeView tree;
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }
}
