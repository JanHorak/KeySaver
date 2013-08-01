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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.RotateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
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
    @FXML
    private Button btn_trash;
    @FXML
    private Button btn_addKey;
    //Images
    private Image addCatImage;
    private Image trashImage;
    private Image keyImage;
    private Image settingImage;
    private Image iconsImage;
    ////////
    
    //MenuItems
    @FXML
    MenuItem settingsItem;
    @FXML
    MenuItem iconsItem;
    
    
    private CategoryList catList = new CategoryList();
    private FileManager fileManager = new FileManager();
    private final String PATH_PROPERTIES = "src\\net\\jan\\keysaver\\properties\\Properties.fxml";
    private SelectionModel model;
    private ContextMenu conMenu = new ContextMenu();
    private Key selectedKey = new Key();
    private TreeItem selectedItem = new TreeItem();
    private boolean addCat = false;
    private boolean editKey = false;
    private boolean addKey = false;
    private boolean editCat = false;
    private String tmpBuffer = "";
    private Key keyBuffer = new Key();
    private Key editedKey = new Key();
    private String selectedCat = "";
    private boolean isKeySelected = false;
    private boolean isCatSelected = false;

    @FXML
    private void edit() {
        tmpBuffer = selectedItem.getValue().toString();
        if (selectedItem.isLeaf()) {
            unlockFields();
            lockTree();
            disableControl(btn_edit, btn_trash);
            enableControl(btn_cancel, btn_save);
            keyBuffer = new FileManager().returnKey(tmpBuffer);
            editKey = true;
            System.out.println("editKey:" + editKey);
        }
        if (!selectedItem.isLeaf()) {
            lockTree();
            lockFields();
            setControlVisible(lb_catName, tf_catName);
            disableControl(btn_addCat, btn_edit, btn_trash);
            enableControl(btn_save, btn_cancel);
            tf_catName.setText(tmpBuffer);
            editCat = true;
            System.out.println("editCat:" + editCat);
        }
    }

    @FXML
    private void prepareAddCat() {
        setControlVisible(lb_catName, tf_catName);
        disableControl(btn_edit, btn_addCat, btn_trash, btn_addKey);
        enableControl(btn_cancel, btn_save);
        btn_save.setText("Add");
        addCat = true;
        System.out.println("AddCat: " + addCat);
        lockTree();
    }

    @FXML
    private void prepareAddKey() {
        disableControl(btn_edit, btn_addCat, btn_trash, btn_addKey);
        // Buttons
        enableControl(btn_cancel, btn_save);
        // Textfields
        enableControl(tf_keyname, tf_description, tf_password, tf_passwordConfirm, tf_username);
        btn_save.setText("Save");
        addKey = true;
        System.out.println("AddKey: " + addKey);
        lockTree();
    }

    @FXML
    private void editCancel() {
        lockFields();
        unlockTree();
        setControlUnvisible(lb_catName, tf_catName);
        disableControl(btn_edit, btn_save, btn_addCat, btn_addKey, btn_cancel, btn_trash);
        btn_save.setText("Save");
        tf_catName.setText("");
        addCat = false;
        editKey = false;
        addKey = false;
        editCat = false;
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

    }

    private void resetFields() {
        tf_keyname.setText("");
        tf_username.setText("");
        tf_description.setText("");
        tf_password.setText("");
        tf_passwordConfirm.setText("");
    }

    private void lockFields() {
        disableControl(tf_keyname, tf_username, tf_description, tf_password, tf_passwordConfirm);
    }

    private void unlockFields() {
        enableControl(tf_keyname, tf_username, tf_description, tf_password, tf_passwordConfirm);
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

    @FXML
    private void remove() {
        boolean stop = false;
        if (isKeySelected) {
            List<Category> cat = catList.getCategoryList();

            for (Category c : cat) {
                List<Key> keyList = c.getKeylist();
                for (Key k : keyList) {
                    if (stop) {
                        break;
                    }
                    if (k.getKeyname().equals(selectedKey.getKeyname())) {
                        if (keyList.size() == 1) {
                            cat.remove(c);

                            stop = true;
                            break;
                        } else {
                            keyList.remove(k);
                            c.setKeylist(keyList);
                        }
                    }
                }
                if (stop) {
                    break;
                }
            }
            catList.setCategoryList(cat);
            new FileManager().saveStructure(catList);
            initTree();
            editCancel();
        }
        if (isCatSelected) {
            List<Category> cat = catList.getCategoryList();
            for (Category c : cat) {
                if (stop) {
                    break;
                }
                if (c.getName().equals(selectedCat)) {
                    cat.remove(c);
                    stop = true;
                    break;
                }
            }
            new FileManager().saveStructure(catList);
            initTree();
            editCancel();
        }
    }

    @FXML
    private void save() {
        //new Category
        if (addCat) {
            //VALIDATION IS MISSING
            String catname = tf_catName.getText();
            Category cat = new Category();
            cat.setName(catname);

            Key k = new Key();
            List<Key> keyList = new ArrayList<Key>();
            k.setKeyname("new Key");
            k.setDescription("unknown");
            k.setPassword("unknown");
            k.setUsername("unknown");
            keyList.add(k);
            cat.setKeylist(keyList);
            List<Category> categoryList = new ArrayList<>();
            categoryList = catList.getCategoryList();
            categoryList.add(cat);
            catList.setCategoryList(categoryList);
            new FileManager().saveStructure(catList);
            initTree();
            editCancel();
        }
        // Change existing name of Cat
        if (editCat) {
            //VALIDATION IS MISSING
            for (Category cat : catList.getCategoryList()) {
                if (cat.getName().equals(tmpBuffer)) {
                    cat.setName(tf_catName.getText());
                    new FileManager().saveStructure(catList);
                    initTree();
                    editCancel();
                    break;
                }
            }

        }

        if (editKey) {
            Key oldKey = keyBuffer;
            editedKey.setKeyname(tf_keyname.getText());
            editedKey.setDescription(tf_description.getText());
            editedKey.setPassword(tf_passwordConfirm.getText());
            editedKey.setUsername(tf_username.getText());

            List<Category> cat = catList.getCategoryList();
            boolean stop = false;
            //VALIDATION IS MISSING
            for (Category c : cat) {
                List<Key> keyList = c.getKeylist();
                for (Key k : keyList) {
                    if (stop) {
                        break;
                    }
                    if (k.getKeyname().equals(oldKey.getKeyname())) {
                        k.overwriteKey(editedKey);
                        c.setKeylist(keyList);
                        stop = true;
                    }
                }

            }
            catList.setCategoryList(cat);
            new FileManager().saveStructure(catList);
            initTree();
            editCancel();
        }

        if (addKey) {
            Key k = new Key();
            k.setKeyname(tf_keyname.getText());
            k.setDescription(tf_description.getText());
            k.setPassword(tf_passwordConfirm.getText());
            k.setUsername(tf_username.getText());

            List<Category> cat = catList.getCategoryList();
            boolean stop = false;
            for (Category c : cat) {
                if (stop) {
                    break;
                }
                if (c.getName().equals(selectedCat)) {
                    c.getKeylist().add(k);
                    stop = true;
                }
            }
            //VALIDATION IS MISSING
            catList.setCategoryList(cat);
            new FileManager().saveStructure(catList);
            initTree();
            editCancel();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new FileManager().checkAvailibility();
        catList = fileManager.returnListofCategories();
        lb_username.setText(new SettingManager().returnProperty("USERNAME"));
        try {
            addCatImage = new Image(new FileInputStream("AppData\\Images\\intern\\addCat_16x16.png"));
            trashImage = new Image(new FileInputStream("AppData\\Images\\intern\\trash_16x16.png"));
            keyImage = new Image(new FileInputStream("AppData\\Images\\intern\\Key_8x16.png"));
            settingImage = new Image(new FileInputStream("AppData\\Images\\intern\\Settings_16x16.png"));
            iconsImage = new Image(new FileInputStream("AppData\\Images\\intern\\IconManagement_16x16.png"));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
        }
        btn_addCat.setGraphic(new ImageView(addCatImage));
        btn_trash.setGraphic(new ImageView(trashImage));
        btn_addKey.setGraphic(new ImageView(keyImage));
        settingsItem.setGraphic(new ImageView(settingImage));
        iconsItem.setGraphic(new ImageView(iconsImage));
        
        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (ov != null || t != null || t1 != null) {
                    selectedItem = (TreeItem) ov.getValue();
                    if (selectedItem != null) {
                        btn_trash.setDisable(false);
                        if (selectedItem.isLeaf()) {
                            selectedKey = new FileManager().returnKey(selectedItem.getValue().toString());
                            tf_keyname.setText(selectedKey.getKeyname());
                            tf_username.setText(selectedKey.getUsername());
                            tf_description.setText(selectedKey.getDescription());
                            tf_password.setText(selectedKey.getPassword());
                            tf_passwordConfirm.setText(selectedKey.getPassword());
                            btn_edit.setDisable(false);
                            btn_addCat.setDisable(true);
                            btn_addKey.setDisable(true);
                            isCatSelected = false;
                            isKeySelected = true;
                        } else {
                            btn_edit.setDisable(false);
                            btn_addCat.setDisable(false);
                            btn_addKey.setDisable(false);
                            selectedCat = selectedItem.getValue().toString();
                            isCatSelected = true;
                            isKeySelected = false;
                            resetFields();
                        }
                    }
                }
            }
        });

        initTree();
        lockFields();
    }

    private void disableControl(Control... c) {
        for (Control b : c) {
            b.setDisable(true);
        }
    }

    private void enableControl(Control... c) {
        for (Control b : c) {
            b.setDisable(false);
        }
    }

    private void setControlVisible(Control... c) {
        for (Control b : c) {
            b.setVisible(true);
        }
    }

    private void setControlUnvisible(Control... c) {
        for (Control b : c) {
            b.setVisible(false);
        }
    }
}
