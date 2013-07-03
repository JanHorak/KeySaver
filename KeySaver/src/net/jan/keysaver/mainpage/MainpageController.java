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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.VBox;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.sources.Category;
import net.jan.keysaver.sources.CategoryList;
import net.jan.keysaver.sources.Key;

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

    private CategoryList catList = new CategoryList();
    private FileManager fileManager = new FileManager();
    
    private void initTree(){
        TreeItem<String> rootItem = new TreeItem<String> ("Categories", null);
        rootItem.setExpanded(true);
        for (Category cat : catList.getCategoryList()) {
            TreeItem<String> categoryItem = new TreeItem<String> (cat.getName());
            for ( Key k : cat.getKeylist() ){
                TreeItem<String> keyItem = new TreeItem<String> (k.getKeyname());
                categoryItem.getChildren().add(keyItem);
            }
            rootItem.getChildren().add(categoryItem);
        }
        tree.setRoot(rootItem);
    }
    
    @FXML
    private void onKeySelected(){
        
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        catList = fileManager.returnListofCategories();
        initTree();
        
        
        
        
    }
}
