/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.mainpage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import net.jan.keysaver.manager.ErrorManager;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.manager.SettingManager;
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
    private Label lb_username;
    @FXML
    private Label lb_catName;
    @FXML
    private TextField tf_description;
    @FXML
    private TextField tf_username;
    @FXML
    private TextField tf_password;
    @FXML
    private TextField tf_passwordConfirm;
    @FXML
    private TextField tf_catName;
    @FXML
    private TreeView tree;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_addCat;
    private Image addCatImage;
    private CategoryList catList = new CategoryList();
    private FileManager fileManager = new FileManager();
    private final String PATH_PROPERTIES = "src\\net\\jan\\keysaver\\properties\\Properties.fxml";
    private SelectionModel model;
    private ContextMenu conMenu = new ContextMenu();
    private Key selectedKey = new Key();
    private TreeItem selectedItem = new TreeItem();
    private boolean modCat = false;

    @FXML
    private void edit() {
        if (selectedItem.isLeaf()) {
            unlockFields();
            lockTree();
            btn_cancel.setDisable(false);
            btn_save.setDisable(false);
            btn_edit.setDisable(true);
        }
        if (!selectedItem.isLeaf()) {
            lockTree();
            lockFields();
            lb_catName.setVisible(true);
            tf_catName.setVisible(true);
            btn_save.setDisable(false);
            btn_addCat.setDisable(true);
            btn_cancel.setDisable(false);
            btn_edit.setDisable(true);
            tf_catName.setText(selectedItem.getValue().toString());
            modCat = true;
        }
    }

    @FXML
    private void editCancel() {
        lockFields();
        unlockTree();
        btn_cancel.setDisable(true);
        btn_edit.setDisable(true);
        btn_save.setDisable(true);
        lb_catName.setVisible(false);
        tf_catName.setVisible(false);
        lb_catName.setVisible(false);
        tf_catName.setVisible(false);
        modCat = false;
    }

    private void initTree() {
        TreeItem<String> rootItem = new TreeItem<String>("Categories", null);
        rootItem.setExpanded(true);
        for (Category cat : catList.getCategoryList()) {
            TreeItem<String> categoryItem = new TreeItem<String>(cat.getName());
            for (Key k : cat.getKeylist()) {
                TreeItem<String> keyItem = new TreeItem<String>(k.getKeyname());
                categoryItem.getChildren().add(keyItem);
            }
            rootItem.getChildren().add(categoryItem);
        }
        tree.setRoot(rootItem);
        model = tree.getSelectionModel();
        tree.setEditable(true);
        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (ov != null || t != null || t1 != null) {
                    selectedItem = (TreeItem) ov.getValue();
                    if (selectedItem.isLeaf()) {
                        selectedKey = new FileManager().returnKey(selectedItem.getValue().toString());
                        tf_keyname.setText(selectedKey.getKeyname());
                        tf_username.setText(selectedKey.getUsername());
                        tf_description.setText(selectedKey.getDescription());
                        tf_password.setText(selectedKey.getPassword());
                        tf_passwordConfirm.setText(selectedKey.getPassword());
                        btn_edit.setDisable(false);
                        btn_addCat.setDisable(true);
                    } else {
                        btn_edit.setDisable(false);
                        btn_addCat.setDisable(false);
                        resetFields();
                    }
                }
            }
        });

    }

    private void resetFields() {
        tf_keyname.setText("");
        tf_username.setText("");
        tf_description.setText("");
        tf_password.setText("");
        tf_passwordConfirm.setText("");
    }

    private void lockFields() {
        tf_keyname.setDisable(true);
        tf_username.setDisable(true);
        tf_description.setDisable(true);
        tf_password.setDisable(true);
        tf_passwordConfirm.setDisable(true);
    }

    private void unlockFields() {
        tf_keyname.setDisable(false);
        tf_username.setDisable(false);
        tf_description.setDisable(false);
        tf_password.setDisable(false);
        tf_passwordConfirm.setDisable(false);
    }

    private void lockTree() {
        tree.setDisable(true);
    }

    private void unlockTree() {
        tree.setDisable(false);
    }

    @FXML
    private void open_Properties() {
        loadPage(PATH_PROPERTIES, "Properties", 428, 221);
    }

    private void loadPage(String pathString, String title, double width, double height) {
        Parent root;
        try {
            root = FXMLLoader.load(new File(pathString).toURL());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root, width, height));
            stage.show();

        } catch (IOException e) {
            ErrorManager.writeToErrorFile(null, e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        catList = fileManager.returnListofCategories();
        lb_username.setText(new SettingManager().returnProperty("USERNAME"));
        try {
            addCatImage = new Image(new FileInputStream("AppData\\Images\\intern\\addCat_16x16.png"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        btn_addCat.setGraphic(new ImageView(addCatImage));
        initTree();
        lockFields();
    }

    @FXML
    private void prepareAddCat() {
        lb_catName.setVisible(true);
        tf_catName.setVisible(true);
        btn_cancel.setDisable(false);
        lockTree();
    }
}
