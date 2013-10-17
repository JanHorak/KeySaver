/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.dialogs.importdialog;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import net.jan.keysaver.beans.Language_Singleton;
import net.jan.keysaver.validators.Validator;

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
    private Label lb_zipPath;

    @FXML
    private Label lb_mainFilePath;
    
    @FXML
    private Label lb_error;
    
    @FXML
    private Button btn_browse_zip;

    @FXML
    private Button btn_browse_mainFile;
    
    @FXML
    private Button btn_cancel_zip;

    @FXML
    private Button btn_cancel_mainFile;
    
    @FXML
    private Button btn_import;
    
    @FXML
    private Button btn_cancel;
    
    private File wantToImport;
    private Language_Singleton languageBean;
    
    @FXML
    private void browseZip(){
        browse(lb_zipPath,"Zip- File", "KeySaver_export.zip");
    }
    @FXML
    private void browseXML(){
        browse(lb_mainFilePath,"Structure- File", "structure.xml");
    }

    
    @FXML
    private void clearZip(){
        clear(lb_zipPath);
        lb_error.setVisible(false);
        btn_import.setDisable(!Validator.validateImportButton(lb_zipPath.getText(), lb_mainFilePath.getText()));
    }
    
    @FXML
    private void clearXML(){
        clear(lb_mainFilePath);
        lb_error.setVisible(false);
        btn_import.setDisable(!Validator.validateImportButton(lb_zipPath.getText(), lb_mainFilePath.getText()));
    }  
    
    private void browse(Label pathLabel, String filterHint, String filter){
        FileChooser chooser = new FileChooser();
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(filterHint, filter));
        wantToImport = chooser.showOpenDialog(null).getAbsoluteFile();
        if ( Validator.validateImport(wantToImport) ){
            pathLabel.setText(wantToImport.getAbsolutePath());
            btn_import.setDisable(!Validator.validateImportButton(lb_zipPath.getText(), lb_mainFilePath.getText()));
        } else {
            lb_error.setVisible(true);
        }
    }
    
    private void clear(Label label){
        lb_error.setVisible(false);
        label.setText("");
    }
    
    private void setUpLanguage(){
        lb_error.setText(languageBean.getValue("ERROR_FILEINVALID"));
        lb_mainFile.setText(languageBean.getValue("MAINPATH"));
        lb_zip.setText(languageBean.getValue("ZIPPATH"));
        
        btn_browse_zip.setText(languageBean.getValue("BROWSE"));
        btn_browse_mainFile.setText(languageBean.getValue("BROWSE"));
        btn_cancel_zip.setText(languageBean.getValue("CANCEL"));
        btn_cancel_mainFile.setText(languageBean.getValue("CANCEL"));
        btn_cancel.setText(languageBean.getValue("CANCEL"));
        
        btn_import.setText(languageBean.getValue("IMPORT"));
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        languageBean = Language_Singleton.getInstance();
        lb_error.setVisible(false);
        setUpLanguage();
    }
}
