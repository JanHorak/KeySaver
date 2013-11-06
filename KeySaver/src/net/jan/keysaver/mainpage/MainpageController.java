/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.mainpage;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import net.jan.keysaver.manager.LoggingManager;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.manager.SettingManager;
import net.jan.keysaver.sources.Category;
import net.jan.keysaver.sources.CategoryList;
import net.jan.keysaver.sources.EnumNotification;
import net.jan.keysaver.sources.Key;
import net.jan.keysaver.beans.Language_Singleton;
import net.jan.keysaver.beans.Settings_Singelton;
import net.jan.keysaver.dialogs.icondialog.IcondialogController;
import net.jan.keysaver.manager.ValidationManager;
import net.jan.keysaver.properties.PropertiesController;
import net.jan.keysaver.sources.PageLoadHelper;

/**
 *
 * @author Jan Horak
 */
public class MainpageController implements Initializable {

    @FXML
    private TextField tf_keyname;
    //Labels
    @FXML
    private Label lb_dynamicUserName;
    @FXML
    private Label lb_username;
    @FXML
    private Label lb_catName;
    @FXML
    private Label lb_confpw;
    @FXML
    private Label lb_keyname;
    @FXML
    private Label lb_description;
    @FXML
    private Label lb_password;
    //Special Labels
    @FXML
    private Label notifyLabel;
    @FXML
    private Label pathLabelCatIcon;
    @FXML
    private Label pathLabelKeyIcon;
    ////////
    //Textfields
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
    ///////
    @FXML
    private TreeView tree;
    @FXML
    private CheckBox chk_useDefaultCatIcon;
    @FXML
    private CheckBox chk_useDefaultKeyIcon;
    //Buttons
    @FXML
    private Button btn_browseCatIcon;
    @FXML
    private Button btn_browseKeyIcon;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_edit;
    @FXML
    private Button btn_addCat;
    @FXML
    private Button btn_remove;
    @FXML
    private Button btn_addKey;
    //////////
    //Menubar / MenuItems
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu fileMenu;
    @FXML
    private Menu editMenu;
    @FXML
    private Menu helpMenu;
    //MenuItems
    @FXML
    private MenuItem settingsItem;
    @FXML
    private MenuItem helpItem;
    @FXML
    private MenuItem export_fileItem;
    @FXML
    private MenuItem iconsItem;
    @FXML
    private MenuItem languageItem;
    @FXML
    private MenuItem exitItem;
    @FXML
    private CheckBox chk_englishLang;
    @FXML
    private CheckBox chk_germanLang;
    /////////
    @FXML
    private ImageView avatarIV;
    @FXML
    private ImageView iconCatPreview;
    @FXML
    private ImageView iconKeyPreview;
    @FXML
    private ImageView notifyImageView;
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
    private Image warningImage;
    ////////
    //Panes
    @FXML
    private TitledPane keyPane;
    @FXML
    private TitledPane notificationPane;
    @FXML
    private TitledPane actionPane;
    private CategoryList catList = new CategoryList();
    private FileManager fileManager = new FileManager();
    private final String PATH_PROPERTIES = "Properties.fxml";
    private final String PATH_ICONMANAGEMENT = "Icondialog.fxml";
    private SelectionModel model;
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
    private String selectedLanguage = "unknown";
    private SettingManager sm_icons;
    private int debug = 0;
    private String addString = "";
    private String addString2 = "";
    Language_Singleton languageBean;
    Settings_Singelton settingsBean;
    @FXML
    private Tooltip errorTooltip;

    @FXML
    private void edit() {
        tmpBuffer = selectedItem.getValue().toString();
        if (selectedItem.isLeaf()) {
            unlockFields();
            lockTree();
            disableControl(btn_edit, btn_remove);
            enableControl(btn_cancel, btn_save);
            keyBuffer = new FileManager().returnKey(tmpBuffer);
            editKey = true;
        }
        if (!selectedItem.isLeaf()) {
            lockTree();
            lockFields();
            Category selectedLocalCat = new FileManager().returnSingleCategory(tmpBuffer);
            setControlVisible(lb_catName, tf_catName);
            disableControl(btn_addCat, btn_edit, btn_remove, btn_addKey);
            setControlVisible(btn_browseCatIcon, chk_useDefaultCatIcon);
            chk_useDefaultCatIcon.setDisable(false);
            iconCatPreview.setDisable(false);
            enableControl(btn_save, btn_cancel);

            tf_catName.setText(selectedLocalCat.getName());
            pathLabelCatIcon.setText(selectedLocalCat.getIconPath());
            selectedLocalCat = null;
            editCat = true;
        }
    }

