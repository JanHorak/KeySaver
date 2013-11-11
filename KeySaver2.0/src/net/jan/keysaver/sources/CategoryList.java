/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver.sources;

import java.io.Serializable;
import java.util.ArrayList;
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
            List<Key> keyList = new ArrayList<>();
            keyList = cat.getKeylist();
            if ( keyList.isEmpty() ){
                System.out.println("No Keys available");
            } else {
                for ( Key k : keyList ){
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
}
