package net.jan.test.XMLtests;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import net.jan.keysaver.manager.FileManager;
import net.jan.keysaver.sources.CategoryList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jan Horak
 */
public class TestXMLFunctions {
    
    public TestXMLFunctions() {
    }
    
    @Test
    public void shouldLoadCategoryListFromXML(){
        CategoryList catList = new CategoryList();
        catList = new FileManager().returnListofCategories();
        
        assertNotNull(catList);
        
        catList.printStructure();
    }
}