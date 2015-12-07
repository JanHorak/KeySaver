/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.jan.keysaver2.manager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.jan.keysaver2.entities.AppUser;

/**
 *
 * @author janhorak
 */
public class UserManager {

    private final EntityManager em;
    private final EntityManagerFactory emf;

    public UserManager() {
        emf = Persistence.createEntityManagerFactory("KS2PU");
        em = emf.createEntityManager();
    }

    public void registerUser(AppUser user) {
        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }

    public AppUser getUserByName(String name) {
        em.getTransaction().begin();
        AppUser u = (AppUser) em.createNamedQuery(AppUser.FIND_BY_NAME).setParameter("name", name).getSingleResult();
        em.close();
        emf.close();
        return u == null ? null : u;
    }
    
    public boolean doesUserExist(String user){
        em.getTransaction().begin();
        AppUser u = (AppUser) em.createNamedQuery(AppUser.FIND_BY_NAME).getSingleResult();
        em.close();
        emf.close();
        return (u == null);
    }

}
