/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.jan.keysaver.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author janhorak
 */
public class FileManager {

    public static ImageView getImageViewFromPath(String path) {
        ImageView iv = new ImageView();
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Image image = new Image(inStream);
        try {
            inStream.close();
        } catch (IOException ex) {
            Logger.getLogger(XMLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        iv.setImage(image);
        return iv;
    }

    public static Image getImageFromPath(String path) {
        FileInputStream inStream = null;
        try {
            inStream = new FileInputStream(new File(path));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XMLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        Image image = new Image(inStream);
        try {
            inStream.close();
        } catch (IOException ex) {
            Logger.getLogger(XMLManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return image;
    }
  
    public static void deleteXMLAndKey(){
        File xml = new File("AppData/structure.xml");
        File key = new File("AppData/private.key");
        if ( xml.delete() && key.delete()){
            System.out.println("XML and Key are deleted");
        } else {
            System.err.println("Something goes wrong with key and XML- Deleting");
        }
    }
}
