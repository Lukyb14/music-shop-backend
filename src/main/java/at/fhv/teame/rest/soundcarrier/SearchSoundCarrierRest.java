package at.fhv.teame.rest.soundcarrier;

import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v1/soundCarrier/search")
public class SearchSoundCarrierRest {

    @EJB
    private SearchSoundCarrierServiceRemote searchSoundCarrierService;

    @GET
    @Path("/artist/{artist}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Sound Carriers by artist name")
    @ApiResponse(
            responseCode = "200",
            description = "List of SoundCarrierDTOs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SoundCarrierDTO.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchByArtist(@PathParam("artist") String artist, @QueryParam("pageNr") String pageNrStr) {
        try {
            if (artist == null || pageNrStr == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            int pageNr = Integer.parseInt(pageNrStr);

            List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarrierService.soundCarriersByArtistName(artist, pageNr);
            return Response.ok(soundCarrierDTOS, MediaType.APPLICATION_JSON).build();

        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/album/{album}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Sound Carriers by album name")
    @ApiResponse(
            responseCode = "200",
            description = "List of SoundCarrierDTOs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SoundCarrierDTO.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchByAlbum(@PathParam("album") String album, @QueryParam("pageNr") String pageNrStr) {
        try {
            if (album == null || pageNrStr == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            int pageNr = Integer.parseInt(pageNrStr);

            List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarrierService.soundCarriersByAlbumName(album, pageNr);
            return Response.ok(soundCarrierDTOS, MediaType.APPLICATION_JSON).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/song/{song}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get Sound Carriers by song name")
    @ApiResponse(
            responseCode = "200",
            description = "List of SoundCarrierDTOs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SoundCarrierDTO.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchBySong(@PathParam("song") String song, @QueryParam("pageNr") String pageNrStr) {
        try {
            if (song == null || pageNrStr == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            int pageNr = Integer.parseInt(pageNrStr);

            List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarrierService.soundCarriersBySongName(song, pageNr);
            return Response.ok(soundCarrierDTOS, MediaType.APPLICATION_JSON).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}