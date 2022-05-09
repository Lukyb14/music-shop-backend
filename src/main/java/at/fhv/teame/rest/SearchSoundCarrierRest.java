package at.fhv.teame.rest;

import at.fhv.teame.application.impl.AuthenticationServiceImpl;
import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;

import javax.ws.rs.*;
import java.util.List;

@Path("/soundCarrier/search")
public class SearchSoundCarrierRest {


    private SearchSoundCarrierServiceRemote searchSoundCarrierService;
    private AuthenticationServiceImpl authenticationImpl;

    public SearchSoundCarrierRest() {
        authenticationImpl = new AuthenticationServiceImpl();
        searchSoundCarrierService = new SearchSoundCarrierServiceImpl();
    }

    @GET
    @Path("/artist/{artist}")
    @Produces("application/json")
    @Consumes("text/plain")
    public List<SoundCarrierDTO> searchByArtist(@PathParam("artist") String artist, @QueryParam("pageNr") String pageNrStr) throws InvalidSessionException {
        int pageNr;
        try {
            pageNr = Integer.parseInt(pageNrStr);
        } catch (NumberFormatException e) {
            pageNr = 1;
        }

        return searchSoundCarrierService.soundCarriersByArtistName(artist, pageNr, authenticationImpl.backdoorLogin("yce5586").getSessionId());
    }

    @GET
    @Path("/album/{album}")
    @Produces("application/json")
    @Consumes("text/plain")
    public List<SoundCarrierDTO> searchByAlbum(@PathParam("album") String album, @QueryParam("pageNr") String pageNrStr) throws InvalidSessionException {

        int pageNr;
        try {
            pageNr = Integer.parseInt(pageNrStr);
        } catch (NumberFormatException e) {
            pageNr = 1;
        }

        return searchSoundCarrierService.soundCarriersByAlbumName(album, pageNr, authenticationImpl.backdoorLogin("yce5586").getSessionId());
    }

    @GET
    @Path("/song/{song}")
    @Produces("application/json")
    @Consumes("text/plain")
    public List<SoundCarrierDTO> searchBySong(@PathParam("song") String song, @QueryParam("pageNr") String pageNrStr) throws InvalidSessionException {
        int pageNr;
        try {
            pageNr = Integer.parseInt(pageNrStr);
        } catch (NumberFormatException e) {
            pageNr = 1;
        }
        return searchSoundCarrierService.soundCarriersBySongName(song, pageNr, authenticationImpl.backdoorLogin("yce5586").getSessionId());
    }

}

