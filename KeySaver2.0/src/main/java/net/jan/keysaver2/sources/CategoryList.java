/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.sources;

import net.jan.keysaver2.entities.PersonalKey;
import net.jan.keysaver2.entities.Category;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Jan Horak
 */
public class CategoryList implements Serializable{
    
    private List<Category> categoryList;
    
    public List<Category> getCategoryList() {
        return categoryList;
    }
    
    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
    
    //Debug
    public void printStructure(){
        for (Category cat : this.categoryList){
            System.out.println("Name of Category: "+ cat.getName());
            System.out.println("-----------------------------------");
            List<PersonalKey> keyList = new ArrayList<>();
            keyList = cat.getKeylist();
            if ( keyList.isEmpty() ){
                System.out.println("No Keys available");
            } else {
                for ( PersonalKey k : keyList ){
                    System.out.println("Key");
                    System.out.println("------");
                    System.out.println("IconPath: " +k.getIconPath());
                    System.out.println("Name: " + k.getKeyname());
                    System.out.println("Username: " +k.getUsername());
                    System.out.println("Dec: " + k.getDescription());
                    System.out.println("pw " + k.getPassword());
                }
            }
            System.out.println("-----------------------------------");
            System.out.println("-----------------------------------\n");
        }
        
    }
    
    public void addNewCategory(Category category){
        this.categoryList.add(category);
    }
    
    public void removeCategory(Category category){
        for (int i = 0; i < categoryList.size(); i++ ){
            Category tmp = categoryList.get(i);
            if ( tmp.getName().equals(category.getName())){
                categoryList.remove(tmp);
                break;
            }
        }
    }
    
    public void replaceCategory(Category oldCategory, Category newCategory){
        Iterator<Category> iterator = this.categoryList.iterator();
        int index = 0;
        while (iterator.hasNext()){
            Category catTmp = iterator.next();
            if ( catTmp.getName().equals(oldCategory.getName()) ){
                this.categoryList.set(index, newCategory);
                break;
            }
            index++;
        }
    }
    
}
