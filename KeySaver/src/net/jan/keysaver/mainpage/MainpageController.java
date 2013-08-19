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
import java.util.Properties;
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
    private Label pathLabel;
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
    private CheckBox chk_useDefaultIcon;
    //Buttons
    @FXML
    private Button btn_browse;
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
    private MenuItem iconsItem;
    @FXML
    private MenuItem languageItem;
    @FXML
    private CheckBox chk_englishLang;
    @FXML
    private CheckBox chk_germanLang;
    /////////
    @FXML
    private ImageView pwImage;
    @FXML
    private ImageView avatarIV;
    @FXML
    private ImageView iconPreview;
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
    private final String PATH_PROPERTIES = "src\\net\\jan\\keysaver\\properties\\Properties.fxml";
    private final String PATH_ICONMANAGEMENT = "src\\net\\jan\\keysaver\\icondialog\\Icondialog.fxml";
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
    private SettingManager sm_main;
    private SettingManager sm_icons;
    private SettingManager sm_languageDE;
    private SettingManager sm_languageEN;
    private int debug = 0;

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
            System.out.println("editKey detected - " + editKey);
        }
        if (!selectedItem.isLeaf()) {
            lockTree();
            lockFields();
            setControlVisible(lb_catName, tf_catName);
            disableControl(btn_addCat, btn_edit, btn_remove);
            enableControl(btn_save, btn_cancel);
            tf_catName.setText(tmpBuffer);
            editCat = true;
            System.out.println("editCat detected - " + editCat);
        }
    }

    @FXML
    private void prepareAddCat() {
        setControlVisible(lb_catName, tf_catName, chk_useDefaultIcon, btn_browse);
        disableControl(btn_edit, btn_addCat, btn_remove, btn_addKey);
        enableControl(btn_cancel, btn_save);
        btn_save.setText("Add");
        addCat = true;
        if (iconPreview.getImage() == null) {
            try {
                iconPreview.setImage(new Image(new FileInputStream("AppData\\Images\\intern\\Folder_default_16x16.png")));

            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("initialize() -> Failed to load Folder_default_16x16.png", ex);
                startNotification(EnumNotification.ERROR);
            }
        } else {
            iconPreview.setVisible(true);
        }
        lockTree();
    }

    @FXML
    private void prepareAddKey() {
        disableControl(btn_edit, btn_addCat, btn_remove, btn_addKey);
        // Buttons
        enableControl(btn_cancel, btn_save);
        // Textfields
        enableControl(tf_keyname, tf_description, tf_password, tf_passwordConfirm, tf_username);
        btn_save.setText("Save");
        addKey = true;
        System.out.println("AddKey detected - " + addKey);
        lockTree();
    }

    @FXML
    private void editCancel() {
        lockFields();
        unlockTree();
        setControlUnvisible(lb_catName, tf_catName, btn_browse, chk_useDefaultIcon);
        disableControl(btn_edit, btn_save, btn_addCat, btn_addKey, btn_cancel, btn_remove);
        btn_save.setText("Save");
        tf_catName.setText("");
        addCat = false;
        editKey = false;
        addKey = false;
        editCat = false;
        iconPreview.setVisible(false);
        chk_useDefaultIcon.setSelected(true);
        tf_catName.setText("");
        btn_browse.setDisable(true);
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
                LoggingManager.writeToErrorFile("Init-Tree() -> Iconpath for Category", ex);
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
            iconPreview.setVisible(true);
            if (chk_useDefaultIcon.isSelected()) {
                cat.setIconPath("AppData\\Images\\intern\\Folder_default_16x16.png");
            } else {
                cat.setIconPath(pathLabel.getText());
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

    ////////////////////////////////
    //
    // *****  Methods for the Notification in the UI  ******
    //
    ////////////////////////////////
    private void startNotification(EnumNotification eNotification) {
        String addCatMessage = "";
        String addKeyMessage = "";
        String removeCatMessage = "";
        String removeKeyMessage = "";
        String errorMessage = "";

        if (selectedLanguage.equals("DE")) {
            try {
                addCatMessage = sm_languageDE.returnProperty("NOTIFIADDCAT");
                addKeyMessage = sm_languageDE.returnProperty("NOTIFIADDKEY");
                removeCatMessage = sm_languageDE.returnProperty("NOTIFIREMOVECAT");
                removeKeyMessage = sm_languageDE.returnProperty("NOTIFIREMOVEKEY");
                errorMessage = sm_languageDE.returnProperty("NOTIFIERROR");
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("startNotification()", ex);
            }
        }

        if (selectedLanguage.equals("EN")) {
            try {
                addCatMessage = sm_languageEN.returnProperty("NOTIFIADDCAT");
                addKeyMessage = sm_languageEN.returnProperty("NOTIFIADDKEY");
                removeCatMessage = sm_languageEN.returnProperty("NOTIFIREMOVECAT");
                removeKeyMessage = sm_languageEN.returnProperty("NOTIFIREMOVEKEY");
                errorMessage = sm_languageEN.returnProperty("NOTIFIERROR");
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("startNotification()", ex);
            }
        }

        if (eNotification.equals(EnumNotification.ERROR)) {
            errorLogHyperlink.setGraphic(new ImageView(nokImage));
            errorLogHyperlink.setText(errorMessage);
            errorLogHyperlink.setDisable(false);
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
        errorLogHyperlink.setDisable(true);
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
        debugLog("Try to load new Language from Properties...");
        Properties prop = new Properties();
        debugLog("  try to get the language we want to load...");
        try {
            selectedLanguage = sm_main.returnSetLanguage();
            debugLog("  ... ok");
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        }

        setLanguageCheckbox();
        debugLog("  try to get the data from the Properties...");
        if (selectedLanguage.equals("DE")) {
            try {
                prop = sm_languageDE.initAndReturnProperties();
                debugLog("  ...ok");
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile(null, ex);
                startNotification(EnumNotification.ERROR);
            }
        }
        if (selectedLanguage.equals("EN")) {
            try {
                prop = sm_languageEN.initAndReturnProperties();
                debugLog("  ...ok");
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile(null, ex);
                startNotification(EnumNotification.ERROR);
            }
        }

        debugLog("  Changing Language...");
        //Labels
        lb_catName.setText(prop.getProperty("CATNAME"));
        lb_confpw.setText(prop.getProperty("CONFPASSWORD"));
        lb_username.setText(prop.getProperty("USERNAME"));
        lb_description.setText(prop.getProperty("DESCRIPTION"));
        lb_password.setText(prop.getProperty("PASSWORD"));
        lb_confpw.setText(prop.getProperty("CONFPASSWORD"));
        debugLog("  ...Labels done");

        //Buttons
        btn_addCat.setText(prop.getProperty("ADDCAT"));
        btn_addKey.setText(prop.getProperty("ADDKEY"));
        btn_browse.setText(prop.getProperty("BROWSE"));
        btn_cancel.setText(prop.getProperty("CANCEL"));
        btn_edit.setText(prop.getProperty("EDIT"));
        btn_save.setText(prop.getProperty("SAVE"));
        btn_remove.setText(prop.getProperty("REMOVE"));
        debugLog("  ...Buttons done");

        //Menu / MenuItems
        fileMenu.setText(prop.getProperty("FILE"));
        editMenu.setText(prop.getProperty("EDIT"));
        helpMenu.setText(prop.getProperty("HELP"));
        languageItem.setText(prop.getProperty("LANGUAGE"));
        chk_englishLang.setText(prop.getProperty("ENGLISH"));
        chk_germanLang.setText(prop.getProperty("GERMAN"));
        iconsItem.setText(prop.getProperty("ICONMAN"));
        settingsItem.setText(prop.getProperty("PROP"));
        debugLog("  ...Menus and Items done");

        //Panes
        keyPane.setText(prop.getProperty("KEYS"));
        notificationPane.setText(prop.getProperty("NOTIFICATION"));
        actionPane.setText(prop.getProperty("ACTIONS"));
        debugLog("  ...Panes done");



        debugLog("");
        debugLog("...Language is changed!");
    }

    @FXML
    private void changeLangEN() {
        debugLog("Try to change language detected...");
        try {
            sm_main.saveLanguageInIniFile("EN");
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        }
        debugLog("...ok");
        initLanguage();
        chk_germanLang.setSelected(false);
    }

    @FXML
    private void changeLangDE() {
        debugLog("Try to change language detected...");
        chk_englishLang.setSelected(false);
        try {
            sm_main.saveLanguageInIniFile("DE");
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        }
        debugLog("...ok");
        initLanguage();
        chk_englishLang.setSelected(false);
    }

    private void setLanguageCheckbox() {
        debugLog("  try to manage the Checkboxen in the UI ...");
        if (selectedLanguage.equals("EN")) {
            chk_englishLang.setSelected(true);
            chk_germanLang.setSelected(false);
            try {
                sm_main.saveLanguageInIniFile("EN");
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("setLanguageCheckbox-Event", ex);
                startNotification(EnumNotification.ERROR);
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile(null, ex);
                startNotification(EnumNotification.ERROR);
            }
        }
        if (selectedLanguage.equals("DE")) {
            chk_englishLang.setSelected(false);
            chk_germanLang.setSelected(true);
            try {
                sm_main.saveLanguageInIniFile("DE");
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("setLanguageCheckbox-Event", ex);
                startNotification(EnumNotification.ERROR);
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile(null, ex);
                startNotification(EnumNotification.ERROR);
            }
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
    private void selectDefaultCheckBox() {
        if (chk_useDefaultIcon.isSelected()) {
            disableControl(btn_browse);
            try {
                iconPreview.setImage(new Image(new FileInputStream("AppData\\Images\\intern\\Folder_default_16x16.png")));
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("selectDefaultCheckBox()", ex);
                startNotification(EnumNotification.ERROR);
            }
            pathLabel.setText("");
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
        new PageLoadHelper(PATH_PROPERTIES, "Properties", 428, 241);
    }

    @FXML
    private void open_Iconsite() {
        new PageLoadHelper(PATH_ICONMANAGEMENT, "Iconmanagement", 366, 400);
    }

    @FXML
    private void openErrorFile() {
        try {
            Desktop.getDesktop().open(new File("AppData\\Error.log"));
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("openErrorFile()", ex);
            startNotification(EnumNotification.ERROR);
        }
    }

    @FXML
    private void browseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File("AppData\\Images\\intern"));
        chooser.setTitle("Choose an own Image for the Category");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ImageFiles", "*.png", "*.jpg"));
        File path = chooser.showOpenDialog(null);

        debugLog("Filechooser opened...");

        pathLabel.setText(path.getAbsolutePath());
        try {
            iconPreview.setImage(new Image(new FileInputStream(path)));
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile("browseImage()", ex);
            startNotification(EnumNotification.ERROR);
        }
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
        sm_main = new SettingManager("settings.ini");
        // Debugsettings
        try {
            debug = Integer.decode(sm_main.returnProperty("DEBUG"));
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        }
        System.out.println(debug);
        debugLog("Try to initialize the Program");
        debugLog("------------------------------");
        debugLog("");



        // Check availibility of the Structure.xml
        new FileManager().checkAvailibility();
        debugLog("Structure.xml founded or created");

        //Load the ini-Files and provide data

        sm_icons = new SettingManager("AppData\\icons.properties");
        sm_languageDE = new SettingManager("AppData\\Lang_DE.properties");
        sm_languageEN = new SettingManager("AppData\\Lang_EN.properties");
        debugLog("Property and Ini- Files are loaded");

        // Set up the Language
        initLanguage();
        debugLog("Language configured");

        catList = fileManager.returnListofCategories();
        debugLog("Get converted List from Structure.xml");

        String userAvatar = null;
        try {
            userAvatar = sm_main.returnProperty("AVATAR");
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        }
        try {
            lb_dynamicUserName.setText(sm_main.returnProperty("USERNAME"));
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile(null, ex);
            startNotification(EnumNotification.ERROR);
        }
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
