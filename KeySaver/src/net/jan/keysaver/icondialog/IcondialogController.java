/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.icondialog;

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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import net.jan.keysaver.beans.Language_Singleton;
import net.jan.keysaver.beans.Settings_Singelton;
import net.jan.keysaver.manager.LoggingManager;
import net.jan.keysaver.manager.SettingManager;

/**
 * FXML Controller class
 *
 * @author Jan Horak
 */
public class IcondialogController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ListView<String> dataList;
    @FXML
    private Label dyn_lb_fileName;
    @FXML
    private Label dyn_lb_size;
    @FXML
    private Label dyn_lb_kindOfFile;
    @FXML
    private Label dyn_lb_preview;
    @FXML
    private Label lb_fileName;
    @FXML
    private Label lb_size;
    @FXML
    private Label lb_kindOfFile;
    @FXML
    private Label lb_preview;
    @FXML
    private Button btn_save;
    @FXML
    private Button btn_cancel;
    @FXML
    private Button btn_edit;
    private String selectedProperty = "";
    private File selectedFile;
    private Language_Singleton langBean;
    private Settings_Singelton settingBean;
    private SettingManager sm_icons = new SettingManager("AppData/icons.properties");

    @FXML
    private void loadUpImage() {
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg"));
        chooser.setTitle(langBean.getValue("FILECHOOSERTITLE"));
        chooser.setInitialDirectory(new File("AppData/Images/intern"));



        selectedFile = chooser.showOpenDialog(null);

        if (selectedFile != null) {
            dyn_lb_fileName.setText(selectedFile.getName());
            dyn_lb_kindOfFile.setText(returnEnd(lb_fileName.getText()));
            ImageView image = null;
            try {
                image = new ImageView(new Image(new FileInputStream(new File(selectedFile.getAbsoluteFile().toString()))));
            } catch (FileNotFoundException ex) {
                Logger.getLogger(IcondialogController.class.getName()).log(Level.SEVERE, null, ex);
            }
            dyn_lb_preview.setGraphic(image);
            dyn_lb_size.setText(returnSize(image));
        }

        btn_save.setDisable(false);
        btn_edit.setDisable(true);
        btn_cancel.setDisable(false);
    }

    @FXML
    private void cancelEdit() {
        btn_save.setDisable(true);
        btn_edit.setDisable(true);
        btn_cancel.setDisable(true);
        selectedProperty = "";
        selectedFile = null;
        dyn_lb_fileName.setText("");
        dyn_lb_kindOfFile.setText("");
        dyn_lb_preview.setText("");
        dyn_lb_preview.setGraphic(null);
        dyn_lb_size.setText("");
    }
    
    @FXML
    private void saveEdit(){
        try {
            sm_icons.storeProperty(selectedProperty,selectedFile.getAbsolutePath() );
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IcondialogController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(IcondialogController.class.getName()).log(Level.SEVERE, null, ex);
        }
        cancelEdit();
    }

    private String returnSize(ImageView image) {
        return image.getImage().getHeight() + "x" + image.getImage().getWidth();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        settingBean = Settings_Singelton.getInstance();
        langBean = Language_Singleton.getInstance();

        lb_fileName.setText(langBean.getValue("FILENAME") + ":");
        lb_kindOfFile.setText(langBean.getValue("KINDOFFILE") + ":");
        lb_preview.setText(langBean.getValue("PREVIEW") + ":");
        lb_size.setText(langBean.getValue("SIZE") + ":");

        btn_cancel.setText(langBean.getValue("CANCEL"));
        btn_edit.setText(langBean.getValue("EDIT"));
        btn_save.setText(langBean.getValue("SAVE"));

        btn_edit.setDisable(true);
        btn_save.setDisable(true);
        btn_cancel.setDisable(true);

        ObservableList<String> data = FXCollections.observableArrayList();
        final SettingManager sm = new SettingManager("AppData/icons.properties");
        List<Object> obList = new ArrayList<>();
        try {
            obList = sm.returnAllProperties();
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("IcondialogController: Failed to load ObjectList", ex);
        }
        for (Object o : obList) {
            data.add(o.toString());
        }
        dataList.setItems(data);
        dataList.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                selectedProperty = ov.getValue();
                dyn_lb_fileName.setText(getFileName());
                dyn_lb_kindOfFile.setText(getSuffix());
                dyn_lb_preview.setText("");
                dyn_lb_preview.setGraphic(getImage());
                dyn_lb_size.setText(getImageSize(getImage()));
                if (selectedProperty == null || selectedProperty.isEmpty()) {
                    btn_edit.setDisable(true);
                } else {
                    btn_edit.setDisable(false);
                }
            }

            private ImageView getImage() {
                ImageView image = new ImageView();
                try {
                    image = new ImageView(new Image(new FileInputStream(sm.returnProperty(selectedProperty))));
                } catch (FileNotFoundException ex) {
                    LoggingManager.writeToErrorFile(null, ex);
                } catch (IOException ex) {
                    LoggingManager.writeToErrorFile(null, ex);
                }
                return image;
            }

            private String getFileName() {
                String tmp = "";
                try {
                    tmp = sm.returnProperty(selectedProperty);
                } catch (IOException ex) {
                    LoggingManager.writeToErrorFile(null, ex);
                }
                File f = new File(tmp);
                return f.getName();
            }

            private String getImageSize(ImageView image) {
                return image.getImage().getHeight() + "x" + image.getImage().getWidth();
            }

            private String getSuffix() {
                String tmp = "";
                try {
                    tmp = sm.returnProperty(selectedProperty);
                } catch (IOException ex) {
                    LoggingManager.writeToErrorFile(null, ex);
                }
                String suffix = returnEnd(tmp);
                return suffix;
            }
        });
    }

    private String returnEnd(String fileName) {
        String suffix = "unknown";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".JPG")) {
            suffix = "JPG";
        } else if (fileName.endsWith(".png") || fileName.endsWith(".PNG")) {
            suffix = "PNG";
        }
        return suffix;
    }
}
