package at.fhv.teame.rest.authentication;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info = @Info(
                title = "Music Shop",
                version = "1",
                description = "REST APIs of the Music Shop application"
        )
)
@ApplicationPath("/api")
public class JaxRsApplication extends Application {
    //'standalone.bat -c standalone-microprofile.xml'
    public static final Algorithm algorithm = Algorithm.HMAC256("XnF[>YCq_)Fn#KV7!A#C}{4Hh8b?Xz");

    public static String verifyToken(String token) throws JWTVerificationException {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build(); //Reusable verifier instance
            DecodedJWT jwt = verifier.verify(token);

            if (AuthenticationRest.isTokenInBlacklist(token)) {
                throw new JWTVerificationException("");
            }
            return jwt.getSubject();
        } catch (JWTVerificationException | NullPointerException exception) {
            //Invalid signature/claims
            throw new JWTVerificationException("Verification failed");
        }
    }
}
