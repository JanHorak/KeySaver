/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Jan Horak
 */
public class Category implements Serializable{
 
    private List<Key> keylist;
    
    private String name;
    
    private String iconPath;

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    

    public List<Key> getKeylist() {
        return keylist;
    }

    public void setKeylist(List<Key> keylist) {
        this.keylist = keylist;
    }
    
    
    
    
}
