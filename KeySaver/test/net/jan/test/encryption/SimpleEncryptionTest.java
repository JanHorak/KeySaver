/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.test.encryption;

import java.io.File;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
import net.jan.aes.keygenerationmanager.KeyGenerationManager;
import org.junit.Test;

/**
 *
 * @author janhorak
 */
public class SimpleEncryptionTest {
    
    @Test
    public void shouldEnAndDecryptTheStructure(){
        File structureFile = new File("AppData/structure.xml");
        
        KeyGenerationManager keyMananger = new KeyGenerationManager();
        keyMananger.generateAndStoreKey("AppData/private.key");
        
        structureFile = new Encryption().returnEncryptedFile(structureFile, structureFile.getAbsolutePath(), "AppData/private.key");

        
    }
    
    @Test
    public void shouldDecryptTheini(){
        File structureFile = new File("settings.ini");
        
        structureFile = new Encryption().returnEncryptedFile(structureFile, structureFile.getAbsolutePath(), "AppData/private.key");

        
    }
    
}
