/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.manager;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
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
import net.jan.keysaver.beans.Settings_Singleton;
import net.jan.keysaver.sources.EnumClientVersionStatus;
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
            urlList.add(new URL("https://www.github.com"));
        } catch (MalformedURLException ex) {
            Logger.getLogger(UpdateManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        for (URL url : urlList) {
            try {
                System.out.println("try to connect");

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

    public static String getCurrentVersionFromGitHub() {
        String lastVersionOnReadMe = "https://raw.github.com/JanHorak/KeySaver2.0/master/CurrentVersion/versioninfo";
        String pathForTempFile = "AppData/versioninfo.txt";
        String result = "unknownVersion";
        downloadAndSave(lastVersionOnReadMe, pathForTempFile);
        result = Utilities.getStringFromFile(pathForTempFile).trim();
        new File(pathForTempFile).delete();
        return result;
    }

    public static void downloadAndSave(String url, String pathForStoring) {
        final int SIZE = 1024 * 8;
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
        BufferedOutputStream bout = new BufferedOutputStream(fos, SIZE);
        byte[] buffer = new byte[SIZE];
        int i = 0;
        int read = 0;
        try {
            while ((i = in.read(buffer)) > 0) {
                bout.write(buffer, 0, i);
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

    public static EnumClientVersionStatus isActualVersionOlder(String currentVersion) {
        System.out.println("From server: " + currentVersion);
        //Current Version is the Version from the Server
        boolean result = false;
        String actualVersion = Settings_Singleton.getInstance().getVersionNumber();
        if (currentVersion.equals(actualVersion)) {
            return EnumClientVersionStatus.actual;
        }
        if (!currentVersion.equals(actualVersion)) {
            return EnumClientVersionStatus.old;
        }
        return null;
    }

    public static String getFilePathForDownload() {
        String präfix = "https://github.com/JanHorak/KeySaver2.0/blob/master/CurrentVersion/KeySaver2.0_";
        String version = getCurrentVersionFromGitHub();
        String suffix = "%20Beta.zip?raw=true";
        return präfix + version + suffix;
    }
}
