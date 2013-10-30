/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.jan.keysaver.validation.Equals;
import org.apache.commons.beanutils.BeanUtils;



/**
 *
 * @author janhorak
 */
public class EqualsValidator implements ConstraintValidator<Equals, Object>{

    private String first;
    private String second;
    
    @Override
    public void initialize(Equals a) {
        first = a.first();
        second = a.second();
    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext cvc) {
        String valueOfFirst = "unknown";
        String valueOfSecond = "unknown";
        boolean isValid = true;
        try {
            valueOfFirst = BeanUtils.getProperty(t, first);
            valueOfSecond = BeanUtils.getProperty(t, second);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(EqualsValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (!valueOfFirst.equals(valueOfSecond)){
            isValid = false;
        }
        return isValid;
    }

}
