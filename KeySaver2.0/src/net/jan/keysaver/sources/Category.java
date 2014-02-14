/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import javax.validation.constraints.NotNull;
import net.jan.keysaver.validation.UniqueInCategoryList;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Jan Horak
 */
@UniqueInCategoryList(uniqueField = "name", iconField = "iconPath")
public class Category implements Serializable, Comparable<Category>{
 
    @NotNull
    @NotEmpty
    private List<Key> keylist;
    
    @NotNull
    @NotEmpty
    private String name;
    
    @NotNull
    @NotEmpty
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
    
    public void replaceKey(Key oldKey, Key newKey){
        Iterator<Key> iterator = this.keylist.iterator();
        int index = 0;
        while (iterator.hasNext()){
            Key keyTmp = iterator.next();
            if ( keyTmp.getKeyname().equals(oldKey.getKeyname()) ){
                this.keylist.set(index, newKey);
                break;
            }
            index++;
        }
    }
    
    public void addKey(Key key){
        this.keylist.add(key);
    }
    
    public void deleteKey(Key key){
        for ( int i = 0; i < this.keylist.size(); i++ ){
            Key k = this.keylist.get(i);
            if (k.getKeyname().equals(key.getKeyname())){
                this.keylist.remove(k);
                break;
            }
        }
    }
    
    public boolean hasMoreThanOneKey(){
        if (this.keylist.size() > 1){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int compareTo(Category o) {
        return this.name.compareTo(o.getName());
    }


      
}
