package at.fhv.teame.application.rest;

import at.fhv.teame.application.api.AuthenticationServiceLocal;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Key;
import java.util.UUID;

@Path("/login")
public class AuthenticationRest {
    @EJB
    private AuthenticationServiceLocal authenticationService;

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/json")
    public String login(final MyLoginBean loginBean) throws LoginFailedException {
        String userId = authenticationService.loginCustomer(loginBean.username, loginBean.password);
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setSubject(userId)
                .signWith(key)
                .setId(UUID.randomUUID().toString())
                .compact();
    }
}
