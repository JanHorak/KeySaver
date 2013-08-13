/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.mainpage;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import net.jan.keysaver.manager.ErrorManager;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.manager.SettingManager;
import net.jan.keysaver.sources.Category;
import net.jan.keysaver.sources.CategoryList;
import net.jan.keysaver.sources.EnumNotification;
import net.jan.keysaver.sources.Key;
import net.jan.keysaver.sources.PageLoadHelper;

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
    private Label lb_confpw;
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
    private CheckBox chk_useDefaultIcon;
    @FXML
    private Button btn_browse;
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
    @FXML
    private ImageView pwImage;
    @FXML
    private ImageView avatarIV;
    @FXML
    private ImageView iconPreview;
    @FXML
    private ImageView notifyImageView;
    @FXML
    private Label notifyLabel;
    @FXML
    private Hyperlink errorLogHyperlink;
    //Images
    private Image addCatImage;
    private Image trashImage;
    private Image keyImage;
    private Image settingImage;
    private Image iconsImage;
    private Image okImage;
    private Image nokImage;
    ////////
    //MenuItems
    @FXML
    MenuItem settingsItem;
    @FXML
    MenuItem iconsItem;
    /////////
    private CategoryList catList = new CategoryList();
    private FileManager fileManager = new FileManager();
    private final String PATH_PROPERTIES = "src\\net\\jan\\keysaver\\properties\\Properties.fxml";
    private final String PATH_ICONMANAGEMENT = "src\\net\\jan\\keysaver\\icondialog\\Icondialog.fxml";
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
        setControlVisible(lb_catName, tf_catName, chk_useDefaultIcon, btn_browse);
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
        setControlUnvisible(lb_catName, tf_catName, btn_browse, chk_useDefaultIcon);
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
            ImageView iViewTmp = new ImageView();
            Image image = null;
            TreeItem<String> categoryItem = new TreeItem<String>(cat.getName());
            try {
                image = new Image(new FileInputStream(cat.getIconPath()));
            } catch (FileNotFoundException ex) {
                ErrorManager.writeToErrorFile("Init-Tree() -> Iconpath for Category", ex);
                startNotification(EnumNotification.ERROR);
            }
            iViewTmp.setImage(image);
            categoryItem.setGraphic(iViewTmp);
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
        new PageLoadHelper(PATH_PROPERTIES, "Properties", 428, 221);
    }

    @FXML
    private void open_Iconsite() {
        new PageLoadHelper(PATH_ICONMANAGEMENT, "Iconmanagement", 366, 400);
    }

    @FXML
    private void openErrorFile(){
        try {
            Desktop.getDesktop().open(new File("AppData\\Error.log"));
        } catch (IOException ex) {
            ErrorManager.writeToErrorFile("openErrorFile()", ex);
            startNotification(EnumNotification.ERROR);
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
            startNotification(EnumNotification.KEY_REMOVED);
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
            startNotification(EnumNotification.CAT_REMOVED);
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
            if (chk_useDefaultIcon.isSelected()) {
                cat.setIconPath("AppData\\Images\\intern\\Folder_default_16x16.png");
            }
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
            startNotification(EnumNotification.CAT_ADDED);
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
            startNotification(EnumNotification.KEY_ADDED);
        }
    }

    @FXML
    private void validatePW() {
        if (tf_password.getText().equals(tf_passwordConfirm.getText())) {
            pwImage.setImage(okImage);
            enableControl(btn_save);
        } else {
            pwImage.setImage(nokImage);
            disableControl(btn_save);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        new FileManager().checkAvailibility();
        SettingManager sm_main = new SettingManager("settings.ini");
        SettingManager sm_icons = new SettingManager("AppData\\icons.properties");
        catList = fileManager.returnListofCategories();
        String userAvatar = sm_main.returnProperty("AVATAR");
        lb_username.setText(sm_main.returnProperty("USERNAME"));
        try {
            avatarIV.setImage(new Image(new FileInputStream(userAvatar)));
            addCatImage = new Image(new FileInputStream(sm_icons.returnProperty("ADDCAT")));
            trashImage = new Image(new FileInputStream(sm_icons.returnProperty("TRASH")));
            keyImage = new Image(new FileInputStream(sm_icons.returnProperty("ADDKEY")));
            settingImage = new Image(new FileInputStream(sm_icons.returnProperty("SETTINGS")));
            iconsImage = new Image(new FileInputStream(sm_icons.returnProperty("ICONS")));
            okImage = new Image(new FileInputStream(sm_icons.returnProperty("OK")));
            nokImage = new Image(new FileInputStream(sm_icons.returnProperty("NOK")));
        } catch (FileNotFoundException ex) {
            ErrorManager.writeToErrorFile("initialize() -> Loading Icons from icons.properties failed", ex);
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
        try {
            iconPreview.setImage(new Image(new FileInputStream("AppData\\Images\\intern\\Folder_default_16x16.png")));
        } catch (FileNotFoundException ex) {
            ErrorManager.writeToErrorFile("initialize() -> Failed to load Folder_default_16x16.png", ex);
        }
        initTree();
        lockFields();
    }

    private void startNotification(EnumNotification eNotification) {
        if (eNotification.equals(EnumNotification.ERROR)) {
            errorLogHyperlink.setGraphic(new ImageView(nokImage));
            errorLogHyperlink.setText("There was an Error! - click here!");
            fadeIn(errorLogHyperlink);
            fadeOut(errorLogHyperlink);
        }
        if (eNotification.equals(EnumNotification.CAT_ADDED)) {
            notifyLabel.setGraphic(new ImageView(okImage));
            notifyLabel.setText("Category is added");
            fadeIn(notifyLabel);
            fadeOut(notifyLabel);
        }
        if (eNotification.equals(EnumNotification.CAT_REMOVED)) {
            notifyLabel.setGraphic(new ImageView(okImage));
            notifyLabel.setText("Category is removed");
            fadeIn(notifyLabel);
            fadeOut(notifyLabel);
        }
        if (eNotification.equals(EnumNotification.KEY_ADDED)) {
            notifyLabel.setGraphic(new ImageView(okImage));
            notifyLabel.setText("Category is added");
            fadeIn(notifyLabel);
            fadeOut(notifyLabel);
        }
        if (eNotification.equals(EnumNotification.KEY_REMOVED)) {
            notifyLabel.setGraphic(new ImageView(okImage));
            notifyLabel.setText("Category is removed");
            fadeIn(notifyLabel);
            fadeOut(notifyLabel);
        }
    }

    private void fadeIn(Control con) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), con);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private void fadeOut(Control con) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(5500), con);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    @FXML
    private void selectDefaultCheckBox() {
        if (chk_useDefaultIcon.isSelected()) {
            disableControl(btn_browse);
        } else {
            enableControl(btn_browse);
        }
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
