/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.manager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jan Horak
 */
public class LoggingManager {

    static String pathErrorFile = "AppData\\Error.log";
    static String pathLoggingFile = "AppData\\Logging.log";

    public static void writeToErrorFile(String errorText, Exception inputEx) {
        Date date = new Date();
        FileWriter fw = null;
        try {
            fw = new FileWriter(pathErrorFile, true);
        } catch (IOException ex) {
            Logger.getLogger(LoggingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            fw.append("---------------ERROR---------------\n");
            fw.append("Time: " + date + "\n");
            fw.append("\n");
            fw.append("Hint from Developer: \n");
            if (errorText != null) {
                fw.append(errorText + "\n");
            } else {
                fw.append("No Hint given \n");
            }
            fw.append("\n");
            fw.append("Exception: \n");
            if (inputEx != null) {
                fw.append(inputEx.getStackTrace().toString() + "\n");
            } else {
                fw.append("No Exception given \n");
            }
            fw.append("------------------------------------\n");
            fw.append("\n");
            fw.close();
            System.err.println("New Entry in Errorlog!");
        } catch (IOException ex) {
            Logger.getLogger(LoggingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    public static void writeToLogFile(String LogText) {
        Date date = new Date();
        FileWriter fw = null;
        try {
            fw = new FileWriter(pathLoggingFile, true);
        } catch (IOException ex) {
            Logger.getLogger(LoggingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedWriter bw = new BufferedWriter(fw);
        try {
            fw.append(date + ": " + LogText+"\n");
            
        } catch (IOException ex) {
            Logger.getLogger(LoggingManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
