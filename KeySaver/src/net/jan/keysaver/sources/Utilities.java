/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import net.jan.keysaver.manager.LoggingManager;

/**
 *
 * @author janhorak
 */
public class Utilities {

    public static String getHash(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            LoggingManager.writeToErrorFile("Utilities - getHash(): ", ex);
        }
        try {
            return new String(md.digest(input.getBytes()), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            LoggingManager.writeToErrorFile("Encoding failed:  ", ex);
        }
        return null;
    }

    public static void generateZip(String pathOfZip, File... files) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(pathOfZip);
        } catch (FileNotFoundException ex) {
            LoggingManager.writeToErrorFile("Utilities: generateZip- failed:", ex);
        }
        ZipOutputStream zos = new ZipOutputStream(fos);

        for (File file : files) {

            ZipEntry ze = new ZipEntry(file.getName());
            try {
                zos.putNextEntry(ze);
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("Cant create Zip- Entry", ex);
            }
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                LoggingManager.writeToErrorFile("Utilities: generateZip- File not found: " + file, ex);
            }
            byte[] buffer = new byte[1024];
            int len;
            try {
                while ((len = fis.read(buffer)) > 0) {
                    try {
                        zos.write(buffer, 0, len);
                    } catch (IOException ex) {
                        LoggingManager.writeToErrorFile("Utilities: generateZip- ZipOutputStream cannot write", ex);
                    }
                }
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("Utilities: generateZip- Problems with Buffer- Length", ex);
            }
            try {
                zos.closeEntry();
                fis.close();
            } catch (IOException ex) {
                LoggingManager.writeToErrorFile("Utilities: generateZip- Cannot close Streams (SubStreams):", ex);
            }

        }
        try {
            zos.close();
            fos.close();
        } catch (IOException ex) {
            LoggingManager.writeToErrorFile("Utilities: generateZip- Cannot close Streams (MainStreams):", ex);
        }
        
  
    }
}
