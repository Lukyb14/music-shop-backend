package at.fhv.teame.application.impl;

import at.fhv.teame.domain.model.soundcarrier.Album;
import at.fhv.teame.domain.model.soundcarrier.Medium;
import at.fhv.teame.domain.model.soundcarrier.Song;
import at.fhv.teame.domain.model.soundcarrier.SoundCarrier;
import at.fhv.teame.mocks.MockSessionRepository;
import at.fhv.teame.mocks.MockSoundCarrierRepository;
import at.fhv.teame.sharedlib.dto.SongDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDetailsDTO;
import at.fhv.teame.sharedlib.rmi.exceptions.InvalidSessionException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SearchSoundCarrierServiceTest {

    static SearchSoundCarrierServiceImpl searchSoundCarrierService;

    @BeforeAll
    static void beforeAll() throws RemoteException {
        searchSoundCarrierService = new SearchSoundCarrierServiceImpl(new MockSoundCarrierRepository(), new MockSessionRepository());
    }

    @Test
    void given_2soundCarriersinrepository_when_soundCarriersByAlbumName_then_expectsoundCarrierssequals() throws RemoteException, InvalidSessionException {
        //given
        List<SoundCarrier> soundCarriers = Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );

        List<SoundCarrierDTO> soundCarrierDtosExpected = new ArrayList<>();
        for (SoundCarrier s : soundCarriers) {
            SoundCarrierDTO soundCarrierDTO = SoundCarrierDTO.builder()
                    .withSoundCarrierEntity(s.getArticleId(), s.getMedium(), s.getPrice().toString(), s.getStock(), s.getAlbumSongs().size())
                    .withAlbumEntity(s.getAlbumName(), s.getAlbumArtist(), s.getAlbumGenre())
                    .build();
            soundCarrierDtosExpected.add(soundCarrierDTO);
        }

        //when
        List<SoundCarrierDTO> soundCarriersDtoActual = searchSoundCarrierService.soundCarriersByAlbumName("Black and White", 1, UUID.randomUUID().toString());

        //then
        for (SoundCarrierDTO s : soundCarriersDtoActual) {
            assertTrue(soundCarrierDtosExpected.contains(s));
        }
    }

    @Test
    void given_2soundCarriersinrepository_when_totResultsByAlbumName_then_expecttotResultsByAlbumNameequals() throws RemoteException, InvalidSessionException {
        //given
        List<SoundCarrier> soundCarriers = Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );

        //when
        int counter = 0;
        for (SoundCarrier s: soundCarriers) {
            if (s.getAlbumName().equals("Black and White")){
                counter = counter +  1;
            }
        }
        int totResults = searchSoundCarrierService.totResultsByAlbumName("Black and White", UUID.randomUUID().toString());

        //then
        assertEquals(totResults, counter);
    }

    @Test
    void given_2soundCarriersinrepository_when_soundCarriersByArtistName_then_expectsoundsCarriersequals() throws RemoteException, InvalidSessionException {
        //given
        List<SoundCarrier> soundCarriers = Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );

        List<SoundCarrierDTO> soundCarrierDtosExpected = new ArrayList<>();
        for (SoundCarrier s : soundCarriers) {
            SoundCarrierDTO soundCarrierDTO = SoundCarrierDTO.builder()
                    .withSoundCarrierEntity(s.getArticleId(), s.getMedium(), s.getPrice().toString(), s.getStock(), s.getAlbumSongs().size())
                    .withAlbumEntity(s.getAlbumName(), s.getAlbumArtist(), s.getAlbumGenre())
                    .build();
            soundCarrierDtosExpected.add(soundCarrierDTO);
        }

        //when
        List<SoundCarrierDTO> soundCarriersDtoActual = searchSoundCarrierService.soundCarriersByArtistName("Bob", 1, UUID.randomUUID().toString());

        //then
        for (SoundCarrierDTO s : soundCarriersDtoActual) {
            assertTrue(soundCarrierDtosExpected.contains(s));
        }
    }

    @Test
    void given_2soundCarriersinrepository_when_totResultsByArtistName_then_expecttotResultsByArtistNameequals() throws RemoteException, InvalidSessionException {
        //given
        List<SoundCarrier> soundCarriers = Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );

        //when
        int counter = 0;
        for (SoundCarrier s: soundCarriers) {
            if (s.getAlbumArtist().equals("Bob")){
                counter = counter +  1;
            }
        }
        int totResults = searchSoundCarrierService.totResultsByArtistName("Bob", UUID.randomUUID().toString());

        //then
        assertEquals(totResults, counter);
    }

    @Test
    void given_2soundCarriersinrepository_when_soundCarriersBySongName_then_expectsoundsCarriersequals() throws RemoteException, InvalidSessionException {
        //given
        List<SoundCarrier> soundCarriers = Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );

        List<SoundCarrierDTO> soundCarrierDtosExpected = new ArrayList<>();
        for (SoundCarrier s : soundCarriers) {
            SoundCarrierDTO soundCarrierDTO = SoundCarrierDTO.builder()
                    .withSoundCarrierEntity(s.getArticleId(), s.getMedium(), s.getPrice().toString(), s.getStock(), s.getAlbumSongs().size())
                    .withAlbumEntity(s.getAlbumName(), s.getAlbumArtist(), s.getAlbumGenre())
                    .build();
            soundCarrierDtosExpected.add(soundCarrierDTO);
        }

        //when
        List<SoundCarrierDTO> soundCarriersDtoActual = searchSoundCarrierService.soundCarriersBySongName("Apple M1", 1, UUID.randomUUID().toString());

        //then
        for (SoundCarrierDTO s : soundCarriersDtoActual) {
            assertTrue(soundCarrierDtosExpected.contains(s));
        }
    }

    @Test
    void given_2soundCarriersinrepository_when_totResultsBySongName_then_expecttotResultsBySongNameequals() throws RemoteException, InvalidSessionException {
        //given
        List<SoundCarrier> soundCarriers = Arrays.asList(
                createSoundCarrierDummy(),
                createSoundCarrierDummy()
        );

        //when
        int counter = 2;
        int totResults = searchSoundCarrierService.totResultsBySongName("Hello World", UUID.randomUUID().toString());

        //then
        assertEquals(totResults, counter);
    }

    @Test
    void given_1soundCarriersinrepository_when_soundCarrierDetailsByArticleId_then_SoundcarriersDetailDTOequals() throws RemoteException, InvalidSessionException {
        //given
        SoundCarrier soundCarrierexpected = createSoundCarrierDummy();

        //when
        SoundCarrierDetailsDTO soundCarrierDetailsDTOactual = searchSoundCarrierService.soundCarrierDetailsByArticleId("100", UUID.randomUUID().toString());

        //then
        assertEquals(soundCarrierexpected.getArticleId(), soundCarrierDetailsDTOactual.getArticleId());
    }

    public SoundCarrier createSoundCarrierDummy() {
        return new SoundCarrier(
                String.valueOf(100),
                new Album("Black and White", "Black bars", LocalDate.of(2022,04,11), createSongListDummy(), "Pop", "Bob"),
                Medium.VINYL,
                BigDecimal.valueOf(20),
                3
        );
    }


    public List<Song> createSongListDummy() {
        return Arrays.asList(
                new Song("Hello World", LocalDate.of(2022, 04, 10), "3:10"),
                new Song("Apple M1", LocalDate.of(2022, 02, 10), "2:54"),
                new Song("Understruck", LocalDate.of(2022, 03, 10), "4:03")
        );
    }
}
