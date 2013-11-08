/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.beans;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jan.keysaver.manager.SettingManager;

/**
 *
 * @author Jan Horak
 */
public class Settings_Singelton {

    private static Settings_Singelton instance = null;
    private static Properties propertiesOfInstance = new Properties();
    
    private static final String PROGRAMVERSION = "V.0.9.9.1 Beta";
    
    public String getVersion(){
        return PROGRAMVERSION;
    }

    public static Settings_Singelton getInstance() {
        if (instance == null) {
            instance = new Settings_Singelton();
        }
        return instance;
    }
    
    private Settings_Singelton(){
        SettingManager sm = new SettingManager("settings.ini");
        try {
            propertiesOfInstance = sm.initAndReturnProperties();
            propertiesOfInstance = setCharsetInNewPropertiesFile();
        } catch (IOException ex) {
            Logger.getLogger(Settings_Singelton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getValue(String key){
        return propertiesOfInstance.getProperty(key);
    }
    
    public void storeInBean(String key, String Value){
        propertiesOfInstance.setProperty(key, Value);
    }
    
    public void saveBean(){
        try {
            propertiesOfInstance.store(new FileOutputStream("settings.ini"), new String("Bean updated: " + new Date()));
            System.out.println("Values from Settings-Bean are Saved in settings.ini");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Settings_Singelton.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Settings_Singelton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Properties setCharsetInNewPropertiesFile(){
        Properties props = new Properties();
        for ( Object obj : propertiesOfInstance.stringPropertyNames() ){
            props.setProperty(obj.toString(), new String(propertiesOfInstance.getProperty(obj.toString()).getBytes(), Charset.forName("UTF-8")));
        }
        return props;
    }
    
}
