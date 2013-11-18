/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jan.keysaver.sources.Utilities;

/**
 *
 * @author janhorak
 */
public class UpdateManager {

    

    public static boolean checkInternetAvailibility() {
        boolean isAvailable = false;
        List<URL> urlList = new ArrayList<URL>();
        try {
            urlList.add(new URL("http://www.google.com"));
            urlList.add(new URL("http://www.gmx.de"));
            urlList.add(new URL("http://www.amazon.com"));
            urlList.add(new URL("http://www.microsoft.com"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (URL url : urlList) {
            try {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                if (con.getResponseCode() == 200) {
                    System.out.println("Connection established: " + con.getURL());
                    isAvailable = true;
                }
            } catch (Exception x) {
                System.out.println("No Connection to " + x);
            }
        }
        return isAvailable;
    }

    public static String getCurrentVersionFromGitHub(String p) {
        //"https://raw.github.com/JanHorak/KeySaver2.0/blob/master/CurrentVersion/versioninfo.txt"
        String pathForTempFile = "AppData/versioninfo.txt";
        String LASTVERSIONREADME = p;
        String result = "unknownVersion";
        downloadAndSave(LASTVERSIONREADME, pathForTempFile);
        result = Utilities.getStringFromFile(pathForTempFile);
        return result;
    }

    private static void downloadAndSave(String url, String pathForStoring) {
        URL goalURL = null;
        HttpURLConnection connection = null;
        try {
            goalURL = new URL(url);
            connection = (HttpURLConnection) goalURL.openConnection();
        } catch (IOException ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        BufferedInputStream in = null;
        try {
            in = new java.io.BufferedInputStream(connection.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        FileOutputStream fos = null;
        try {
            fos = new java.io.FileOutputStream(pathForStoring);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte[] data = new byte[1024];
        int i = 0;
        int read = 0;
        try {
            while ((i = in.read(data, 0, 1024)) >= 0) {
                bout.write(data, 0, 1024);
            }
        } catch (IOException ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            bout.close();
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
