/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
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
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import net.jan.keysaver2.manager.LoggingManager;
import net.jan.keysaver2.manager.SettingManager;
import net.jan.keysaver2.entities.Category;
import net.jan.keysaver2.sources.CategoryList;
import net.jan.keysaver2.sources.EnumNotification;
import net.jan.keysaver2.entities.PersonalKey;
import net.jan.keysaver2.manager.ValidationManager;
import net.jan.keysaver2.sources.PageLoadHelper;

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
    private Label lb_password;
    @FXML
    private Label lb_keyname;
    @FXML
    private Label lb_description;
    @FXML
    private Label lb_keyicon;
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
    private MenuItem devSiteItem;
    @FXML
    private MenuItem languageItem;
    @FXML
    private MenuItem exitItem;
    @FXML
    private MenuItem updateItem;
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
    private SelectionModel model;
    private PersonalKey selectedKey = new PersonalKey();
    private TreeItem selectedItem = new TreeItem();
    private PersonalKey keyBuffer = new PersonalKey();
    private PersonalKey editedKey = new PersonalKey();
    private Category selectedCategory = new Category();
    private boolean isKeySelected = false;
    private boolean isCatSelected = false;
    private String selectedLanguage = "unknown";
    private SettingManager sm_icons;
    private int debug = 0;
    private String addString = "";
    private String addString2 = "";
    @FXML
    private Tooltip errorTooltip;
    
    @FXML
    private GridPane keyGrid;
    @FXML
    private GridPane categoryGrid;
    
    /*
     * The Behavior- Variables define the global behavior of the UI.
     */
    /**
     * The addCat- Variable flags if a Category will be added.
     */
    private boolean addCat = false;
    /**
     * The editKey- Variable flags if a Key will be edited.
     */
    private boolean editKey = false;
    /**
     * The addKey- Variable flags if a Key will be added.
     */
    private boolean addKey = false;
    /**
     * The editCat- Variable flags if a Category will be edited.
     */
    private boolean editCat = false;
    // ============================
    
    
    // ========== END OF VARIABLES AND DECLARATIONS =================
    
    @FXML
    private void edit() {
        String tmpBuffer = selectedItem.getValue().toString();
        if (selectedItem.isLeaf()) {
            unlockFields();
            lockTree();
            categoryGrid.setStyle("-fx-background-color: #E6E6E6");
            disableControl(btn_edit, btn_remove);
            enableControl(btn_cancel, btn_save);
//            keyBuffer = new XMLManager().returnKey(tmpBuffer);
            pathLabelKeyIcon.setText(keyBuffer.getIconPath());
            if (keyBuffer.getIconPath().endsWith("Key_tree_16x16.png")) {
                chk_useDefaultKeyIcon.setSelected(true);
                btn_browseKeyIcon.setDisable(true);
            } else {
                btn_browseKeyIcon.setDisable(false);
                chk_useDefaultKeyIcon.setSelected(false);
            }
            editKey = true;
        }
        if (!selectedItem.isLeaf()) {
            lockTree();
            lockFields();
            keyGrid.setStyle("-fx-background-color: #E6E6E6");
            Category selectedLocalCat = null;
            disableControl(btn_addCat, btn_edit, btn_remove, btn_addKey);
            chk_useDefaultCatIcon.setDisable(false);
            iconCatPreview.setDisable(false);
            enableControl(btn_save, btn_cancel, tf_catName);

            tf_catName.setText(selectedLocalCat.getName());
            pathLabelCatIcon.setText(selectedLocalCat.getIconPath());
//            iconCatPreview.setImage(FileManager.getImageFromPath(pathLabelCatIcon.getText()));
            if (selectedLocalCat.getIconPath().endsWith("Folder_default_16x16.png")) {
                chk_useDefaultCatIcon.setSelected(true);
                btn_browseCatIcon.setDisable(true);
            } else {
                chk_useDefaultCatIcon.setSelected(false);
                btn_browseCatIcon.setDisable(false);
            }
            selectedLocalCat = null;
            editCat = true;
            
        }
    }

    @FXML
    private void prepareAddCat() {
        disableControl(btn_edit, btn_addCat, btn_remove, btn_addKey);
        enableControl(btn_cancel, btn_save, chk_useDefaultCatIcon, tf_catName);
        keyGrid.setStyle("-fx-background-color: #E6E6E6");
        btn_save.setText(addString2);
        addCat = true;
        if (iconCatPreview.getImage() == null) {
            try {
                String pathDefaultIcon = sm_icons.returnProperty("FOLDER_DEFAULT");
                chk_useDefaultCatIcon.setSelected(true);
//                iconCatPreview.setImage(FileManager.getImageFromPath(pathDefaultIcon));
                pathLabelCatIcon.setText(pathDefaultIcon);
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
    
    
    /**
     * PrepareAddKey- Method manages the behavior of the UI if the
     * addition of a Key is detected.
     * This method defines some rules for UI- Components if
     * the User want to add a Key. 
     * Beside the simple changes set this method the important 
     * {@link #addKey} - value for the {@link #save() }- method.
     */
    @FXML
    private void prepareAddKey() {
        disableControl(btn_edit, btn_addCat, btn_remove, btn_addKey);
        // Buttons
        enableControl(btn_cancel, btn_save);
        // Textfields
        enableControl(tf_keyname, tf_description, tf_password,
                tf_username, chk_useDefaultKeyIcon);
        chk_useDefaultKeyIcon.setSelected(true);
        categoryGrid.setStyle("-fx-background-color: #E6E6E6");
        try {
            String prop = sm_icons.returnProperty("KEYINTREE");
//            iconKeyPreview.setImage(FileManager.getImageFromPath(prop));
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
        
        disableControl(
                btn_edit, btn_save, btn_addCat, 
                btn_addKey, btn_cancel, chk_useDefaultCatIcon, 
                tf_catName, btn_remove, btn_browseCatIcon,
                pathLabelCatIcon, btn_browseKeyIcon, pathLabelKeyIcon);
        
        btn_save.setText(addString);
        tf_catName.setText("");
        addCat = false;
        editKey = false;
        addKey = false;
        editCat = false;
        chk_useDefaultKeyIcon.setSelected(false);
        chk_useDefaultCatIcon.setSelected(false);
        tf_catName.setText("");
        pathLabelCatIcon.setText("");
        pathLabelKeyIcon.setText("");
        iconCatPreview.setImage(null);
        iconKeyPreview.setImage(null);
        categoryGrid.setStyle("-fx-background-color: #FFFFFF");
        keyGrid.setStyle("-fx-background-color: #FFFFFF");
    }

    private void initTree() {
        TreeItem<String> rootItem = new TreeItem<String>("Categories", null);
        rootItem.setExpanded(true);
        
        for (Category cat : catList.getCategoryList()) {
            TreeItem<String> categoryItem = new TreeItem<String>(cat.getName());
//            categoryItem.setGraphic(FileManager.getImageViewFromPath(cat.getIconPath()));
            for (PersonalKey k : cat.getKeylist()) {
                TreeItem<String> keyItem = new TreeItem<String>(k.getKeyname());
//                keyItem.setGraphic(FileManager.getImageViewFromPath(k.getIconPath()));
                categoryItem.getChildren().add(keyItem);
            }
            rootItem.getChildren().add(categoryItem);
        }
        tree.setRoot(rootItem);
        model = tree.getSelectionModel();
        tree.setEditable(true);
        tree.showRootProperty().setValue(false);
    }

    /**
     * This method removes the selected Category or Key from the Tree.
     */
    @FXML
    private void remove() {
        List<Category> cat = catList.getCategoryList();
//        if (isKeySelected) {
//            if (selectedCategory.hasMoreThanOneKey()){
//                Category c = selectedCategory;
//                c.deleteKey(selectedKey);
//                catList.replaceCategory(selectedCategory, c);
////                new XMLManager().saveStructure(catList);
//                sortTree();
//            } else {
//                startNotification(EnumNotification.WARNING_LASTCAT);
//                LoggingManager.writeToErrorFile("WARNING!! \nList of Categories may not be empty!", null);
//            }
//        }
        
        if (isCatSelected) {
            if (cat.size() > 1){
                Category c = selectedCategory;
                catList.removeCategory(c);
//                new XMLManager().saveStructure(catList);
                sortTree();
            } else {
                startNotification(EnumNotification.WARNING_LASTCAT);
                LoggingManager.writeToErrorFile("WARNING!! \nList of Categories may not be empty!", null);
            }
        }
    }

    /**
     * Save- Method for keys and Categories. This method saves all keys and
     * Categories. The saving of data includes the editing and adding new
     * values. So the Method is divided in add- and edit- parts. The variables {@link #addCat }, {@link #editCat }, {@link #addKey
     * } and {@link #editKey }
     * are controlled by the prepare- methods and the cancel- method. See: {@link #prepareAddCat() }
     * {@link #prepareAddKey() }
     * {@link #editCancel() }
     *
     * @throws IOException
     */
    @FXML
    private void save() throws IOException {
        Category categoryBuffer = new Category();
        //new Category
        /*
         * Defines the behavior of the addCat- Case.
         */
        if (addCat) {
            Category cat = new Category();
            cat.setName(tf_catName.getText().trim());
            if (chk_useDefaultCatIcon.isSelected()) {
                cat.setIconPath(sm_icons.returnProperty("FOLDER_DEFAULT"));
            } else {
                cat.setIconPath(pathLabelCatIcon.getText());
            }
            iconCatPreview.setVisible(true);
            PersonalKey k = new PersonalKey();
            List<PersonalKey> keyList = new ArrayList<PersonalKey>();
            k.setKeyname("new Key");
            k.setDescription("unknown");
            k.setIconPath(sm_icons.returnProperty("KEYINTREE"));
            k.setPassword("unknown");
            k.setUsername("unknown");
            keyList.add(k);
            cat.setKeylist(keyList);

            //Validation of the Category
            if (ValidationManager.isValid(cat)) {
                catList.addNewCategory(cat);
//                new XMLManager().saveStructure(catList);
                sortTree();
                editCancel();
                startNotification(EnumNotification.CAT_ADDED);

                //Category is invalid!
            } else {
                startNotification(EnumNotification.WARNING);
                LoggingManager.writeToLogFile("Categoryname or Iconpath is invalid!"
                        + " Please try again");
            }
        }
        // Change existing name of Cat or path of icon
        if (editCat) {
            Category editedCategory = new Category();
            editedCategory.setName(tf_catName.getText().trim());
            editedCategory.setIconPath(pathLabelCatIcon.getText());
            editedCategory.setKeylist(selectedCategory.getKeylist());

            if (ValidationManager.isValid(editedCategory)) {
                catList.replaceCategory(selectedCategory, editedCategory);
//                new XMLManager().saveStructure(catList);
                sortTree();
                editCancel();
            } else {
                startNotification(EnumNotification.WARNING);
                LoggingManager.writeToLogFile("Categoryname or Iconpath is invalid!"
                        + " Please try again");
            }
        }

        if (editKey) {
            PersonalKey oldKey = keyBuffer;
            editedKey = new PersonalKey();
            editedKey.setKeyname(tf_keyname.getText().trim());
            editedKey.setIconPath(pathLabelKeyIcon.getText());
            editedKey.setDescription(tf_description.getText().trim());
            editedKey.setPassword(tf_password.getText().trim());
            editedKey.setUsername(tf_username.getText().trim());

            //Validation of the Key
//            if (ValidationManager.isValid(editedKey)) {
//                categoryBuffer = selectedCategory;
//                selectedCategory.replaceKey(oldKey, editedKey);
//                catList.replaceCategory(categoryBuffer, selectedCategory);
////                new XMLManager().saveStructure(catList);
//                sortTree();
//                editCancel();
//
//                //Key is Invalid
//            } else {
//                startNotification(EnumNotification.WARNING);
//                LoggingManager.writeToLogFile("Key is invalid!");
//            }

        }

        if (addKey) {
            PersonalKey newKey = new PersonalKey();
            newKey.setKeyname(tf_keyname.getText().trim());
            newKey.setDescription(tf_description.getText().trim());
            newKey.setPassword(tf_password.getText().trim());
            newKey.setUsername(tf_username.getText().trim());
            newKey.setIconPath(pathLabelKeyIcon.getText());
            // Validate new Key
//            if (ValidationManager.isValid(newKey)) {
//                categoryBuffer = selectedCategory;
//                selectedCategory.addKey(newKey);
//                catList.replaceCategory(categoryBuffer, selectedCategory);
////                new XMLManager().saveStructure(catList);
//                sortTree();
//                editCancel();
//                startNotification(EnumNotification.KEY_ADDED);
//
//            } else {
//                startNotification(EnumNotification.WARNING);
//                LoggingManager.writeToLogFile("Key is invalid!");
//            }
        }
    }

    ////////////////////////////////
    //
    // *****  Methods for the Notification in the UI  ******
    //
    ////////////////////////////////
    private void startNotification(EnumNotification eNotification) {
        
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
        
    }

    @FXML
    private void changeLangEN() {
        
    }

    @FXML
    private void changeLangDE() {
        
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
            String path = "";
            try {
                path = sm_icons.returnProperty("FOLDER_DEFAULT");
            } catch (IOException ex) {
                Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
            }
            pathLabelCatIcon.setText(path);
            disableControl(btn_browseCatIcon);
//            iconCatPreview.setImage(FileManager.getImageFromPath(path));
        } else {
            enableControl(btn_browseCatIcon);
            pathLabelCatIcon.setText("");
        }
    }

    @FXML
    private void selectDefaultKeyIconCheckBox() {
        if (chk_useDefaultKeyIcon.isSelected()) {
            disableControl(btn_browseKeyIcon);
            try {
                String prop = sm_icons.returnProperty("KEYINTREE");
//                iconKeyPreview.setImage(FileManager.getImageFromPath(prop));
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

    private void resetFields() {
        tf_keyname.setText("");
        tf_username.setText("");
        tf_description.setText("");
        tf_password.setText("");
        pathLabelKeyIcon.setText("");
        iconKeyPreview.setImage(null);
        chk_useDefaultKeyIcon.setSelected(false);
        btn_browseKeyIcon.setDisable(true);
    }

    private void lockFields() {
        disableControl(tf_keyname, tf_username, tf_description, tf_password,
                chk_useDefaultKeyIcon);
        disableControl(btn_browseKeyIcon);
    }

    private void unlockFields() {
        enableControl(tf_keyname, tf_username, tf_description, tf_password, chk_useDefaultKeyIcon);
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
        new PageLoadHelper().loadPropertiesDialog();
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
    private void openGitHub() {
        try {
            Desktop.getDesktop().browse(new URI("https://github.com/janhorak/keysaver2.0"));
        } catch (IOException | URISyntaxException ex) {
            Logger.getLogger(MainpageController.class.getName()).log(Level.SEVERE, null, ex);
        }

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
//            iconCatPreview.setImage(FileManager.getImageFromPath(path.getAbsolutePath()));
        }
    }

    @FXML
    private void browseImageKeyIcon() {
        File path = browseFile();
        if (path != null) {
//            pathLabelKeyIcon.setText(path.getAbsolutePath());
//            iconKeyPreview.setImage(FileManager.getImageFromPath(path.getAbsolutePath()));
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
        // @TODO: Empty null- path will be returned anyway
        
        return path;
    }

    private void updateKeyIcon(PersonalKey key) {
//        iconKeyPreview.setImage(FileManager.getImageFromPath(key.getIconPath()));
    }

    @FXML
    private void close(Event e) {
        System.exit(0);
    }
    
    private void sortCatListForName(){
//        Collections.sort(catList.getCategoryList());
    }
    
    private void sortKeyListForName(){
//        for (Category c : catList.getCategoryList()){
//            Collections.sort(c.getKeylist());
//        }
    }
    
    private void sortTree(){
        sortCatListForName();
        sortKeyListForName();
        initTree();
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
        

        System.out.println(debug);
        debugLog("Try to initialize the Program");
        debugLog("------------------------------");
        debugLog("");

        // Check availibility of the Structure.xml
//        new XMLManager().checkAvailibility();
        debugLog("Structure.xml founded or created");

        //Load the ini-Files and provide data
        sm_icons = new SettingManager("AppData/icons.properties");
        

        debugLog("Property and Ini- Files are loaded");

        // Set up the Language
        initLanguage();
        debugLog("Language configured");

//        catList = xmlManager.returnListofCategories();
        debugLog("Get converted List from Structure.xml");

        String userAvatar = null;
        

        btn_addCat.setGraphic(new ImageView(addCatImage));
        btn_remove.setGraphic(new ImageView(trashImage));
        btn_addKey.setGraphic(new ImageView(keyImage));
        settingsItem.setGraphic(new ImageView(settingImage));
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
//                            selectedKey = new XMLManager().returnKey(selectedItem.getValue().toString());
//                            selectedCategory = new XMLManager().returnSingleCategory(selectedItem.getParent().getValue().toString());
                            tf_keyname.setText(selectedKey.getKeyname());
                            tf_username.setText(selectedKey.getUsername());
                            tf_description.setText(selectedKey.getDescription());
                            tf_password.setText(selectedKey.getPassword());
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
//                            selectedCategory = new XMLManager().returnSingleCategory(selectedItem.getValue().toString());
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
