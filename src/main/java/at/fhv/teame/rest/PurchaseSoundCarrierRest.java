package at.fhv.teame.rest;

import at.fhv.teame.application.impl.AuthenticationServiceImpl;
import at.fhv.teame.application.impl.PurchaseSoundCarrierServiceImpl;
import at.fhv.teame.application.impl.SearchSoundCarrierServiceImpl;
import at.fhv.teame.sharedlib.dto.ShoppingCartDTO;
import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;
import at.fhv.teame.sharedlib.ejb.PurchaseSoundCarrierServiceRemote;
import at.fhv.teame.sharedlib.exceptions.InvalidSessionException;
import at.fhv.teame.sharedlib.exceptions.PurchaseFailedException;

import javax.ws.rs.*;
import java.util.List;

@Path("/soundCarrier/purchase")
public class PurchaseSoundCarrierRest {


    private PurchaseSoundCarrierServiceRemote purchaseSoundCarrierService;

    public PurchaseSoundCarrierRest() {
        purchaseSoundCarrierService = new PurchaseSoundCarrierServiceImpl();
    }

    @POST
    @Consumes("application/json")
    public void PurchaseSoundCarrier(ShoppingCartDTO shoppingCartDTO) throws PurchaseFailedException {
        purchaseSoundCarrierService.confirmPurchase(shoppingCartDTO);
    }
}

