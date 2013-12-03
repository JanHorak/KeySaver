/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.connection;

import java.net.HttpURLConnection;
import java.net.URL;
import net.jan.keysaver.manager.UpdateManager;
import net.jan.keysaver.sources.EnumClientVersionStatus;
import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

/**
 *
 * @author janhorak
 */
public class ConnectionTests {

    private URL URL_LASTVERSION;
    HttpURLConnection connection;
    private final String URL_TESTFILE = "https://raw.github.com/JanHorak/KeySaver2.0/master/KeySaver2.0/settings.ini";
    
    
    @Before
    public void init() {
        
    }

    @Test
    public void testForGetCurrentVersion() {
        String version = UpdateManager.getCurrentVersionFromGitHub();
        assertNotNull(version);
        System.out.println(version);
    }
    
    @Test
    public void testIsNewUpdateNeeded(){
        EnumClientVersionStatus status = UpdateManager.isActualVersionOlder(UpdateManager.getCurrentVersionFromGitHub());
        assertNotNull(status);
        System.out.println(status);
    }
    
    @Test
    public void testForCompleteUpdateLookup(){
        if (UpdateManager.checkInternetAvailibility()){
            String version = UpdateManager.getCurrentVersionFromGitHub();
            assertNotNull(version);
            EnumClientVersionStatus status = UpdateManager.isActualVersionOlder(version);
            assertNotNull(status);
        } else {
            System.err.println("Cannot establish Internetconnection");
        }
    }
    
    @Test
    public void testDownloadTheServerVersion(){
        System.out.println("want to download: " + UpdateManager.getFilePathForDownload());
        UpdateManager.downloadAndSave(UpdateManager.getFilePathForDownload(), "AppData/testDownload.zip");
    }
}