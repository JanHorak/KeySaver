/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.jan.keysaver2.manager.ValidationManager;
import net.jan.keysaver2.sources.Utilities;
import net.jan.keysaver2.validationentities.ImportEntity;
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
    private Label lb_zipPath;
    @FXML
    private Label lb_error;
    @FXML
    private Button btn_browse_zip;
    @FXML
    private Button btn_cancel_zip;
    @FXML
    private Button btn_import;
    @FXML
    private Button btn_cancel;
    private File wantToImport;

    @FXML
    private void browseZip() {
        browse(lb_zipPath, "Zip- File", "KeySaver2.0_export.zip");
    }

    @FXML
    private void clearZip() {
        lb_zipPath.setText("");
        lb_error.setVisible(false);
    }

    private void browse(Label pathLabel, String filterHint, String filter) {
        lb_error.setVisible(false);
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterHint, filter));
        wantToImport = chooser.showOpenDialog(null);
        if (wantToImport != null) {
            pathLabel.setText(wantToImport.getAbsolutePath());
        }
    }

    @FXML
    private void close(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void importData(ActionEvent event) {
        ImportEntity im = new ImportEntity();
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
            String pathInifile = "";
            for (String s : tmpList) {
                if (s.equals("settings.ini")) {
                    pathInifile = folder_pathTempFolder + "/" + s;
                } else {
                    coreFilePathes.add(folder_pathTempFolder + "/" + s);
                }
            }
            
            Utilities.copyFiles(coreFilePathes, folder_appData.getAbsolutePath(), StandardCopyOption.REPLACE_EXISTING);
            coreFilePathes.clear();
            coreFilePathes.add(pathInifile);
            Utilities.copyFiles(coreFilePathes, new File("").getAbsolutePath().toString(), StandardCopyOption.REPLACE_EXISTING);
            try {
                FileUtils.deleteDirectory(folder_pathTempFolder);
            } catch (IOException ex) {
                Logger.getLogger(ImportDialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
            close(event);

            //invalid
        } else {
            lb_error.setVisible(true);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lb_error.setText("Error - File not valid!");
        lb_error.setVisible(false);
    }
}
