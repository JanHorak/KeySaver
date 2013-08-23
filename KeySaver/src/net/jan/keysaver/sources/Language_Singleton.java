/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jan.keysaver.manager.SettingManager;

/**
 *
 * @author Jan Horak
 */
public class Language_Singleton {
    
    private static Language_Singleton instance = null;
    private static Properties instanceProperties = null;
    
    private String lang = "unknown";
    
    public static Language_Singleton getInstance(){
        if ( instance == null ){
            instance = new Language_Singleton();
        }
        return instance;
    }
    
    private Language_Singleton(){
        SettingManager sm = new SettingManager("settings.ini");
        try {
            lang = sm.returnProperty("LANG");
            System.out.println("Language- Singelton loaded...");
        } catch (IOException ex) {
            Logger.getLogger(Language_Singleton.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            instanceProperties = new SettingManager("AppData\\Lang_"+lang+".properties").initAndReturnProperties();
            System.out.println("Language-Values loaded in Singelton");
        } catch (IOException ex) {
            Logger.getLogger(Language_Singleton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setupNewLanguage(){
        instance = new Language_Singleton();
    }
    
    public String returnValue(String value){
        return instanceProperties.getProperty(value);
    }
    
}
