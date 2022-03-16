package at.fhv.teame;

import javax.persistence.*;

public class Main {

    @PersistenceContext
    static EntityManager em;

    @PersistenceUnit
    static EntityManagerFactory emf;

    public static void main(String[] args) {
        Person halim = new Person("Halim");

        emf = Persistence.createEntityManagerFactory("at.fhv.teame");

        em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(halim);
        em.getTransaction().commit();
    }
}
