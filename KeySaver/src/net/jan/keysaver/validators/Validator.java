/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validators;

import net.jan.keysaver.sources.Category;
import net.jan.keysaver.sources.Key;

/**
 *
 * @author Jan Horak
 */
public class Validator {

    public boolean validateKey(Key key) {
        boolean isValid = true;

        if (key.getKeyname().isEmpty() || key.getKeyname() == null) {
            isValid = false;
        }
        if (key.getDescription().isEmpty() || key.getDescription() == null) {
            isValid = false;
        }
        if (key.getPassword().isEmpty() || key.getPassword() == null) {
            isValid = false;
        }
        if (key.getUsername().isEmpty() || key.getUsername() == null) {
            isValid = false;
        }

        if (!isValid) {
            System.err.println("Key is invalid!");
        }
        return isValid;
    }

    public boolean validateCategory(Category category) {
        boolean isValid = true;

        if (category.getName().isEmpty() || category.getName() == null) {
            isValid = false;
        }
        if (category.getIconPath().isEmpty() || category.getIconPath() == null) {
            isValid = false;
        }
        
        if (!isValid) {
            System.err.println("Category is invalid!");
        }
        return isValid;
    }
}
