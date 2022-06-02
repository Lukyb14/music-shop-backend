package at.fhv.teame.rest.digitalsong;

import at.fhv.teame.rest.schema.DigitalSongListSchema;
import at.fhv.teame.sharedlib.ejb.SearchDigitalSongServiceRemote;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/song/search")
public class SearchDigitalSongRest {

    @EJB
    private SearchDigitalSongServiceRemote searchDigitalSongService;

    @GET
    @Path("/artist/{artist}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get songs by artist name")
    @ApiResponse(
            responseCode = "200",
            description = "List of songs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DigitalSongListSchema.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchByArtist(@PathParam("artist") String artist, @QueryParam("pageNr") String pageNrStr, @QueryParam("pageSize") String pageSizeStr) {
        try {
            if (artist == null || pageNrStr == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            int pageNr = Integer.parseInt(pageNrStr);
            int pageSize = Integer.parseInt(pageSizeStr);

            DigitalSongListSchema digitalSongListDTOS = new DigitalSongListSchema(searchDigitalSongService.digitalSongByArtist(artist, pageNr, pageSize));
            return Response.ok(digitalSongListDTOS, MediaType.APPLICATION_JSON).build();

        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/genre/{genre}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get songs by genre")
    @ApiResponse(
            responseCode = "200",
            description = "List of songs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DigitalSongListSchema.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchByGenre(@PathParam("genre") String genre, @QueryParam("pageNr") String pageNrStr, @QueryParam("pageSize") String pageSizeStr) {
        try {
            if (genre == null || pageNrStr == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            int pageNr = Integer.parseInt(pageNrStr);
            int pageSize = Integer.parseInt(pageSizeStr);

            DigitalSongListSchema digitalSongListDTOS = new DigitalSongListSchema(searchDigitalSongService.digitalSongByGenre(genre, pageNr, pageSize));
            return Response.ok(digitalSongListDTOS, MediaType.APPLICATION_JSON).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/song/{song}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get songs by song title")
    @ApiResponse(
            responseCode = "200",
            description = "List of songs",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DigitalSongListSchema.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response searchBySong(@PathParam("song") String song, @QueryParam("pageNr") String pageNrStr, @QueryParam("pageSize") String pageSizeStr) {
        try {
            if (song == null || pageNrStr == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            int pageNr = Integer.parseInt(pageNrStr);
            int pageSize = Integer.parseInt(pageSizeStr);

            DigitalSongListSchema digitalSongListDTOS = new DigitalSongListSchema(searchDigitalSongService.digitalSongByTitle(song, pageNr, pageSize));
            return Response.ok(digitalSongListDTOS, MediaType.APPLICATION_JSON).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

}

