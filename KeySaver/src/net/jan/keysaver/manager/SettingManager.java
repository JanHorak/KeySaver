/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jan Horak
 */
public class SettingManager {

   private Properties property;
   private String filePathINI = "settings.ini";

    public String returnSetLanguage() throws FileNotFoundException, IOException {
        String result = "";
        initProperty();
        result = property.getProperty("LANG");
        System.out.println("Initial Language founded: " + result);
        return result;
    }

    public void saveLanguageInIniFile(String lang) throws FileNotFoundException, IOException {
        initProperty();
        property.setProperty("LANG", lang);
        property.store(new FileOutputStream(new File(filePathINI)), "");
        System.out.println("Saved initial Language Successfully: " + lang);
    }
    
    public void storeProperty(String key, String value) throws FileNotFoundException, IOException {
        initProperty();
        property.setProperty(key, value);
        property.store(new FileOutputStream(new File(filePathINI)), "");
    }
    
    public String returnProperty(String propertyKey){
        initProperty();
        String result = property.getProperty(propertyKey);
        return result;
    }
    
    
    private void initProperty(){
        property = new Properties();
       try {
           property.load(new FileInputStream(new File(filePathINI)));
       } catch (IOException ex) {
           Logger.getLogger(SettingManager.class.getName()).log(Level.SEVERE, null, ex);
       }
    }
    
}
