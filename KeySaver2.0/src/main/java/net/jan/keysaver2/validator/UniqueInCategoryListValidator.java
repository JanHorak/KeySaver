/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.validator;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
//import net.jan.keysaver2.manager.XMLManager;
import net.jan.keysaver2.entities.Category;
import net.jan.keysaver2.validation.UniqueInCategoryList;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author janhorak
 */
public class UniqueInCategoryListValidator implements ConstraintValidator<UniqueInCategoryList, Object> {

    String uniqueField = "";
    String iconField = "";

    @Override
    public void initialize(UniqueInCategoryList a) {
        uniqueField = a.uniqueField();
        iconField = a.iconField();
    }

    @Override
    public boolean isValid(Object t, ConstraintValidatorContext cvc) {
        boolean isValid = true;
//        String name = "";
//        String icon = "";
//        try {
//            name = BeanUtils.getProperty(t, uniqueField);
//            icon = BeanUtils.getProperty(t, iconField);
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
//            Logger.getLogger(UniqueInCategoryListValidator.class.getName()).log(Level.SEVERE, null, ex);
//        }
//
//        List<Category> catList = new XMLManager().returnListofCategories().getCategoryList();
//        for (Category cat : catList) {
//            if (cat.getName().equals(name)) {
//                if (cat.getIconPath().equals(icon)) {
//                    isValid = false;
//                    break;
//                }
//            }
//        }

        return isValid;
    }

}
