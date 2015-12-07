/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.validationentities;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author janhorak
 */
public class ImportEntity {

    @NotNull
    @NotEmpty
    private String globalZip;

    public String getGlobalZip() {
        return globalZip;
    }

    public void setGlobalZip(String globalZip) {
        this.globalZip = globalZip;
    }
    
    
    
}
