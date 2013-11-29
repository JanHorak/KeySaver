/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.updatedialog;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import net.jan.keysaver.beans.Language_Singleton;
import net.jan.keysaver.beans.Settings_Singleton;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.manager.SettingManager;
import net.jan.keysaver.manager.UpdateManager;
import net.jan.keysaver.sources.EnumClientVersionStatus;
import net.jan.keysaver.sources.Utilities;

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
    private Language_Singleton langBean;
    private SettingManager sm_icon;
    boolean connection = false;
    

    Task task = new Task<Boolean>() {
        boolean live = true;
        boolean con = false;

        public Boolean call() {
            updateMessage(langBean.getValue("CHECKCONN"));
            updateProgress(-1, -1);
            while (live) {
                con = UpdateManager.checkInternetAvailibility();
                if (con) {
                    kill();
                    updateMessage("...done");
                    connection = true;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                image_connectionstatus.setImage(FileManager.getImageFromPath(sm_icon.returnProperty("CONNECTION_ON")));
                            } catch (IOException ex) {
                                Logger.getLogger(UpdateDialogController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });

                    updateMessage(langBean.getValue("REQUESTING"));
                    updateProgress(-1, -1);
                    String version = UpdateManager.getCurrentVersionFromGitHub();
                    if (UpdateManager.isActualVersionOlder(version).equals(EnumClientVersionStatus.actual)) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                btn_download.setDisable(true);
                            }
                        });
                        updateMessage(langBean.getValue("STATUS_ACTUAL"));
                        updateProgress(100, 100);
                    }
                    if (UpdateManager.isActualVersionOlder(version).equals(EnumClientVersionStatus.old)) {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                btn_download.setDisable(false);
                            }
                        });
                        updateMessage(langBean.getValue("STATUS_OLD"));
                        updateProgress(100, 100);
                    }

                } else {
                    updateProgress(0, 0);
                    updateMessage(langBean.getValue("CHECKCONN_FAIL"));
                }
            }

            return con;
        }
        private void kill() {
            live = false;
        }
    };

    private void initLang() {
        lb_info.setText(langBean.getValue("INTERNETCONNEEDED"));
        btn_checkVersion.setText(langBean.getValue("CHECKUPDATES"));
        btn_close.setText(langBean.getValue("CLOSE"));
        btn_download.setText(langBean.getValue("DOWNLOAD"));
    }

    @FXML
    private void checkConnectionAndVersion() {
        btn_checkVersion.setDisable(true);
        System.out.println(UpdateManager.getFilePathForDownload());
        lb_progress.textProperty().bind(task.messageProperty());
        indicator.progressProperty().bind(task.progressProperty());
        bar.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }

    @FXML
    private void downloadNewVersion(){
        DirectoryChooser chooser = new DirectoryChooser();
        File downloadLocation = chooser.showDialog(null);
        if ( downloadLocation != null ){
            UpdateManager.downloadAndSave(UpdateManager.getFilePathForDownload(), downloadLocation.getAbsolutePath()+"KeySaver2.0_"+Settings_Singleton.getInstance().getVersion()+".zip");
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        langBean = Language_Singleton.getInstance();
        sm_icon = new SettingManager("AppData/icons.properties");
        btn_download.setDisable(true);
        initLang();
        try {
            image_connectionstatus.setImage(FileManager.getImageFromPath(sm_icon.returnProperty("CONNECTION_OFF")));
        } catch (IOException ex) {
            Logger.getLogger(UpdateDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
