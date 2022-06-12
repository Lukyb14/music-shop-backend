package at.fhv.teame.rest.soundcarrier;

import at.fhv.teame.rest.exceptions.MissingParameterException;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v1/soundCarriers")
public class SearchSoundCarrierRest {

    @EJB
    private SearchSoundCarrierServiceRemote searchSoundCarrierService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Sound Carriers by either artist, album or song title")
    @ApiResponse(
            responseCode = "200",
            description = "List of SoundCarrierDTOs",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    array = @ArraySchema(schema = @Schema(implementation = SoundCarrierDTO.class))
            )
    )
    @ApiResponse(responseCode = "400", description = "No search parameters specified or pageNr/pageSize is not a number")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response soundCarriers(@QueryParam("artist") String artist,
                                  @QueryParam("album") String album,
                                  @QueryParam("songtitle") String songTitle,
                                  @QueryParam("pageNr") String pageNrStr) {
        try {
            List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarriers(artist, album, songTitle, pageNrStr);
            return Response.ok(soundCarrierDTOS, MediaType.APPLICATION_JSON).build();
        } catch (NumberFormatException | MissingParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<SoundCarrierDTO> searchSoundCarriers(String artist, String album, String songTitle, String pageNrStr) throws MissingParameterException {
        if (pageNrStr != null) {
            int pageNr = Integer.parseInt(pageNrStr);
            if (artist != null) {
                return searchSoundCarrierService.soundCarriersByArtistName(artist, pageNr);
            } else if (songTitle != null) {
                return searchSoundCarrierService.soundCarriersBySongName(songTitle, pageNr);
            } else if (album != null) {
                return searchSoundCarrierService.soundCarriersByAlbumName(album, pageNr);
            } else {
                throw new MissingParameterException();
            }
        } else {
            throw new MissingParameterException();
        }
    }
}