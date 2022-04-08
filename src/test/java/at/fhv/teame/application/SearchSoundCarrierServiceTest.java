package at.fhv.teame.application;

import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.mocks.MockSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.rmi.RemoteException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearchSoundCarrierServiceTest {

    static SearchSoundCarrierServiceImpl searchSoundCarrierService;

    @BeforeAll
    static void beforeAll() throws RemoteException {
        searchSoundCarrierService = new SearchSoundCarrierServiceImpl(new MockSoundCarrierRepository());
    }

    @Test
    void searchByAlbumName() throws RemoteException {
        String expectedAlbum = "zachary";

        List<SoundCarrierDTO> soundCarrierResults = searchSoundCarrierService.soundCarriersByAlbumName(expectedAlbum, 1);

        for (SoundCarrierDTO soundCarrier : soundCarrierResults) {
            String actualAlbum = soundCarrier.getAlbumName().toLowerCase();
            assertEquals(expectedAlbum, actualAlbum);
        }
    }
}
