/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.importdialog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.jan.keysaver.beans.Language_Singleton;
import net.jan.keysaver.manager.ValidationManager;
import net.jan.keysaver.sources.Utilities;
import net.jan.keysaver.validationentities.ImportEntity;
import org.apache.commons.io.FileUtils;

/**
 * FXML Controller class
 *
 * @author janhorak
 */
public class ImportDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Label lb_zip;
    @FXML
    private Label lb_mainFile;
    @FXML
    private Label lb_avatars;
    @FXML
    private Label lb_avatarsPath;
    @FXML
    private Label lb_keyFile;
    @FXML
    private Label lb_keyFilePath;
    @FXML
    private Label lb_iconPropFile;
    @FXML
    private Label lb_iconPropPath;
    @FXML
    private Label lb_imagesZipFile;
    @FXML
    private Label lb_imagesZipPath;
    @FXML
    private Label lb_iniFile;
    @FXML
    private Label lb_iniFilePath;
    @FXML
    private Label lb_zipPath;
    @FXML
    private Label lb_mainFilePath;
    @FXML
    private Label lb_error;
    @FXML
    private Button btn_browse_zip;
    @FXML
    private Button btn_browse_avatars;
    @FXML
    private Button btn_browse_mainFile;
    @FXML
    private Button btn_browse_key;
    @FXML
    private Button btn_browse_images;
    @FXML
    private Button btn_browse_Iconproperties;
    @FXML
    private Button btn_browse_ini;
    @FXML
    private Button btn_cancel_zip;
    @FXML
    private Button btn_cancel_mainFile;
    @FXML
    private Button btn_cancel_key;
    @FXML
    private Button btn_cancel_avatars;
    @FXML
    private Button btn_cancel_iconProp;
    @FXML
    private Button btn_cancel_images;
    @FXML
    private Button btn_cancel_ini;
    @FXML
    private TitledPane zipPane;
    @FXML
    private TitledPane singlePane;
    @FXML
    private Button btn_import;
    @FXML
    private Button btn_cancel;
    private File wantToImport;
    private Language_Singleton languageBean;

    @FXML
    private void browseZip() {
        browse(lb_zipPath, "Zip- File", "KeySaver2.0_export.zip");
    }

    @FXML
    private void browseAvatars() {
        browse(lb_avatarsPath, "Zip- File", "avatars.zip");
    }

    @FXML
    private void browseXML() {
        browse(lb_mainFilePath, "Structure- File", "structure.xml");
    }

    @FXML
    private void browseKey() {
        browse(lb_keyFilePath, "Key- File", "private.key");
    }

    @FXML
    private void browseImages() {
        browse(lb_imagesZipPath, "Images- ZipFile", "images.zip");
    }

    @FXML
    private void browseini() {
        browse(lb_iniFilePath, "Config- File", "settings.ini");
    }

    @FXML
    private void browseIcons() {
        browse(lb_iconPropPath, "IconProperties- File", "icons.properties");
    }

    @FXML
    private void clearZip() {
        clear(lb_zipPath);
        lb_error.setVisible(false);
    }

    @FXML
    private void clearAvatars() {
        clear(lb_avatarsPath);
        lb_error.setVisible(false);
    }

    @FXML
    private void clearImages() {
        clear(lb_imagesZipPath);
        lb_error.setVisible(false);
    }

    @FXML
    private void clearIcons() {
        clear(lb_iconPropPath);
        lb_error.setVisible(false);
    }

    @FXML
    private void clearKey() {
        clear(lb_keyFilePath);
        lb_error.setVisible(false);
    }

    @FXML
    private void clearXML() {
        clear(lb_mainFilePath);
        lb_error.setVisible(false);
    }

    @FXML
    private void clearIni() {
        clear(lb_iniFilePath);
        lb_error.setVisible(false);
    }

    private void browse(Label pathLabel, String filterHint, String filter) {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterHint, filter));
        wantToImport = chooser.showOpenDialog(null);
        if (wantToImport != null) {
            pathLabel.setText(wantToImport.getAbsolutePath());
        }
    }

    private void clear(Label label) {
        lb_error.setVisible(false);
        label.setText("");
    }

    private void setUpLanguage() {
        zipPane.setText(languageBean.getValue("ZIPHEADER"));
        singlePane.setText(languageBean.getValue("SINGLEFILES"));
        //Labels
        lb_avatars.setText(languageBean.getValue("AVATARZIP"));
        lb_iconPropFile.setText(languageBean.getValue("ICONPROP"));
        lb_imagesZipFile.setText(languageBean.getValue("IMAGESZIP"));
        lb_iniFile.setText(languageBean.getValue("INIFILE"));
        lb_keyFile.setText(languageBean.getValue("KEYFILE"));
        lb_mainFile.setText(languageBean.getValue("MAINFILE"));
        lb_zip.setText(languageBean.getValue("ZIPHEADER"));
        lb_error.setText(languageBean.getValue("ERROR_IMPORTINVALID"));
        //Buttons
        btn_browse_Iconproperties.setText(languageBean.getValue("BROWSE"));
        btn_browse_avatars.setText(languageBean.getValue("BROWSE"));
        btn_browse_images.setText(languageBean.getValue("BROWSE"));
        btn_browse_ini.setText(languageBean.getValue("BROWSE"));
        btn_browse_key.setText(languageBean.getValue("BROWSE"));
        btn_browse_mainFile.setText(languageBean.getValue("BROWSE"));
        btn_browse_zip.setText(languageBean.getValue("BROWSE"));
        btn_cancel.setText(languageBean.getValue("CANCEL"));
        btn_cancel_avatars.setText(languageBean.getValue("CANCEL"));
        btn_cancel_iconProp.setText(languageBean.getValue("CANCEL"));
        btn_cancel_images.setText(languageBean.getValue("CANCEL"));
        btn_cancel_ini.setText(languageBean.getValue("CANCEL"));
        btn_cancel_key.setText(languageBean.getValue("CANCEL"));
        btn_cancel_mainFile.setText(languageBean.getValue("CANCEL"));
        btn_cancel_zip.setText(languageBean.getValue("CANCEL"));
        btn_import.setText(languageBean.getValue("IMPORT"));

    }

    @FXML
    private void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void importData() {
        ImportEntity im = new ImportEntity();
        im.setAvatarZip(lb_avatarsPath.getText());
        im.setIconProps(lb_iconPropPath.getText());
        im.setImagesZip(lb_imagesZipPath.getText());
        im.setIniFile(lb_iniFilePath.getText());
        im.setKey(lb_keyFilePath.getText());
        im.setXml(lb_mainFilePath.getText());

        im.setGlobalZip(lb_zipPath.getText());

        File folder_pathTempFolder = new File("AppData/importTemp");
        File file_pathAvatarZip = new File("AppData/importTemp/avatars.zip");
        File file_pathImagesZip = new File("AppData/importTemp/images.zip");
        File folder_appData = new File("AppData/");
        File folder_avatars = new File("AppData/Images/Avatars");
        File folder_images = new File("AppData/Images/intern");


        File folder_exportedAvatars = new File("AppData/importTemp/avatars");
        File folder_exportedImages = new File("AppData/importTemp/images");

        List<String> coreFilePathes = new ArrayList<>();

        if (ValidationManager.isValid(im)) {
            if (!lb_zipPath.getText().isEmpty()) {
                Utilities.decompressZip(new File(lb_zipPath.getText()), folder_pathTempFolder.getAbsolutePath());
                Utilities.decompressZip(file_pathAvatarZip, folder_exportedAvatars.getAbsolutePath());
                Utilities.decompressZip(file_pathImagesZip, folder_exportedImages.getAbsolutePath());

                file_pathAvatarZip.delete();
                file_pathImagesZip.delete();

                Utilities.copyFiles(Utilities.getFilePathesFromFolder(folder_exportedImages.getAbsolutePath()), folder_images.getAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
                Utilities.copyFiles(Utilities.getFilePathesFromFolder(folder_exportedAvatars.getAbsolutePath()), folder_avatars.getAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
                try {
                    FileUtils.deleteDirectory(folder_exportedAvatars);
                    FileUtils.deleteDirectory(folder_exportedImages);
                } catch (IOException ex) {
                    Logger.getLogger(ImportDialogController.class.getName()).log(Level.SEVERE, null, ex);
                }

                String[] tmpList = folder_pathTempFolder.list();

                for (String s : tmpList) {
                    coreFilePathes.add(s);
                }

                Utilities.copyFiles(coreFilePathes, folder_appData.getAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
                try {
                    FileUtils.deleteDirectory(folder_pathTempFolder);
                } catch (IOException ex) {
                    Logger.getLogger(ImportDialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Utilities.decompressZip(new File(lb_avatarsPath.getText()), folder_exportedAvatars.getAbsolutePath());
                Utilities.decompressZip(new File(lb_imagesZipPath.getText()), folder_exportedImages.getAbsolutePath());
                
                Utilities.copyFiles(Utilities.getFilePathesFromFolder(folder_exportedImages.getAbsolutePath()), folder_images.getAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
                Utilities.copyFiles(Utilities.getFilePathesFromFolder(folder_exportedAvatars.getAbsolutePath()), folder_avatars.getAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
                
                try {
                    FileUtils.deleteDirectory(folder_exportedAvatars);
                    FileUtils.deleteDirectory(folder_exportedImages);
                    FileUtils.deleteDirectory(folder_pathTempFolder);
                } catch (IOException ex) {
                    Logger.getLogger(ImportDialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    Files.copy(new File(lb_mainFilePath.getText()).toPath(), new File("AppData/structure.xml").toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(new File(lb_iconPropPath.getText()).toPath(), new File("AppData/icons.properties").toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(new File(lb_keyFilePath.getText()).toPath(), new File("AppData/private.key").toPath(), StandardCopyOption.REPLACE_EXISTING);
                    Files.copy(new File(lb_iniFilePath.getText()).toPath(), new File("settings.ini").toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ex) {
                    Logger.getLogger(ImportDialogController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            close(new ActionEvent());

            //invalid
        } else {
            lb_error.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        languageBean = Language_Singleton.getInstance();
        lb_error.setVisible(false);
        setUpLanguage();
    }
}
