package datageneration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.jan.keysaver2.entities.AppUser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Jan
 */
public class Datageneration {

    @Test
    public void createUser() {
        EntityManager em;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");
        em = emf.createEntityManager();

        em.getTransaction().begin();

        AppUser u = new AppUser();
        u.setAvatar(new byte[1]);
        u.setPk(new byte[1]);
        u.setCatList(new ArrayList<>());
        u.setName("Testuser");
        u.setPassword("test");

        em.persist(u);

        em.getTransaction().commit();
        em.close();
    }

}
