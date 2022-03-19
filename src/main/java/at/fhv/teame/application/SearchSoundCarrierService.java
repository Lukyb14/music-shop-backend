package at.fhv.teame.application;

import at.fhv.teame.sharedlib.dto.SoundCarrierDTO;

import java.util.List;

public interface SearchSoundCarrierService {

    List<SoundCarrierDTO> getAllSoundCarriers();
}
