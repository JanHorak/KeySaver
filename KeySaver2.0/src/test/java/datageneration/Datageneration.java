package datageneration;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import net.jan.keysaver2.entities.AppUser;
import net.jan.keysaver2.security.Encryption;
import net.jan.keysaver2.security.KeyGenerationHelper;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
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
        byte[] enc = null;
        byte[] pKeyBytes = null;
        
        try {
            new KeyGenerationHelper().generateAndStoreKeys();
            enc = Encryption.encrypt("public.key", "test");
            pKeyBytes = IOUtils.toByteArray(new FileInputStream("public.key"));
        } catch (NoSuchAlgorithmException | IOException | InvalidKeySpecException | InvalidKeyException ex) {
            Logger.getLogger(Datageneration.class.getName()).log(Level.SEVERE, null, ex);
        }

        em.getTransaction().begin();

        AppUser u = new AppUser();
        u.setAvatar(new byte[1]);
        u.setPk(pKeyBytes);
        u.setCatList(new ArrayList<>());
        u.setName("Testuser");
        u.setPassword(enc);

        em.persist(u);

        em.getTransaction().commit();
        em.close();
    }

}
