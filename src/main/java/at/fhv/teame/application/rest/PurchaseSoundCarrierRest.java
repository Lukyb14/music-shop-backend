package at.fhv.teame.application.rest;

import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.PurchaseFailedException;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/soundCarrier/purchase")
public class PurchaseSoundCarrierRest {

    @EJB
    private PurchaseSoundCarrierServiceRemote purchaseSoundCarrierService;

    @POST
    @Consumes("application/json")
    public void PurchaseSoundCarrier(ShoppingCartDTO shoppingCartDTO) throws PurchaseFailedException {
        purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO);
    }
}

