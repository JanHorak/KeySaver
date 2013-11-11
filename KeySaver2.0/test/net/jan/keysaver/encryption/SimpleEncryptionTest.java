/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.encryption;

import java.io.File;
import net.jan.aes.decryption.Decryption;
import net.jan.aes.encryption.Encryption;
import net.jan.aes.keygenerationmanager.KeyGenerationManager;
import net.jan.keysaver.manager.FileManager;
import org.junit.Test;

/**
 *
 * @author janhorak
 */
public class SimpleEncryptionTest {
    
    @Test
    public void shouldEnAndDecryptTheStructure(){
        FileManager fm = new FileManager();
        fm.checkAvailibility();
        File structureFile = new File("AppData/structure.xml");
        structureFile = new Encryption().returnEncryptedFile(structureFile, structureFile.getAbsolutePath(), "AppData/private.key");
        structureFile = new Decryption().returnDecryptedFile(structureFile, structureFile.getAbsolutePath(), "AppData/private.key");
        
    }
     
}
