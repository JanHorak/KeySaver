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

/**
 *
 * @author Jan Horak
 */
public class LanguageManager {

    Properties property = new Properties();
    String filePathINI = "settings.ini";

    public String returnSetLanguage() throws FileNotFoundException, IOException {
        String result = "";
        Properties prop = new Properties();
        prop.load(new FileInputStream(new File(filePathINI)));
        result = prop.getProperty("LANG");
        System.out.println("Initial Language founded: " + result);
        return result;
    }

    public void saveLanguageInIniFile(String lang) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        prop.setProperty("LANG", lang);
        prop.store(new FileOutputStream(new File(filePathINI)), "");
        System.out.println("Saved initial Language Successfully: " + lang);
    }
}