    @FXML
    private void prepareAddCat() {
        setControlVisible(lb_catName, tf_catName, chk_useDefaultCatIcon, btn_browseCatIcon);
        disableControl(btn_edit, btn_addCat, btn_remove, btn_addKey);
        enableControl(btn_cancel, btn_save);
        btn_save.setText(addString2);
        addCat = true;
        if (iconCatPreview.getImage() == null) {
            try {
                iconCatPreview.setImage(FileManager.getImageFromPath(sm_icons.returnProperty("FOLDER_DEFAULT")));

            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("initialize() -> Failed to load Folder_default_16x16.png", ex);
                startNotification(EnumNotification.ERROR);
            } catch (IOException ex) {
                Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            iconCatPreview.setVisible(true);
        }
        lockTree();
    }

    @FXML
    private void prepareAddKey() {
        disableControl(btn_edit, btn_addCat, btn_remove, btn_addKey);
        // Buttons
        enableControl(btn_cancel, btn_save);
        // Textfields
        enableControl(tf_keyname, tf_description, tf_password, tf_passwordConfirm,
                tf_username, chk_useDefaultKeyIcon);
        chk_useDefaultKeyIcon.setSelected(true);
        try {
            String prop = sm_icons.returnProperty("KEYINTREE");
            iconKeyPreview.setImage(FileManager.getImageFromPath(prop));
            pathLabelKeyIcon.setText(prop);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
        }

        btn_save.setText(addString);
        addKey = true;
        System.out.println("AddKey detected - " + addKey);
        lockTree();
    }

    @FXML
    private void editCancel() {
        lockFields();
        unlockTree();
        setControlUnvisible(lb_catName, tf_catName, btn_browseCatIcon, chk_useDefaultCatIcon);
        disableControl(btn_edit, btn_save, btn_addCat, btn_addKey, btn_cancel, btn_remove);
        btn_save.setText(addString);
        tf_catName.setText("");
        addCat = false;
        editKey = false;
        addKey = false;
        editCat = false;
        chk_useDefaultCatIcon.setSelected(true);
        tf_catName.setText("");
        btn_browseCatIcon.setDisable(true);
        pathLabelCatIcon.setText("");
        pathLabelCatIcon.setDisable(true);
        btn_browseKeyIcon.setDisable(true);
        pathLabelKeyIcon.setText("");
        pathLabelKeyIcon.setDisable(true);
        iconCatPreview.setImage(null);
        iconKeyPreview.setImage(null);
        //EmptyString resets to Default 
        tf_passwordConfirm.setStyle("");
    }

    private void initTree() {
        TreeItem<String> rootItem = new TreeItem<String>("Categories", null);
        rootItem.setExpanded(true);
        for (Category cat : catList.getCategoryList()) {
            TreeItem<String> categoryItem = new TreeItem<String>(cat.getName());
            categoryItem.setGraphic(FileManager.getImageViewFromPath(cat.getIconPath()));
            for (Key k : cat.getKeylist()) {
                TreeItem<String> keyItem = new TreeItem<String>(k.getKeyname());
                keyItem.setGraphic(FileManager.getImageViewFromPath(k.getIconPath()));
                categoryItem.getChildren().add(keyItem);
            }
            rootItem.getChildren().add(categoryItem);
        }
        tree.setRoot(rootItem);
        model = tree.getSelectionModel();
        tree.setEditable(true);
    }

    @FXML
    private void remove() {
        boolean live = true;
        if (isKeySelected) {
            List<Category> cat = catList.getCategoryList();
            for (Category c : cat) {
                List<Key> keyList = c.getKeylist();
                for (int i = 0; i < keyList.size() && live; i++) {
                    if (keyList.get(i).getKeyname().equals(selectedKey.getKeyname())) {
                        if (keyList.size() == 1) {
                            cat.remove(c);
                        } else {
                            keyList.remove(i);
                            c.setKeylist(keyList);
                            live = false;
                        }
                    }
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
            if (cat.size() > 1) {
                for (int i = 0; i < cat.size() && live; i++) {
                    Category currentCategory = cat.get(i);
                    if (currentCategory.getName().equals(selectedCat)) {
                        cat.remove(currentCategory);
                        new FileManager().saveStructure(catList);
                        initTree();
                        editCancel();
                        startNotification(EnumNotification.CAT_REMOVED);
                        live = false;
                    }
                }
            } else {
                startNotification(EnumNotification.WARNING_LASTCAT);
                LoggingManager.writeToErrorFile("WARNING!! \nList of Categories may not be empty!", null);
            }
        }
    }

    @FXML
    private void save() throws IOException {
        //new Category
        if (addCat) {
            Category cat = new Category();
            cat.setName(tf_catName.getText().trim());
            if (chk_useDefaultCatIcon.isSelected()) {
                cat.setIconPath(sm_icons.returnProperty("FOLDER_DEFAULT"));
            } else {
                cat.setIconPath(pathLabelCatIcon.getText());
            }
            iconCatPreview.setVisible(true);
            Key k = new Key();
            List<Key> keyList = new ArrayList<Key>();
            k.setKeyname("new Key");
            k.setDescription("unknown");
            k.setIconPath(sm_icons.returnProperty("KEYINTREE"));
            k.setPassword("unknown");
            k.setUsername("unknown");
            keyList.add(k);
            cat.setKeylist(keyList);

            //Validation of the Category
            if (ValidationManager.isValid(cat)) {
                List<Category> categoryList = new ArrayList<>();
                categoryList = catList.getCategoryList();
                categoryList.add(cat);
                catList.setCategoryList(categoryList);
                new FileManager().saveStructure(catList);
                initTree();
                editCancel();
                startNotification(EnumNotification.CAT_ADDED);

                //Category is invalid!
            } else {
                startNotification(EnumNotification.WARNING);
                LoggingManager.writeToLogFile("Categoryname or Iconpath is invalid!"
                        + " Please try again");
            }
        }
        // Change existing name of Cat
        if (editCat) {
            boolean live = true;
            for (int i = 0; i < catList.getCategoryList().size() && live; i++) {
                Category currentCategory = catList.getCategoryList().get(i);
                if (currentCategory.getName().equals(tmpBuffer)) {
                    currentCategory.setName(tf_catName.getText());
                    currentCategory.setIconPath(pathLabelCatIcon.getText());
                    new FileManager().saveStructure(catList);
                    initTree();
                    editCancel();
                    live = false;
                }
            }

        }

        if (editKey) {
            Key oldKey = keyBuffer;
            editedKey.setKeyname(tf_keyname.getText().trim());
            editedKey.setIconPath(pathLabelKeyIcon.getText());
            editedKey.setDescription(tf_description.getText().trim());
            editedKey.setPassword(tf_passwordConfirm.getText().trim());
            editedKey.setUsername(tf_username.getText().trim());

            List<Category> cat = catList.getCategoryList();

            //Validation of the Key
            if (ValidationManager.isValid(editedKey)) {
                boolean live = true;
                for (int i = 0; i < cat.size() && live; i++) {
                    Category currentCategory = cat.get(i);
                    List<Key> keyList = currentCategory.getKeylist();
                    for (int j = 0; j < keyList.size() && live; j++) {
                        Key currentKey = keyList.get(j);
                        if (currentKey.getKeyname().equals(oldKey.getKeyname())) {
                            currentKey.overwriteKey(editedKey);
                            currentCategory.setKeylist(keyList);
                            catList.setCategoryList(cat);
                            new FileManager().saveStructure(catList);
                            initTree();
                            editCancel();
                            live = false;
                        }
                    }
                }
                //Key is Invalid
            } else {
                startNotification(EnumNotification.WARNING);
                LoggingManager.writeToLogFile("Key is invalid!");
            }

        }

        if (addKey) {
            Key newKey = new Key();
            newKey.setKeyname(tf_keyname.getText().trim());
            newKey.setDescription(tf_description.getText().trim());
            newKey.setPassword(tf_passwordConfirm.getText().trim());
            newKey.setUsername(tf_username.getText().trim());
            newKey.setIconPath(pathLabelKeyIcon.getText());
            // Validate new Key
            if (ValidationManager.isValid(newKey)) {

                List<Category> cat = catList.getCategoryList();
                boolean stop = false;
                for (Category c : cat) {
                    if (stop) {
                        break;
                    }
                    if (c.getName().equals(selectedCat)) {
                        c.getKeylist().add(newKey);
                        stop = true;
                    }
                }
                catList.setCategoryList(cat);
                new FileManager().saveStructure(catList);
                initTree();
                editCancel();
                startNotification(EnumNotification.KEY_ADDED);

            } else {
                startNotification(EnumNotification.WARNING);
                LoggingManager.writeToLogFile("Key is invalid!");
            }
        }
    }

    @FXML
    private void validatePW() {
        if (tf_password.getText().equals(tf_passwordConfirm.getText())) {
            tf_passwordConfirm.setStyle("-fx-background-color: #00FF00");
            enableControl(btn_save);
        } else {
            tf_passwordConfirm.setStyle("-fx-background-color: #FE2E2E");
            disableControl(btn_save);
        }
    }

    ////////////////////////////////
    //
    // *****  Methods for the Notification in the UI  ******
    //
    ////////////////////////////////
    private void startNotification(EnumNotification eNotification) {
        String addCatMessage = languageBean.getValue("NOTIFIADDCAT");
        String addKeyMessage = languageBean.getValue("NOTIFIADDKEY");
        String removeCatMessage = languageBean.getValue("NOTIFIREMOVECAT");
        String removeKeyMessage = languageBean.getValue("NOTIFIREMOVEKEY");
        String errorMessage = languageBean.getValue("NOTIFIERROR");
        String warningMessage = languageBean.getValue("NOTIFIWARNING");
        String warningMessage_lastCat = languageBean.getValue("NOTIFIWARNINGCAT");

        if (eNotification.equals(EnumNotification.ERROR)) {
            errorLogHyperlink.setGraphic(new ImageView(nokImage));
            errorLogHyperlink.setText(errorMessage);
            fadeIn(errorLogHyperlink, 500);
            fadeOut(errorLogHyperlink, 10500);
            debugLog("Error! Please look at the Error.log");
        }
        if (eNotification.equals(EnumNotification.CAT_ADDED)) {
            notifyLabel.setGraphic(new ImageView(okImage));
            notifyLabel.setText(addCatMessage);
            fadeIn(notifyLabel, 500);
            fadeOut(notifyLabel, 5500);
        }
        if (eNotification.equals(EnumNotification.CAT_REMOVED)) {
            notifyLabel.setGraphic(new ImageView(okImage));
            notifyLabel.setText(removeCatMessage);
            fadeIn(notifyLabel, 500);
            fadeOut(notifyLabel, 5500);
        }
        if (eNotification.equals(EnumNotification.KEY_ADDED)) {
            notifyLabel.setGraphic(new ImageView(okImage));
            notifyLabel.setText(addKeyMessage);
            fadeIn(notifyLabel, 500);
            fadeOut(notifyLabel, 5500);
        }
        if (eNotification.equals(EnumNotification.KEY_REMOVED)) {
            notifyLabel.setGraphic(new ImageView(okImage));
            notifyLabel.setText(removeKeyMessage);
            fadeIn(notifyLabel, 500);
            fadeOut(notifyLabel, 5500);
        }
        if (eNotification.equals(EnumNotification.WARNING)) {
            notifyLabel.setGraphic(new ImageView(warningImage));
            notifyLabel.setText(warningMessage);
            fadeIn(notifyLabel, 500);
            fadeOut(notifyLabel, 5500);
        }
        if (eNotification.equals(EnumNotification.WARNING_LASTCAT)) {
            notifyLabel.setGraphic(new ImageView(warningImage));
            notifyLabel.setText(warningMessage_lastCat);
            fadeIn(notifyLabel, 500);
            fadeOut(notifyLabel, 5500);
        }
    }

    private void fadeIn(Control con, double duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), con);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private void fadeOut(Control con, double duration) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(duration), con);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
        fadeTransition.play();
    }

