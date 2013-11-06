/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.validationentities;

import net.jan.keysaver.validation.NotEmptyOR;

/**
 *
 * @author janhorak
 */
@NotEmptyOR(fields = {"avatarZip", "imagesZip", "iconProps", "iniFile", "xml", "key"}, depedencyField = "globalZip")
public class ImportEntity {
    
    
    private String avatarZip;
    
    private String imagesZip;
    
    private String iconProps;
    
    private String iniFile;
    
    private String xml;
    
    private String key;
    
    private String globalZip;

    public String getAvatarZip() {
        return avatarZip;
    }

    public void setAvatarZip(String avatarZip) {
        this.avatarZip = avatarZip;
    }

    public String getImagesZip() {
        return imagesZip;
    }

    public void setImagesZip(String imagesZip) {
        this.imagesZip = imagesZip;
    }

    public String getIconProps() {
        return iconProps;
    }

    public void setIconProps(String iconProps) {
        this.iconProps = iconProps;
    }

    public String getIniFile() {
        return iniFile;
    }

    public void setIniFile(String iniFile) {
        this.iniFile = iniFile;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getGlobalZip() {
        return globalZip;
    }

    public void setGlobalZip(String globalZip) {
        this.globalZip = globalZip;
    }
    
    
    
}
