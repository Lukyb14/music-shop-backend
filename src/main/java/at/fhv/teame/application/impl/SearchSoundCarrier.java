package at.fhv.teame.application.impl;

import at.fhv.teame.application.SearchSoundCarrierService;
import at.fhv.teame.domain.SoundCarrier;
import at.fhv.teame.domain.repositories.SoundCarrierRepository;

import java.util.List;

public class SearchSoundCarrier implements SearchSoundCarrierService {


    private SoundCarrierRepository soundCarrierRepository;

    @Override
    public List<SoundCarrierDTO> getAllSoundCarriers() {

        List<SoundCarrier> soundCarriers = soundCarrierRepository.getAllSoundCarriers();
        

        return null;
    }
}