    ////////////////////////////////
    // 
    // ***** END of Methods for the Notification in the UI  ******
    //
    ////////////////////////////////
    ////////////////////////////////
    //
    // *****  Methods for the Languagesettings in the UI  ******
    //
    ////////////////////////////////
    private void initLanguage() {
        setUpLanguageCheckbox();
        debugLog("  try to get the data from the Properties...");

        addString = languageBean.getValue("SAVE");
        addString2 = languageBean.getValue("SAVEII");


        debugLog("  Changing Language...");
        //Labels
        lb_catName.setText(languageBean.getValue("CATNAME"));
        lb_confpw.setText(languageBean.getValue("CONFPASSWORD"));
        lb_username.setText(languageBean.getValue("USERNAME"));
        lb_description.setText(languageBean.getValue("DESCRIPTION"));
        lb_password.setText(languageBean.getValue("PASSWORD"));
        lb_confpw.setText(languageBean.getValue("CONFPASSWORD"));
        debugLog("  ...Labels done");

        //Buttons
        btn_addCat.setText(languageBean.getValue("ADDCAT"));
        btn_addKey.setText(languageBean.getValue("ADDKEY"));
        btn_browseCatIcon.setText(languageBean.getValue("BROWSE"));
        btn_cancel.setText(languageBean.getValue("CANCEL"));
        btn_edit.setText(languageBean.getValue("EDIT"));
        btn_save.setText(addString);
        btn_remove.setText(languageBean.getValue("REMOVE"));
        debugLog("  ...Buttons done");

        //Menu / MenuItems
        fileMenu.setText(languageBean.getValue("FILE"));
        editMenu.setText(languageBean.getValue("EDIT"));
        helpMenu.setText(languageBean.getValue("HELP"));
        languageItem.setText(languageBean.getValue("LANGUAGE"));
        chk_englishLang.setText(languageBean.getValue("ENGLISH"));
        chk_germanLang.setText(languageBean.getValue("GERMAN"));
        iconsItem.setText(languageBean.getValue("ICONMAN"));
        settingsItem.setText(languageBean.getValue("PROP"));
        export_fileItem.setText(languageBean.getValue("EXPORT"));
        exitItem.setText(languageBean.getValue("EXIT"));
        debugLog("  ...Menus and Items done");

        //Panes
        keyPane.setText(languageBean.getValue("KEYS"));
        notificationPane.setText(languageBean.getValue("NOTIFICATION"));
        actionPane.setText(languageBean.getValue("ACTIONS"));
        debugLog("  ...Panes done");

        //Special
        chk_useDefaultCatIcon.setText(languageBean.getValue("USEDEFAULTICON"));
        chk_useDefaultKeyIcon.setText(languageBean.getValue("USEDEFAULTICON"));
        errorTooltip.setText(languageBean.getValue("ERRORBOX"));
        debugLog("  ...Specials done");

        debugLog("");
        debugLog("...Language is changed!");
    }

