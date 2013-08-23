package net.jan.keysaver.sources;

import java.io.Serializable;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jan Horak
 */
public class Key{
    
    private String keyname;
    
    private String username;
    
    private String description;
    
    private String password;

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
    }
    
}
