package at.fhv.teame.application.rest;

import at.fhv.teame.application.api.AuthenticationServiceLocal;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import com.auth0.jwt.JWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.json.JSONObject;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static at.fhv.teame.application.rest.JaxRsApplication.algorithm;

@Path("/login")
public class AuthenticationRest {
    @EJB
    private AuthenticationServiceLocal authenticationService;

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Get token with username and password",
            responses = {
                    @ApiResponse(description = "The Token",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = String.class))),
                    @ApiResponse(responseCode = "401", description = "Login failed"),
                    @ApiResponse(responseCode = "400", description = "Bad Request"),
                    @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    public Response login(final LoginSchema login) {
        try {
            if(login.username == null || login.password == null) return Response.status(Response.Status.BAD_REQUEST).build();

            String userId = authenticationService.loginCustomer(login.username, login.password);

            String token = JWT.create()
                    .withSubject(userId)
                    .withExpiresAt(Date.from(Instant.now().plus(12, ChronoUnit.HOURS)))
                    .sign(algorithm);

            JSONObject tokenJson = new JSONObject("{\"token\":" + "\"" + token + "\"}");

            return Response.ok(tokenJson.toString(), MediaType.APPLICATION_JSON)
                    .build();

        } catch (LoginFailedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
