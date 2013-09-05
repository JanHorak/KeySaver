package net.jan.test.XMLtests;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.sources.Category;
import net.jan.keysaver.sources.CategoryList;
import net.jan.keysaver.sources.Key;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jan Horak
 */
public class TestXMLFunctions {
    
    public TestXMLFunctions() {
    }
    /**
     * This Tests need the Default-Structure of the XML-File
     * Warning: Run of the Test will reset the Data of the File!
     */
    
    @Test
    public void shouldCheckAvailibilityOfStructure(){
        FileManager fm = new FileManager();
        fm.checkAvailibility();
        
        //Points the File for Deleting
        File f = new File("AppData\\structure.xml");
        
        //Delete the File
        f.delete();
        
        //Detection and restore
        fm.checkAvailibility();
    }
    
    
    @Test
    public void shouldLoadCategoryListFromXML(){
        CategoryList catList = new CategoryList();
        catList = new FileManager().returnListofCategories();
        
        assertNotNull(catList);
        catList.printStructure();
    
    }
    
    @Test
    public void shouldReturnSingleCategoryListFromXML(){
        Category cat = new Category();
        cat = new FileManager().returnSingleCategory("default");
        System.out.println(cat.getName());
        System.out.println(cat.getIconPath());
        assertNotNull(cat);
    }
    
    @Test
    public void shouldReturnKeyFromXML(){
        Key k = new Key();
        k = new FileManager().returnKey("defaultKey");
        assertNotNull(k);
    }
    
    
}