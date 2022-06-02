package at.fhv.teame.rest.authentication;

import at.fhv.teame.middleware.api.AuthenticationServiceLocal;
import at.fhv.teame.rest.schema.LoginSchema;
import at.fhv.teame.rest.schema.TokenSchema;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
import java.util.HashSet;
import java.util.Set;

@Path("/")
public class AuthenticationRest {
    @EJB
    private AuthenticationServiceLocal authenticationService;
    private static final Set<String> invalidTokenBlacklist = new HashSet<>();

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Login with username and password, returns token as cookie")
    @ApiResponse(
            responseCode = "200",
            description = "Login successful",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = TokenSchema.class)
            )
    )
    @ApiResponse(responseCode = "401", description = "Login failed")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response login(final LoginSchema login) {
        try {
            if (login.mail == null || login.password == null)
                return Response.status(Response.Status.BAD_REQUEST).build();

            String userId = authenticationService.loginCustomer(login.mail, login.password);

            String token = JWT.create()
                    .withSubject(userId)
                    .withExpiresAt(Date.from(Instant.now().plus(12, ChronoUnit.HOURS)))
                    .sign(JaxRsApplication.algorithm);

            return Response
                    .ok("{\"token\": \"" + token + "\"}", MediaType.APPLICATION_JSON)
                    .build();

        } catch (LoginFailedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Logout by invalidating the token")
    @ApiResponse(responseCode = "204", description = "Logout successful")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public Response logout(final TokenSchema token) {
        if (token == null || token.token == null) return Response.status(Response.Status.BAD_REQUEST).build();

        try {
            // only blacklist valid tokens
            JaxRsApplication.verifyToken(token.token);
            invalidTokenBlacklist.add(token.token);
        } catch (JWTVerificationException ignored) {}

        return Response
                .status(Response.Status.NO_CONTENT)
                .build();
    }

    public static boolean isTokenInBlacklist(String token) {
        return invalidTokenBlacklist.contains(token);
    }
}
