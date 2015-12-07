/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 *
 * @author janhorak
 */
public class ValidationManager {
    
    /**
     * This static method validates an Object
     * @param ob - the Object we want to validate
     */
    public static void validate(Object ob) {
        Set<ConstraintViolation<Object>> constraintViolations = returnConstraintViolantionSet(ob);
        for (ConstraintViolation<Object> violation : constraintViolations) {
            System.err.println(violation.getPropertyPath() + " " + violation.getMessage());
        }
    }
    
    /**
     * This static method validates an Object
     * @param ob - the Object we want to validate
     */
    public static List<String> returnInvalidFields(Object ob) {
        List<String> errorFields = new ArrayList<>();
        Set<ConstraintViolation<Object>> constraintViolations = returnConstraintViolantionSet(ob);
        for (ConstraintViolation<Object> violation : constraintViolations) {
            if ( violation.getPropertyPath().toString().isEmpty() ){
                errorFields.add("Error in global Constraint!");
            } else {
                errorFields.add(violation.getPropertyPath().toString());
            }
        }
        return errorFields;
    }
    
    /**
     * This method returns the Set of ConstraintViolation for testing and the validate-method
     * @param ob - the Object we want to validate
     * @return - the Set of Constraints
     */
    public static Set<ConstraintViolation<Object>> returnConstraintViolantionSet(Object ob) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(ob);
        return constraintViolations;
    }
    
    /**
     * Returns the validation of the passed Object
     * @param ob - the Object we want to validate
     * @return - the boolean, if the Object is valid
     */
    public static boolean isValid(Object ob){
        return returnConstraintViolantionSet(ob).isEmpty();
    }
    
}
