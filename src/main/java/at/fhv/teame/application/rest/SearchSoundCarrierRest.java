package at.fhv.teame.application.rest;

import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.ejb.SearchSoundCarrierServiceRemote;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/soundCarrier/search")
public class SearchSoundCarrierRest {

    @EJB
    private SearchSoundCarrierServiceRemote searchSoundCarrierService;

    @GET
    @Path("/artist/{artist}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(summary = "Get Sound Carriers by artist name")
    @ApiResponse(
            responseCode = "200",
            description = "List of SoundCarrierDTOs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SoundCarrierDTO.class)
            )
    )
    @ApiResponse(responseCode = "401", description = "Token verification failed")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchByArtist(@PathParam("artist") String artist, @QueryParam("pageNr") String pageNrStr, @CookieParam("token") String token) {
        try {
            if (artist == null || pageNrStr == null || token == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            JaxRsApplication.verifyToken(token);
            int pageNr = Integer.parseInt(pageNrStr);

            List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarrierService.soundCarriersByArtistName(artist, pageNr);
            return Response.ok(soundCarrierDTOS, MediaType.APPLICATION_JSON).build();

        } catch (JWTVerificationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/album/{album}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(summary = "Get Sound Carriers by album name")
    @ApiResponse(
            responseCode = "200",
            description = "List of SoundCarrierDTOs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SoundCarrierDTO.class)
            )
    )
    @ApiResponse(responseCode = "401", description = "Token verification failed")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchByAlbum(@PathParam("album") String album, @QueryParam("pageNr") String pageNrStr, @CookieParam("token") String token) {
        try {
            if (album == null || pageNrStr == null || token == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            JaxRsApplication.verifyToken(token);
            int pageNr = Integer.parseInt(pageNrStr);

            List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarrierService.soundCarriersByAlbumName(album, pageNr);
            return Response.ok(soundCarrierDTOS, MediaType.APPLICATION_JSON).build();
        } catch (JWTVerificationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/song/{song}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(summary = "Get Sound Carriers by song name")
    @ApiResponse(
            responseCode = "200",
            description = "List of SoundCarrierDTOs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SoundCarrierDTO.class)
            )
    )
    @ApiResponse(responseCode = "401", description = "Token verification failed")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchBySong(@PathParam("song") String song, @QueryParam("pageNr") String pageNrStr, @CookieParam("token") String token) {
        try {
            if (song == null || pageNrStr == null || token == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            JaxRsApplication.verifyToken(token);
            int pageNr = Integer.parseInt(pageNrStr);

            List<SoundCarrierDTO> soundCarrierDTOS = searchSoundCarrierService.soundCarriersBySongName(song, pageNr);
            return Response.ok(soundCarrierDTOS, MediaType.APPLICATION_JSON).build();
        } catch (JWTVerificationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}

