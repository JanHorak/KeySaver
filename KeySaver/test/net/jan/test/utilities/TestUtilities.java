/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.test.utilities;

import net.jan.keysaver.sources.Utilities;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author janhorak
 */
public class TestUtilities {
    
    public TestUtilities() {
    }
    
    @Test
    public void testHash(){
        String test = "test";
        String hashedTest = new Utilities().getHash(test);
        String test2 = "test2";
        assertTrue(new Utilities().getHash(test).equals(hashedTest));
        assertTrue(!test2.equals(hashedTest));
    }
    
}