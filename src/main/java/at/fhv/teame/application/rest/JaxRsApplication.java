package at.fhv.teame.application.rest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.security.Key;

@OpenAPIDefinition(info = @Info(
        title = "Music Shop",
        version = "1",
        description = "REST APIs of the Music Shop application"))
@ApplicationPath("/rest")
public class JaxRsApplication extends Application {
    private static Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //'standalone.bat -c standalone-microprofile.xml'
    public static Algorithm algorithm = Algorithm.HMAC256(String.valueOf(key));

    public static void verifyToken(String token) throws JWTVerificationException {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);
        } catch (JWTVerificationException | NullPointerException exception){
            //Invalid signature/claims
            throw new JWTVerificationException("Verification failed");
        }
    }
}
