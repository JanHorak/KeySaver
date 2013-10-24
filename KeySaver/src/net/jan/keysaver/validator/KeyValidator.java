/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.jan.keysaver.validation.Key;



/**
 *
 * @author janhorak
 */
public class KeyValidator implements ConstraintValidator<Key, Object>{

    @Override
    public void initialize(Key key) {

    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext cvc) {
        boolean isValid = true;
        
        return isValid;
    }
    
}
