package at.fhv.teame;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        // TODO delete when schema is finished
        // entity manager is created here only to generate database schema
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("at.fhv.teame");
//        emf.createEntityManager();

        SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();
        for (SoundCarrier s : soundCarrierRepository.allSoundCarriers()) {
            System.out.println(s.getAlbum());
        }
    }
}
