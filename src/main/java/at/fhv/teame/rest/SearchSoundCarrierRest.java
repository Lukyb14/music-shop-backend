package at.fhv.teame.rest;

import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;

import javax.ejb.EJB;
import javax.ws.rs.*;
import java.util.List;

@Path("/soundCarrier/search")
public class SearchSoundCarrierRest {

    @EJB
    private SearchSoundCarrierServiceRemote searchSoundCarrierService;

    @GET
    @Path("/artist/{artist}")
    @Produces("application/json")
    @Consumes("text/plain")
    public List<SoundCarrierDTO> searchByArtist(@PathParam("artist") String artist, @QueryParam("pageNr") String pageNrStr) {
        int pageNr;
        try {
            pageNr = Integer.parseInt(pageNrStr);
        } catch (NumberFormatException e) {
            pageNr = 1;
        }

        return searchSoundCarrierService.soundCarriersByArtistName(artist, pageNr);
    }

    @GET
    @Path("/album/{album}")
    @Produces("application/json")
    @Consumes("text/plain")
    public List<SoundCarrierDTO> searchByAlbum(@PathParam("album") String album, @QueryParam("pageNr") String pageNrStr) {
        int pageNr;
        try {
            pageNr = Integer.parseInt(pageNrStr);
        } catch (NumberFormatException e) {
            pageNr = 1;
        }

        return searchSoundCarrierService.soundCarriersByAlbumName(album, pageNr);
    }

    @GET
    @Path("/song/{song}")
    @Produces("application/json")
    @Consumes("text/plain")
    public List<SoundCarrierDTO> searchBySong(@PathParam("song") String song, @QueryParam("pageNr") String pageNrStr) {
        int pageNr;
        try {
            pageNr = Integer.parseInt(pageNrStr);
        } catch (NumberFormatException e) {
            pageNr = 1;
        }
        return searchSoundCarrierService.soundCarriersBySongName(song, pageNr);
    }

}

