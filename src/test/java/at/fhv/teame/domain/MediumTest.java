package at.fhv.teame.domain;

import at.fhv.teame.domain.model.soundcarrier.Medium;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MediumTest {

    @Test
    void testMedium() {
        assertEquals("CD", Medium.CD.toString());
        assertEquals("VINYL", Medium.VINYL.toString());
        assertEquals("DIGITAL", Medium.DIGITAL.toString());
    }
}
