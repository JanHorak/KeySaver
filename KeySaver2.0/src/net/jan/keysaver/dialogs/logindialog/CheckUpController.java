/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.logindialog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BlendMode;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
import net.jan.keysaver.manager.SettingManager;
import net.jan.keysaver.sources.Utilities;

/**
 *
 * @author janhorak
 */
public class CheckUpController {
    
    @FXML
    private ProgressIndicator indicator;
    @FXML
    private ProgressBar bar_main;
    @FXML
    private ProgressBar bar_collecting;
    @FXML
    private ProgressBar bar_reading;
    @FXML
    private ProgressBar bar_exporting;
    @FXML
    private ProgressBar bar_testing;
    @FXML
    private Label lb_main;
    @FXML
    private Label lb_coll;
    @FXML
    private Label lb_read;
    @FXML
    private Label lb_exp;
    @FXML
    private Label lb_test;
    @FXML
    private TextArea checkUpArea;
    @FXML
    private Button btn_checkUp;
    
    @FXML
    private void checkUp() {
        indicator.setBlendMode(BlendMode.DARKEN);
        setImModeBarAndIndicator();
        btn_checkUp.setDisable(true);
        lb_coll.setText("Collecting Data...");
        checkUpArea.appendText("Collecting...");
        List<File> fileList_global = new ArrayList<>();
        boolean checkUpOk = true;
        File errorLog = new File("AppData/Error.log");
        File propDE = new File("AppData/Lang_DE.properties");
        File propEN = new File("AppData/Lang_EN.properties");
        File logLog = new File("AppData/Logging.log");
        File propIcon = new File("AppData/icons.properties");
        File key = new File("AppData/private.key");
        File structure = new File("AppData/structure.xml");
        File ini = new File("settings.ini");
        String[] images2 = new File("AppData/Images/Avatars/").list();
        String[] images3 = new File("AppData/Images/intern/").list();

        fileList_global.add(errorLog);
        fileList_global.add(propDE);
        fileList_global.add(propEN);
        fileList_global.add(propIcon);
        fileList_global.add(logLog);
        fileList_global.add(key);
        fileList_global.add(structure);
        fileList_global.add(ini);

        List<File> fileList_test = new ArrayList<>();
        fileList_test.addAll(fileList_global);

        List<File> fileList_avatars = new ArrayList<>();
        for (String s : images2) {
            File f = new File("AppData/Images/Avatars/" + s);
            fileList_avatars.add(f);
        }
        List<File> fileList_intern = new ArrayList<>();
        for (String s : images3) {
            File f = new File("AppData/Images/intern/" + s);
            fileList_intern.add(f);
        }

        bar_collecting.setProgress(100);
        lb_coll.setText("...done");
        checkUpArea.appendText("\n ...done \n");
        checkUpArea.appendText("Important Files founded: " + fileList_global.size() + "\n");
        double step = ((double) fileList_global.size() / 100);

        resetBarAndIndicator();

        checkUpArea.appendText("Testing Files...\n");
        lb_read.setText("Testing Files...");
        for (File f : fileList_global) {
            if (f.exists()) {
                bar_main.setProgress(step);
                indicator.setProgress(step);
                step += step;
            } else {
                checkUpOk = false;
                checkUpArea.appendText("ERROR:\nCant find File: " + f.getName() + "\n");
            }
        }
        checkUpArea.appendText("...done\n");
        lb_read.setText("...done\n");
        bar_reading.setProgress(step);

        resetBarAndIndicator();

        checkUpArea.appendText("Preparing export and Backup...\n");

        Utilities.generateZip("intern.zip", Utilities.getPathesFromFileList(fileList_intern));
        checkUpArea.appendText("intern.zip created temporary...\n");
        Utilities.generateZip("avatars.zip", Utilities.getPathesFromFileList(fileList_avatars));
        checkUpArea.appendText("avatars.zip created temporary...\n");
        fileList_global.add(new File("intern.zip"));
        fileList_global.add(new File("avatars.zip"));
        checkUpArea.appendText("Try to create Backup from collected Data...\n");
        Utilities.generateZip("AppData/Backups/KeySaver2.0_Backup_" + new Date() + ".zip", Utilities.getPathesFromFileList(fileList_global));
        checkUpArea.appendText("KeySaver2.0_Backup_" + new Date() + ".zip" + " created. \n"
                + "cleaning up...\n\n");
        new File("intern.zip").delete();
        new File("avatars.zip").delete();
        bar_exporting.setProgress(100);
        lb_exp.setText("...done\n");

        setImModeBarAndIndicator();
        checkUpArea.appendText("Starting Tests...\n");
        checkUpArea.appendText("===========\n");
        step = ((double) fileList_global.size() / 100);

        resetBarAndIndicator();
        for (File f : fileList_test) {
            checkUpArea.appendText(f.getName() + "\n");
            if (!f.getName().endsWith(".log")) {
                if (f.exists() && f.length() > 0) {
                    setModeBarAndIndicator(step, step);
                    checkUpArea.appendText("...ok\n");
                    step += step;
                } else {
                    checkUpOk = false;
                    writeError("EROOR\n");
                }
            } else {
                checkUpArea.appendText("...skipped\n");
            }
        }
        bar_testing.setProgress(50);
        checkUpArea.appendText("Simple Tests done!\n\n");

        checkUpArea.appendText("Starting next Tests...\n");
        checkUpArea.appendText("===========\n");

        try {
            structure = new Decryption().returnDecryptedFile(structure, structure.getAbsolutePath(), key.getAbsolutePath());
        } catch (Exception e) {
            checkUpOk = false;
            writeError("Something goes wrong with Decryption!\n");
            writeError(e.getLocalizedMessage() + "\n");
        }

        if (structure.length() > 0) {
            try {
                structure = new Encryption().returnEncryptedFile(structure, structure.getAbsolutePath(), key.getAbsolutePath());
            } catch (Exception e) {
                checkUpOk = false;
                writeError("Something goes wrong with Encryption!\n");
                writeError(e.getLocalizedMessage() + "\n");
            }
        }
        
        checkUpArea.appendText("En- and Decryptiontests done...\n\n");
        bar_main.setProgress(75);
        
        
        checkUpArea.appendText("Starting next Tests...\n");
        checkUpArea.appendText("===========\n");
        
        SettingManager icons = new SettingManager(propIcon.getAbsolutePath());
        List<String> values = new ArrayList<>();
        try {
            values = icons.returnAllValues();
        } catch (IOException ex) {
            Logger.getLogger(LoginDialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        for ( String s : values ){
            File f = new File(s);
            if (!f.exists()){
                checkUpOk = false;
                writeError("Wrong Reference: \n");
                writeError("Cant find: " +f.getAbsolutePath()+"\n");
            }
        }
        checkUpArea.appendText("Propertiestests done...\n\n");
        bar_testing.setProgress(100);
        lb_test.setText("...done");
        
        
        checkUpArea.appendText("Tests finished\n");
        checkUpArea.appendText("===========\n");
        checkUpArea.appendText(checkUpOk ? "You can start without problems" : "Errors occured! Please consult the programmer!");

    }

    private void resetBarAndIndicator() {
        bar_main.setProgress(0);
        indicator.setProgress(0);
    }

    private void setImModeBarAndIndicator() {
        bar_main.setProgress(-1);
        indicator.setProgress(-1);
    }

    private void setModeBarAndIndicator(double bar, double indi) {
        bar_main.setProgress(bar);
        indicator.setProgress(indi);
    }

    private void writeError(String text) {
        checkUpArea.appendText(text);
    }
    
}
