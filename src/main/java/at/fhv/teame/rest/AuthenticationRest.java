package at.fhv.teame.rest;

import at.fhv.teame.sharedlib.dto.SessionDTO;
import at.fhv.teame.sharedlib.ejb.AuthenticationServiceRemote;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;

import javax.ws.rs.*;

@Path("/login")
public class AuthenticationRest {

    private AuthenticationServiceRemote authenticationService;

    @GET
    @Produces("text/plain")
    @Consumes("text/plain")
    public SessionDTO login(@QueryParam("username") String username, @QueryParam("password") String password) throws LoginFailedException {

        return authenticationService.login(username,password);

    }
}
