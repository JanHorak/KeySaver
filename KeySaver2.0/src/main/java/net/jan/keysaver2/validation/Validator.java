/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.validation;

import java.io.File;

/**
 *
 * @author Jan Horak
 */
public class Validator {

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
