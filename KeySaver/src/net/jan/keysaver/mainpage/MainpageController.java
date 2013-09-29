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
import net.jan.keysaver.icondialog.IcondialogController;
import net.jan.keysaver.properties.PropertiesController;
import net.jan.keysaver.sources.PageLoadHelper;
import net.jan.keysaver.validators.Validator;

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
            System.out.println("editKey detected - " + editKey);
        }
        if (!selectedItem.isLeaf()) {
            lockTree();
            lockFields();
            Category selectedLocalCat = new FileManager().returnSingleCategory(tmpBuffer);
            setControlVisible(lb_catName, tf_catName);
            disableControl(btn_addCat, btn_edit, btn_remove, btn_addKey);
            setControlVisible(btn_browse, chk_useDefaultIcon);
            chk_useDefaultIcon.setDisable(false);
            iconPreview.setDisable(false);
            enableControl(btn_save, btn_cancel);
            
            tf_catName.setText(selectedLocalCat.getName());
            pathLabel.setText(selectedLocalCat.getIconPath());
            selectedLocalCat = null;
            editCat = true;
            System.out.println("editCat detected - " + editCat);
        }
    }

    @FXML
    private void prepareAddCat() {
        setControlVisible(lb_catName, tf_catName, chk_useDefaultIcon, btn_browse);
        disableControl(btn_edit, btn_addCat, btn_remove, btn_addKey);
        enableControl(btn_cancel, btn_save);
        btn_save.setText(addString2);
        addCat = true;
        if (iconPreview.getImage() == null) {
            try {
                iconPreview.setImage(new Image(new FileInputStream(sm_icons.returnProperty("FOLDER_DEFAULT"))));

            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("initialize() -> Failed to load Folder_default_16x16.png", ex);
                startNotification(EnumNotification.ERROR);
            } catch (IOException ex) {
                Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
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
        btn_save.setText(addString);
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
        btn_save.setText(addString);
        tf_catName.setText("");
        addCat = false;
        editKey = false;
        addKey = false;
        editCat = false;
        iconPreview.setVisible(false);
        chk_useDefaultIcon.setSelected(true);
        tf_catName.setText("");
        btn_browse.setDisable(true);
        pathLabel.setDisable(true);
        pwImage.setImage(null);
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
    private void save() throws IOException {
        //new Category
        if (addCat) {
            String catname = tf_catName.getText().trim();
            
            Category cat = new Category();
            cat.setName(catname);
            if (chk_useDefaultIcon.isSelected()) {
                cat.setIconPath(sm_icons.returnProperty("FOLDER_DEFAULT"));
            } else {
                cat.setIconPath(pathLabel.getText());
            }
            
            //Validation of the Category
            if ( new Validator().validateCategory(cat)){

            iconPreview.setVisible(true);
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
            
            //Category is invalid!
            } else {
             startNotification(EnumNotification.WARNING);
             LoggingManager.writeToLogFile("Categoryname or Iconpath is invalid!"
                     + " Please try again");
            }
        }
        // Change existing name of Cat
        if (editCat) {
            
            for (Category cat : catList.getCategoryList()) {
                if (cat.getName().equals(tmpBuffer)) {
                    //Validate
//                    if ( new Validator().validateCategory(cat) )
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
            editedKey.setKeyname(tf_keyname.getText().trim());
            editedKey.setDescription(tf_description.getText().trim());
            editedKey.setPassword(tf_passwordConfirm.getText().trim());
            editedKey.setUsername(tf_username.getText().trim());

            List<Category> cat = catList.getCategoryList();
            boolean stop = false;

            //Validation of the Key
            if (new Validator().validateKey(editedKey)) {

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


            // Validate new Key
            if (new Validator().validateKey(newKey)) {

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

                // new Key is invalid!
            } else {
                startNotification(EnumNotification.WARNING);
                LoggingManager.writeToLogFile("Key is invalid!");
            }
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
        String addCatMessage = languageBean.getValue("NOTIFIADDCAT");
        String addKeyMessage = languageBean.getValue("NOTIFIADDKEY");
        String removeCatMessage = languageBean.getValue("NOTIFIREMOVECAT");
        String removeKeyMessage = languageBean.getValue("NOTIFIREMOVEKEY");
        String errorMessage = languageBean.getValue("NOTIFIERROR");
        String warningMessage = languageBean.getValue("NOTIFIWARNING");




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
        btn_browse.setText(languageBean.getValue("BROWSE"));
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
        debugLog("  ...Menus and Items done");

        //Panes
        keyPane.setText(languageBean.getValue("KEYS"));
        notificationPane.setText(languageBean.getValue("NOTIFICATION"));
        actionPane.setText(languageBean.getValue("ACTIONS"));
        debugLog("  ...Panes done");
        
        //Special
        chk_useDefaultIcon.setText(languageBean.getValue("USEDEFAULTICON"));
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
    private void selectDefaultCheckBox() {
        if (chk_useDefaultIcon.isSelected()) {
            disableControl(btn_browse);
            try {
                iconPreview.setImage(new Image(new FileInputStream(sm_icons.returnProperty("FOLDER_DEFAULT"))));
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("selectDefaultCheckBox()", ex);
                startNotification(EnumNotification.ERROR);
            } catch (IOException ex) {
                Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
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
        new PageLoadHelper(PATH_PROPERTIES, "Properties", 428, 241, PropertiesController.class).loadPage();
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
    private void browseImage() {
        FileChooser chooser = new FileChooser();
        chooser.setInitialDirectory(new File("AppData/Images/intern"));
        chooser.setTitle("Choose an own Image for the Category");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("ImageFiles", "*.png", "*.jpg"));
        File path = chooser.showOpenDialog(null);

        debugLog("Filechooser opened...");
        if (path != null) {
            pathLabel.setText(path.getAbsolutePath());
            try {
                iconPreview.setImage(new Image(new FileInputStream(path)));
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("browseImage()", ex);
                startNotification(EnumNotification.ERROR);
            }
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
            avatarIV.setImage(new Image(new FileInputStream(userAvatar)));
            addCatImage = new Image(new FileInputStream(sm_icons.returnProperty("ADDCAT")));
            trashImage = new Image(new FileInputStream(sm_icons.returnProperty("TRASH")));
            keyImage = new Image(new FileInputStream(sm_icons.returnProperty("ADDKEY")));
            settingImage = new Image(new FileInputStream(sm_icons.returnProperty("SETTINGS")));
            iconsImage = new Image(new FileInputStream(sm_icons.returnProperty("ICONS")));
            okImage = new Image(new FileInputStream(sm_icons.returnProperty("OK")));
            nokImage = new Image(new FileInputStream(sm_icons.returnProperty("NOK")));
            warningImage = new Image(new FileInputStream(sm_icons.returnProperty("WARNING")));
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
