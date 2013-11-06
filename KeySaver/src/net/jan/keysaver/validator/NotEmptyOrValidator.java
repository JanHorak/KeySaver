/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import net.jan.keysaver.validation.NotEmptyOR;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author janhorak
 */
public class NotEmptyOrValidator implements ConstraintValidator<NotEmptyOR, Object> {

    private String[] valueList;
    private String orField;

    @Override
    public void initialize(NotEmptyOR a) {
        this.valueList = a.fields();
        this.orField = a.depedencyField();
    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext cvc) {
        boolean isValid = true;
        List<String> fieldList = new ArrayList<>();
        for (String val : valueList) {
            try {
                fieldList.add(BeanUtils.getProperty(t, val));
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                Logger.getLogger(NotEmptyOrValidator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            orField = BeanUtils.getProperty(t, orField);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
            Logger.getLogger(NotEmptyOrValidator.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (orField.isEmpty() && fieldList.isEmpty()) {
            isValid = false;
        }
        if (!fieldList.isEmpty() && !orField.isEmpty()) {
            isValid = false;
        }

        if (orField.isEmpty()) {
            for (String s : fieldList) {
                if (s.isEmpty()) {
                    isValid = false;
                }
            }
        }

        return isValid;
    }
}
