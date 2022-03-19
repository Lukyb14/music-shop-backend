package at.fhv.teame;

import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;
import at.fhv.teame.infrastructure.HibernateSoundCarrierRepository;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        SoundCarrierRepository soundCarrierRepository = new HibernateSoundCarrierRepository();
        for (SoundCarrier s : soundCarrierRepository.allSoundCarriers()) {
            System.out.println(s.getAlbum().getName());
        }
    }
}
