/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validationentities;

import javax.validation.constraints.NotNull;
import net.jan.keysaver.validation.Equals;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author janhorak
 */
@Equals(first = "password", second = "password_confirm")
public class PropertiesEntity {
    
    @NotNull
    @NotEmpty
    private String userName;
    
    private String password;
    
    private String password_confirm;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirm() {
        return password_confirm;
    }

    public void setPassword_confirm(String password_confirm) {
        this.password_confirm = password_confirm;
    }
    
    
}
