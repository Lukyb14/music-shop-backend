package at.fhv.teame.rest.digitalsong;

import at.fhv.teame.rest.exceptions.MissingParameterException;
import at.fhv.teame.rest.schema.TotalSongResultsSchema;
import at.fhv.teame.sharedlib.dto.DigitalSongDTO;
import at.fhv.teame.sharedlib.ejb.SearchDigitalSongServiceRemote;
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

@Path("/v1/songs")
public class SearchDigitalSongRest {

    @EJB
    private SearchDigitalSongServiceRemote searchDigitalSongService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get songs by either artist, genre or title",
            description = "When querying with more than one parameter following precedence is taken: 1) artist 2) title 3) genre"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of songs",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    array = @ArraySchema(schema = @Schema(implementation = DigitalSongDTO.class))
            )
    )
    @ApiResponse(responseCode = "400", description = "No search parameters specified or pageNr/pageSize is not a number")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response songs(@QueryParam("artist") String artist,
                          @QueryParam("genre") String genre,
                          @QueryParam("title") String title,
                          @QueryParam("pageNr") String pageNrStr,
                          @QueryParam("pageSize") String pageSizeStr) {
        try {
            List<DigitalSongDTO> digitalSongDTOs = searchSongs(artist, genre, title, pageNrStr, pageSizeStr);
            return Response.ok(digitalSongDTOs, MediaType.APPLICATION_JSON).build();
        } catch (NumberFormatException | MissingParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GET
    @Path("/total-results")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            summary = "Get the total song results by either artist, genre or title",
            description = "When querying with more than one parameter following precedence is taken: 1) artist 2) title 3) genre"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Total results of songs",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = TotalSongResultsSchema.class)
            )
    )
    @ApiResponse(responseCode = "400", description = "No search parameters specified")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response totalResults(@QueryParam("artist") String artist, @QueryParam("genre") String genre, @QueryParam("title") String title) {
        try {
            int results = countResults(artist, genre, title);
            return Response
                    .ok(new TotalSongResultsSchema(results), MediaType.APPLICATION_JSON)
                    .build();
        } catch (MissingParameterException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<DigitalSongDTO> searchSongs(String artist, String genre, String title, String pageNrStr, String pageSizeStr) throws MissingParameterException {
        if (pageNrStr != null && pageSizeStr != null) {
            int pageNr = Integer.parseInt(pageNrStr);
            int pageSize = Integer.parseInt(pageSizeStr);
            if (artist != null) {
                return searchDigitalSongService.digitalSongByArtist(artist, pageNr, pageSize);
            } else if (title != null) {
                return searchDigitalSongService.digitalSongByTitle(title, pageNr, pageSize);
            } else if (genre != null) {
                return searchDigitalSongService.digitalSongByGenre(genre, pageNr, pageSize);
            } else {
                throw new MissingParameterException();
            }
        } else {
            throw new MissingParameterException();
        }
    }

    private int countResults(String artist, String genre, String title) throws MissingParameterException {
        if (artist != null) {
            return searchDigitalSongService.totResultsByArtistName(artist);
        } else if (title != null) {
            return searchDigitalSongService.totResultsByTitle(title);
        } else if (genre != null) {
            return searchDigitalSongService.totResultsByGenre(genre);
        } else {
            throw new MissingParameterException();
        }
    }
}
