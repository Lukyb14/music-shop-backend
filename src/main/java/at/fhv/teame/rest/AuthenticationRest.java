package at.fhv.teame.rest;

import at.fhv.teame.application.impl.AuthenticationServiceImpl;
import at.fhv.teame.domain.model.user.ClientUser;
import at.fhv.teame.sharedlib.exceptions.LoginFailedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Key;
import java.util.UUID;

@Path("/login")
public class AuthenticationRest {
    private AuthenticationServiceImpl authenticationService = new AuthenticationServiceImpl();

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/json")
    public String login(final MyLoginBean loginBean) throws LoginFailedException {
        ClientUser clientUser = authenticationService.loginClient(loginBean.username, loginBean.password);
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        return Jwts.builder()
                .setSubject(clientUser.getCn())
                .claim("role", clientUser.getRole().toString())
                .claim("Session-ID", UUID.randomUUID().toString())
                .signWith(key)
                .setId(UUID.randomUUID().toString())
                .compact();
    }
}
