/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.jan.keysaver.manager;

import net.jan.keysaver.beans.Settings_Singleton;
import net.jan.keysaver.sources.User;
import net.jan.keysaver.sources.Utilities;

/**
 *
 * @author janhorak
 */
public class UserManager {
    
    private Settings_Singleton settingsBean;
    
    public UserManager(){
        settingsBean = Settings_Singleton.getInstance();
    }
    
    public User getUser(){
        User user = new User();
        user.setName(settingsBean.getValue("USERNAME"));
        user.setIconPath(settingsBean.getValue("AVATAR"));
        user.setMPW(settingsBean.getValue("MPW"));
        
        return user;
    }
    
    
    public void registerUser(User user){
        settingsBean.storeInBean("USERNAME", user.getName());
        settingsBean.storeInBean("MPW", Utilities.getHash(user.getMPW()));
        settingsBean.storeInBean("AVATAR", user.getIconPath());
        FileManager.deleteXMLAndKey();
    }
    
}
