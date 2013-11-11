package net.jan.keysaver.sources;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jan Horak
 */
@net.jan.keysaver.validation.Key
public class Key{
    
    @NotNull
    @NotEmpty
    private String keyname;
    
    @NotNull
    @NotEmpty
    private String username;
    
    private String description;
    
    @NotNull
    @NotEmpty
    private String password;
    
    @NotNull
    @NotEmpty
    private String iconPath;

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
    
    public String getKeyname() {
        return keyname;
    }

    public void setKeyname(String keyname) {
        this.keyname = keyname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
    public void overwriteKey(Key newKey){
        this.keyname = newKey.getKeyname();
        this.description = newKey.getDescription();
        this.password = newKey.getPassword();
        this.username = newKey.getUsername();
        this.iconPath = newKey.getIconPath();
    }
    
}
