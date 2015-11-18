/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.jan.keysaver2.validation.Key;
import org.apache.commons.beanutils.BeanUtils;



/**
 *
 * @author janhorak
 */
public class KeyValidator implements ConstraintValidator<Key, Object>{

    String fieldInKey = "username";
    String userNameOrEMail = "unknown";
    @Override
    public void initialize(Key key) {
  
    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext cvc) {
        boolean isValid = true;
        try {
            userNameOrEMail = BeanUtils.getProperty(t, fieldInKey);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(KeyValidator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if ( userNameOrEMail.contains("@") ){
            isValid = userNameOrEMail.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        }
        return isValid;
    }
    
}
