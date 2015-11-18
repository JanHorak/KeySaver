/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import net.jan.keysaver2.manager.LoggingManager;
import net.jan.keysaver2.manager.SettingManager;
import net.jan.keysaver2.manager.UpdateManager;
import net.jan.keysaver2.sources.EnumClientVersionStatus;
import net.jan.keysaver2.sources.EnumNotification;

/**
 * FXML Controller class
 *
 * @author janhorak
 */
public class UpdateDialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ProgressBar bar;
    @FXML
    private ProgressIndicator indicator;
    @FXML
    private Label lb_info;
    @FXML
    private Label lb_progress;
    @FXML
    private Button btn_checkVersion;
    @FXML
    private Button btn_download;
    @FXML
    private Button btn_close;
    @FXML
    private ImageView image_connectionstatus;
    @FXML
    private Hyperlink help;
    
    private SettingManager sm_icon;
    boolean connection = false;
    File downloadLocation = new File("");

    
    Task downloadTask = new Task<Void>() {

        @Override
        protected Void call() throws Exception {
            updateProgress(-1, -1);
            UpdateManager.downloadAndSave(UpdateManager.getFilePathForDownload(), downloadLocation.getAbsolutePath() + "/KeySaver2.0_" + UpdateManager.getCurrentVersionFromGitHub() + ".zip");
            done();
            return null;
        }

        public void done() {
            updateProgress(100, 100);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    btn_download.setDisable(false);
                }
            });
        }
    };

    private void initLang() {

    }
    
    @FXML
    private void openHelpFile() {
        try {
            Desktop.getDesktop().open(new File("AppData/Help/Update_help.html"));
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("openHelpFile()", ex);
        }
    }

    @FXML
    private void checkConnectionAndVersion() {

    }

    @FXML
    private void downloadNewVersion() {
        DirectoryChooser chooser = new DirectoryChooser();
        downloadLocation = chooser.showDialog(null);
        if (downloadLocation != null) {
            lb_progress.textProperty().bind(downloadTask.messageProperty());
            indicator.progressProperty().bind(downloadTask.progressProperty());
            bar.progressProperty().bind(downloadTask.progressProperty());
            new Thread(downloadTask).start();
        }
    }
    
    @FXML
    private void cancel(ActionEvent actionEvent) {
        Node source = (Node) actionEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sm_icon = new SettingManager("AppData/icons.properties");
        btn_download.setDisable(true);
        initLang();
        
    }

}
