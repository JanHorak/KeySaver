/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.jan.keysaver.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.jan.keysaver.manager.XMLManager;
import net.jan.keysaver.sources.Category;
import net.jan.keysaver.validation.UniqueInCategoryList;
import net.jan.keysaver.validation.UniqueInKeyList;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author janhorak
 */
public class UniqueInKeyListValidator implements ConstraintValidator<UniqueInKeyList, Object>{

    @Override
    public void initialize(UniqueInKeyList a) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext cvc) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    
    
}
