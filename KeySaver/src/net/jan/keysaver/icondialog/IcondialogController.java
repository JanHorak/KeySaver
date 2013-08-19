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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Label lb_fileName;
    @FXML
    private Label lb_size;
    @FXML
    private Label lb_kindOfFile;
    @FXML
    private Label lb_preview;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> data = FXCollections.observableArrayList();
        final SettingManager sm = new SettingManager("AppData\\icons.properties");
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
            private String selectedProperty = "";
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                selectedProperty = ov.getValue();
                lb_fileName.setText(getFileName());
                lb_kindOfFile.setText(getSuffix());
                lb_preview.setText("");
                lb_preview.setGraphic(getImage());
                lb_size.setText(getImageSize(getImage()));
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
            
            private String getFileName(){
                String tmp = "";
                try {
                    tmp = sm.returnProperty(selectedProperty);
                } catch (IOException ex) {
                    LoggingManager.writeToErrorFile(null, ex);
                }
                File f = new File(tmp);
                return f.getName();
            }
            
            private String getImageSize(ImageView image){
                return image.getImage().getHeight() + "x" +image.getImage().getWidth();
            }
            
            private String getSuffix(){
                String tmp = "";
                try {
                    tmp = sm.returnProperty(selectedProperty);
                } catch (IOException ex) {
                    LoggingManager.writeToErrorFile(null, ex);
                }
                String suffix = "unknown";
                if ( tmp.endsWith(".jpg") || tmp.endsWith(".JPG") ){
                    suffix = "JPG";
                } else if (tmp.endsWith(".png") || tmp.endsWith(".PNG") ){
                    suffix = "PNG";
                }
                return suffix;
            }
        });
    }
}
