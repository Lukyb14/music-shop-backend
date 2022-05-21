package at.fhv.teame.rest;

import at.fhv.teame.middleware.api.AuthenticationServiceLocal;
import at.fhv.teame.rest.JaxRsApplication;
import at.fhv.teame.rest.LoginSchema;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import com.auth0.jwt.JWT;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;



@Path("/")
public class AuthenticationRest {
    @EJB
    private AuthenticationServiceLocal authenticationService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(summary = "Login with username and password, returns token as cookie")
    @ApiResponse(responseCode = "200", description = "Login successful", headers = { @Header(name = "Set-Cookie", description = "Auth token") })
    @ApiResponse(responseCode = "401", description = "Login failed")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response login(final LoginSchema login) {
        try {
            if(login.username == null || login.password == null) return Response.status(Response.Status.BAD_REQUEST).build();

            String userId = authenticationService.loginCustomer(login.username, login.password);

            String token = JWT.create()
                    .withSubject(userId)
                    .withExpiresAt(Date.from(Instant.now().plus(12, ChronoUnit.HOURS)))
                    .sign(JaxRsApplication.algorithm);

            return Response
                    .ok()
                    .cookie(new NewCookie("token", token))
                    .build();

        } catch (LoginFailedException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DELETE
    @Path("/logout")
    @Operation(summary = "Logout by deleting the cookie")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    public Response logout(@CookieParam("token") Cookie cookie) {
        if (cookie == null) return Response.status(Response.Status.BAD_REQUEST).build();

        return Response
                .ok()
                .cookie(new NewCookie(cookie, "delete cookie", 0, false))
                .build();
    }
}
