package net.jan.keysaver2.entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author Jan Horak
 */
@Entity
@net.jan.keysaver2.validation.Key
public class PersonalKey implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    @ManyToOne(cascade = CascadeType.ALL, targetEntity = Category.class)
    private Category ownedCategory;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Category getOwnedCategory() {
        return ownedCategory;
    }

    public void setOwnedCategory(Category ownedCategory) {
        this.ownedCategory = ownedCategory;
    }

}