    @FXML
    private void changeLangEN() {
        debugLog("Try to change language detected...");
        settingsBean.storeInBean("LANG", "EN");
        settingsBean.saveBean();
        debugLog("...ok");
        languageBean.setupNewLanguage(settingsBean.getValue("LANG"));
        selectedLanguage = "EN";
        initLanguage();
        chk_germanLang.setSelected(false);
    }

    @FXML
    private void changeLangDE() {
        debugLog("Try to change language detected...");
        chk_englishLang.setSelected(false);
        settingsBean.storeInBean("LANG", "DE");
        settingsBean.saveBean();
        debugLog("...ok");
        languageBean.setupNewLanguage(settingsBean.getValue("LANG"));
        selectedLanguage = "DE";
        initLanguage();
        chk_englishLang.setSelected(false);
    }

    private void setUpLanguageCheckbox() {
        debugLog("try to manage the Checkboxen in the UI ...");
        if (selectedLanguage.equals("EN")) {
            chk_englishLang.setSelected(true);
            chk_germanLang.setSelected(false);
        }
        if (selectedLanguage.equals("DE")) {
            chk_englishLang.setSelected(false);
            chk_germanLang.setSelected(true);
        }
        debugLog("  ...ok");
    }

    ////////////////////////////////
    //
    // ***** END Methods for the Languagesettings in the UI  ******
    //
    ///////////////////////////////
    ////////////////////////////////
    //
    // ***** Utility- Methods  ******
    //
    ///////////////////////////////
    @FXML
    private void selectDefaultCatIconCheckBox() {
        if (chk_useDefaultCatIcon.isSelected()) {
            disableControl(btn_browseCatIcon);
            try {
                iconCatPreview.setImage(FileManager.getImageFromPath(sm_icons.returnProperty("FOLDER_DEFAULT")));
            } catch (IOException ex) {
                Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            pathLabelCatIcon.setText("");
        } else {
            enableControl(btn_browseCatIcon);
        }
    }

    @FXML
    private void selectDefaultKeyIconCheckBox() {
        if (chk_useDefaultKeyIcon.isSelected()) {
            disableControl(btn_browseKeyIcon);
            try {
                String prop = sm_icons.returnProperty("KEYINTREE");
                iconKeyPreview.setImage(FileManager.getImageFromPath(prop));
                pathLabelKeyIcon.setText(prop);
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("selectDefaultKeyCheckBox()", ex);
                startNotification(EnumNotification.ERROR);
            } catch (IOException ex) {
                Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            enableControl(btn_browseKeyIcon);
            pathLabelKeyIcon.setText("");
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

    private void resetFields() {
        tf_keyname.setText("");
        tf_username.setText("");
        tf_description.setText("");
        tf_password.setText("");
        tf_passwordConfirm.setText("");
        pathLabelKeyIcon.setText("");
        iconKeyPreview.setImage(null);
        chk_useDefaultKeyIcon.setSelected(false);
        btn_browseKeyIcon.setDisable(true);
    }

    private void lockFields() {
        disableControl(tf_keyname, tf_username, tf_description, tf_password, tf_passwordConfirm,
                chk_useDefaultKeyIcon);
        disableControl(btn_browseKeyIcon);
    }

    private void unlockFields() {
        enableControl(tf_keyname, tf_username, tf_description, tf_password, tf_passwordConfirm, chk_useDefaultKeyIcon);
        enableControl(btn_browseKeyIcon);
    }

    private void lockTree() {
        tree.setDisable(true);
    }

    private void unlockTree() {
        tree.setDisable(false);
    }

    @FXML
    private void open_Properties() {
        new PageLoadHelper(PATH_PROPERTIES, "Properties", 428, 282, PropertiesController.class).loadPage();
    }

    @FXML
    private void open_Iconsite() {
        new PageLoadHelper(PATH_ICONMANAGEMENT, "Iconmanagement", 366, 400, IcondialogController.class).loadPage();
    }

    @FXML
    private void openErrorFile() {
        try {
            Desktop.getDesktop().open(new File("AppData/Error.log"));
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("openErrorFile()", ex);
            startNotification(EnumNotification.ERROR);
        }
    }

    @FXML
    private void exportFile() {
        new PageLoadHelper().loadExportDialog();
    }

    @FXML
    private void openHelpFile() {
        try {
            Desktop.getDesktop().open(new File("AppData/Help/HelpMainpage.html"));
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("openHelpFile()", ex);
            startNotification(EnumNotification.ERROR);
        }
    }

    @FXML
    private void browseImageCatIcon() {
        File path = browseFile();
        if (path != null) {
            pathLabelCatIcon.setText(path.getAbsolutePath());
            iconCatPreview.setImage(FileManager.getImageFromPath(path.getAbsolutePath()));
        }
    }

    @FXML
    private void browseImageKeyIcon() {
        File path = browseFile();
        if (path != null) {
            pathLabelKeyIcon.setText(path.getAbsolutePath());
            iconKeyPreview.setImage(FileManager.getImageFromPath(path.getAbsolutePath()));
        }
    }

    private File browseFile() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File("AppData/Images/intern"));
        chooser.setTitle("Choose an own Image for the Category");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ImageFiles", "*.png", "*.jpg"));
        File path = chooser.showOpenDialog(null);

        debugLog("Filechooser opened...");
        if (path != null) {
            return path;
        }

        return path;
    }

    private void updateKeyIcon(Key key) {
        iconKeyPreview.setImage(FileManager.getImageFromPath(key.getIconPath()));
    }

    ////////////////////////////////
    //
    // ***** END of Utility- Methods  ******
    //
    ///////////////////////////////
    ////////////////////////////////
    //
    // ***** Debugging-Methods  ******
    //
    ///////////////////////////////
    private void debugLog(String logText) {
        if (debug == 1) {
            LoggingManager.writeToLogFile(logText);
        }
    }

    ////////////////////////////////
    //
    // ***** END of Debugging-Methods  ******
    //
    ///////////////////////////////
    ////////////////////////////////
    //
    // ***** initialize- Method of JavaFX  ******
    //
    ///////////////////////////////
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load Main-ini
        settingsBean = Settings_Singelton.getInstance();
        selectedLanguage = settingsBean.getValue("LANG");

        // Debugsettings
        debug = Integer.decode(settingsBean.getValue("DEBUG"));

        System.out.println(debug);
        debugLog("Try to initialize the Program");
        debugLog("------------------------------");
        debugLog("");



        // Check availibility of the Structure.xml
        new FileManager().checkAvailibility();
        debugLog("Structure.xml founded or created");

        //Load the ini-Files and provide data

        sm_icons = new SettingManager("AppData/icons.properties");
        languageBean = Language_Singleton.getInstance();

        debugLog("Property and Ini- Files are loaded");

        // Set up the Language
        initLanguage();
        debugLog("Language configured");

        catList = fileManager.returnListofCategories();
        debugLog("Get converted List from Structure.xml");

        String userAvatar = null;
        userAvatar = settingsBean.getValue("AVATAR");
        lb_dynamicUserName.setText(settingsBean.getValue("USERNAME"));


        try {
            avatarIV.setImage(FileManager.getImageFromPath((userAvatar)));
            addCatImage = FileManager.getImageFromPath((sm_icons.returnProperty("ADDCAT")));
            trashImage = FileManager.getImageFromPath((sm_icons.returnProperty("TRASH")));
            keyImage = FileManager.getImageFromPath((sm_icons.returnProperty("ADDKEY")));
            settingImage = FileManager.getImageFromPath((sm_icons.returnProperty("SETTINGS")));
            iconsImage = FileManager.getImageFromPath((sm_icons.returnProperty("ICONS")));
            okImage = FileManager.getImageFromPath((sm_icons.returnProperty("OK")));
            nokImage = FileManager.getImageFromPath((sm_icons.returnProperty("NOK")));
            warningImage = FileManager.getImageFromPath((sm_icons.returnProperty("WARNING")));
            export_fileItem.setGraphic(FileManager.getImageViewFromPath(sm_icons.returnProperty("FILEEXPORT")));
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile("initialize() -> Loading Icons from icons.properties failed", ex);
            startNotification(EnumNotification.ERROR);
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        }
        btn_addCat.setGraphic(new ImageView(addCatImage));
        btn_remove.setGraphic(new ImageView(trashImage));
        btn_addKey.setGraphic(new ImageView(keyImage));
        settingsItem.setGraphic(new ImageView(settingImage));
        iconsItem.setGraphic(new ImageView(iconsImage));
        debugLog("Images for Mainpage loaded");

        debugLog("Try to add SelectionListener to Tree...");
        tree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue ov, Object t, Object t1) {
                if (ov != null || t != null || t1 != null) {
                    selectedItem = (TreeItem) ov.getValue();
                    if (selectedItem != null) {
                        btn_remove.setDisable(false);
                        if (selectedItem.isLeaf()) {
                            selectedKey = new FileManager().returnKey(selectedItem.getValue().toString());
                            tf_keyname.setText(selectedKey.getKeyname());
                            tf_username.setText(selectedKey.getUsername());
                            tf_description.setText(selectedKey.getDescription());
                            tf_password.setText(selectedKey.getPassword());
                            tf_passwordConfirm.setText(selectedKey.getPassword());
                            pathLabelKeyIcon.setText(selectedKey.getIconPath());
                            updateKeyIcon(selectedKey);
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
        debugLog("... ok");
        initTree();
        debugLog("Create Maintree");
        lockFields();
        debugLog("Cleaning up...");
        debugLog("-ready for using-");
        debugLog("");
    }
    ////////////////////////////////
    //
    // ***** END of initialize- Method of JavaFX  ******
    //
    ///////////////////////////////
}
