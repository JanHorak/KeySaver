/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.test.keyGeneration;

import java.io.File;
import net.jan.aes.keygenerationmanager.KeyGenerationManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author janhorak
 */
public class SimpleKeyGeneration {
    
    public SimpleKeyGeneration() {
    }
    
    
    @Test
    public void generateAPrivateKey(){
        KeyGenerationManager keyMan = new KeyGenerationManager();
        keyMan.generateAndStoreKey("AppData/private.key");
    }
    
}