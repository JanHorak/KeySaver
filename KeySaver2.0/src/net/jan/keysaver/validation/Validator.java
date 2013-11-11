/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validation;

import java.io.File;
import net.jan.keysaver.sources.Category;
import net.jan.keysaver.sources.Key;

/**
 *
 * @author Jan Horak
 */
public class Validator {

    public boolean validateKey(Key key) {
        boolean isValid = true;

        if (key.getKeyname().isEmpty() || key.getKeyname() == null) {
            isValid = false;
        }
        if (key.getDescription().isEmpty() || key.getDescription() == null) {
            isValid = false;
        }
        if (key.getPassword().isEmpty() || key.getPassword() == null) {
            isValid = false;
        }
        if (key.getUsername().isEmpty() || key.getUsername() == null) {
            isValid = false;
        }

        if (!isValid) {
            System.err.println("Key is invalid!");
        }
        return isValid;
    }

    public boolean validateCategory(Category category) {
        boolean isValid = true;

        if (category.getName().isEmpty() || category.getName() == null) {
            isValid = false;
        }
        if (category.getIconPath().isEmpty() || category.getIconPath() == null) {
            isValid = false;
        }

        if (!isValid) {
            System.err.println("Category is invalid!");
        }
        return isValid;
    }

    public static boolean validateImport(File importFile) {
        boolean isValid = true;

        
        if (importFile.getAbsolutePath().endsWith(".xml")) {
            if  (!importFile.getAbsolutePath().endsWith("structure.xml")){
                isValid = false;
            }
            if (importFile == null || importFile.length() == 0) {
                isValid = false;
            }
        }
        
        if (importFile.getAbsolutePath().endsWith(".zip")) {
            if  (!importFile.getAbsolutePath().endsWith("KeySaver_export.zip")){
                isValid = false;
            }
            if (importFile == null || importFile.length() == 0) {
                isValid = false;
            }
        }
        return isValid;
    }
    
    public static boolean validateImportButton(String pathXML, String pathZip){
        boolean isValid = true;
        
        if ( pathXML.equals(pathZip) ){
            isValid = false;
        }
        
        if ( pathXML.isEmpty() && pathZip.isEmpty() ){
            isValid = false;
        }
        
        if ( !pathXML.isEmpty() && !pathZip.isEmpty() ){
            isValid = false;
        }
        
        
        return isValid;
    }
    
}
