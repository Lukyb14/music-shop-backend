package at.fhv.teame.application.rest;

import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.PurchaseFailedException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.CookieParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/soundCarrier/purchase")
public class PurchaseSoundCarrierRest {

    @EJB
    private PurchaseSoundCarrierServiceRemote purchaseSoundCarrierService;

    @POST
    @Consumes("application/json")
    @Operation(summary = "Make a purchase with the given Sound Carriers")
    @ApiResponse(responseCode = "204", description = "Purchase successfully completed")
    @ApiResponse(responseCode = "401", description = "Token verification failed")
    @ApiResponse(responseCode = "400", description = "Bad Request")
    @ApiResponse(responseCode = "500", description = "Internal Server Error")
    public Response purchaseSoundCarrier(ShoppingCartDTO shoppingCartDTO, @CookieParam("token") String token) {
        try {
            if (shoppingCartDTO == null) return Response.status(Response.Status.BAD_REQUEST).build();
            JaxRsApplication.verifyToken(token);

            purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (JWTVerificationException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        } catch (PurchaseFailedException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}

