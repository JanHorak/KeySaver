/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.connection;

import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Test;
import org.junit.Before;

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
    public void saveLastVersion() {
//        UpdateManager.getCurrentVersionFromGitHub(URL_TESTFILE);
        
    }
}