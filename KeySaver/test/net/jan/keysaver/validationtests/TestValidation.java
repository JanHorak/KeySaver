/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validationtests;

import net.jan.keysaver.manager.ValidationManager;
import net.jan.keysaver.sources.Key;
import net.jan.keysaver.validationentities.ImportEntity;
import net.jan.keysaver.validationentities.PropertiesEntity;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author janhorak
 */
public class TestValidation {
    
    public TestValidation() {
    }
    
    @Test
    public void testPropertiesEntityShouldBeInvalid(){
        PropertiesEntity p = new PropertiesEntity();
        p.setUserName(null);
        p.setPassword("123");
        p.setPassword_confirm("1233");

        assertTrue(!ValidationManager.isValid(p));
    }
    
    @Test
    public void testKeyShouldBeInvalid(){
        Key k = new Key();
        k.setUsername("MyUserName@");
        k.setIconPath("AppData/blabla");
        k.setDescription(null);
        k.setPassword("");
        k.setKeyname("sdasd");

        assertTrue(!ValidationManager.isValid(k));
    }
    
    @Test
    public void testKeyShouldBeValid(){
        Key k = new Key();
        k.setUsername("MyUserName@web.com");
        k.setIconPath("AppData/blabla");
        k.setDescription("tada");
        k.setPassword("123");
        k.setKeyname("sdasd");

        assertTrue(ValidationManager.isValid(k));
    }
    
    @Test
    public void testKeyShouldBeInvalidBecauseOfEmail(){
        Key k = new Key();
        k.setUsername("MyUserName@.");
        k.setIconPath("AppData/blabla");
        k.setDescription("tada");
        k.setPassword("123");
        k.setKeyname("sdasd");

        assertTrue(!ValidationManager.isValid(k));
    }
    
    @Test
    public void testKeyShouldBeValidateImport(){
        ImportEntity ie = new ImportEntity();
        
        ie.setAvatarZip("das");
        ie.setImagesZip("ff");
        ie.setIconProps("uzu");
        ie.setIniFile("asdasd");
        ie.setKey("d");
        ie.setXml("ddd");

        
        ie.setGlobalZip("");
        assertTrue(ValidationManager.isValid(ie));
        
        ie.setGlobalZip("dd");
        assertTrue(!ValidationManager.isValid(ie));
        
        ie.setIniFile("");
        ie.setGlobalZip("");
        assertTrue(!ValidationManager.isValid(ie));
    }
    
}