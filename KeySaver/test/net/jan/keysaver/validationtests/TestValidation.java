/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validationtests;

import net.jan.keysaver.manager.ValidationManager;
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
        for ( String errorFields : ValidationManager.returnInvalidFields(p) ){
            System.err.println(errorFields);
        }
    }
}