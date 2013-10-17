/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.exportdialog;

import java.awt.Desktop;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
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
    @FXML
    private Hyperlink hyper_help;
    private File placeForExport;

    @FXML
    private void getHelp() {
        try {
            Desktop.getDesktop().open(new File("AppData/Help/HelpMainpage.html"));
        } catch (IOException ex) {
            Logger.getLogger(ExportDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
    public void export(ActionEvent actionEvent) {
        File file_structure = new File("AppData/structure.xml");
        File file_key = new File("AppData/private.key");
        File file_ini = new File("settings.ini");
        File file_iconProp = new File("AppData/icons.properties");

        List<String> listOfImages = Utilities.getFilePathesFromFolder("AppData/Images/intern");
        Utilities.generateZip("images.zip", listOfImages);

        String exportPath_zip = placeForExport + File.separator + "KeySaver2.0_export.zip";

        if (chk_exportDecrypted.isSelected()) {
            file_structure = new Decryption().returnDecryptedFile(file_structure, file_structure.getAbsolutePath(), file_key.getAbsolutePath());
        }
        // Collecting Data
        List<String> files2Zip = new ArrayList<>();
        files2Zip.add(file_structure.getAbsolutePath());
        files2Zip.add(file_key.getAbsolutePath());
        files2Zip.add(file_ini.getAbsolutePath());
        files2Zip.add(file_iconProp.getAbsolutePath());
        files2Zip.add("images.zip");
        if (chk_exportZipped.isSelected()) {
            Utilities.generateZip(exportPath_zip, files2Zip);
        } else {
            Utilities.copyFiles(files2Zip, placeForExport.toString(), StandardCopyOption.REPLACE_EXISTING);
        }

        if (chk_exportDecrypted.isSelected()) {
            file_structure = new Encryption().returnEncryptedFile(file_structure, file_structure.getAbsolutePath(), file_key.getAbsolutePath());
        }
        new File("images.zip").delete();

        cancel(actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
}
