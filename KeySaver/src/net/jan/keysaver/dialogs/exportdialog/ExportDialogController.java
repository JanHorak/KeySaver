/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.exportdialog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
import net.jan.keysaver.manager.LoggingManager;
import net.jan.keysaver.sources.Utilities;

/**
 * FXML Controller class
 *
 * @author janhorak
 */
public class ExportDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private CheckBox chk_exportDecrypted;
    @FXML
    private CheckBox chk_exportKey;
    @FXML
    private CheckBox chk_exportZipped;
    @FXML
    private Label lb_pathLabel;
    @FXML
    private Label lb_path;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_export;
    @FXML
    private Button btn_browse;
    private File placeForExport;

    @FXML
    private void cancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void browse() {
        DirectoryChooser chooser = new DirectoryChooser();
        placeForExport = chooser.showDialog(null);
        lb_path.setText(placeForExport.getAbsolutePath());
    }

    @FXML
    public void export() {
        File structure = new File("AppData/structure.xml");
        File keyFile = new File("AppData/private.key");
        String exportPath_zip = placeForExport + File.separator + "KeySaver_export.zip";
        if (chk_exportDecrypted.isSelected()) {
            structure = new Decryption().returnDecryptedFile(structure, structure.getAbsolutePath(), keyFile.getAbsolutePath());
        }
        if (chk_exportZipped.isSelected()) {
            if (chk_exportKey.isSelected()) {
                Utilities.generateZip(exportPath_zip, structure, keyFile);
            } else {
                Utilities.generateZip(exportPath_zip, structure);
            }
            structure = new Encryption().returnEncryptedFile(structure, structure.getAbsolutePath(), keyFile.getAbsolutePath());
        } else {
            if (chk_exportKey.isSelected()) {
                try {
                    Files.copy(keyFile.toPath(), new File(placeForExport + File.separator + "private.key").toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                } catch (IOException ex) {
                    LoggingManager.writeToErrorFile("ExportDialogController: export failed (copy of Files)", ex);
                }
            }
            try {
                Files.copy(structure.toPath(), new File(placeForExport + File.separator + "structure.xml").toPath(), StandardCopyOption.COPY_ATTRIBUTES);
                structure = new Encryption().returnEncryptedFile(structure, structure.getAbsolutePath(), keyFile.getAbsolutePath());
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("ExportDialogController: export failed (copy of Files)", ex);
            }
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
