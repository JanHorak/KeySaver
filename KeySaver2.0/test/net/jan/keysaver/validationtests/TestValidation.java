/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validationtests;

import java.util.List;
import net.jan.keysaver.manager.ValidationManager;
import net.jan.keysaver.manager.XMLManager;
import net.jan.keysaver.sources.Category;
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
        p.setUserName("das");
        p.setPassword("ad");
        p.setPassword_confirm("ad");

        assertTrue(ValidationManager.isValid(p));
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
        
        ie.setGlobalZip("");
        assertTrue(!ValidationManager.isValid(ie));
        
        ie.setGlobalZip("dd");
        assertTrue(ValidationManager.isValid(ie));
        
        ie.setGlobalZip(null);
        assertTrue(!ValidationManager.isValid(ie));
    }
    
    @Test
    public void testShouldInsertTwoCatsAndFail(){
        // Test needs the default- XML
        Category cat1 = new Category();
        List<Category> list = new XMLManager().returnListofCategories().getCategoryList();
        
        cat1.setName("default");
        cat1.setKeylist(list.get(0).getKeylist());
        cat1.setIconPath("blabla");
        
        assertTrue(!ValidationManager.isValid(cat1));
        
    }
    
}