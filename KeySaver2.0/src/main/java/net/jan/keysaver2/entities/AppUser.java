/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Jan
 */
@Entity
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    @Column(columnDefinition = "blob")
    private byte[] pk;

    @NotNull
    @Column(columnDefinition = "blob")
    private byte[] avatar;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private List<Category> catList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public List<Category> getCatList() {
        return catList;
    }

    public void setCatList(List<Category> catList) {
        this.catList = catList;
    }

    public byte[] getPk() {
        return pk;
    }

    public void setPk(byte[] pk) {
        this.pk = pk;
    }

}
