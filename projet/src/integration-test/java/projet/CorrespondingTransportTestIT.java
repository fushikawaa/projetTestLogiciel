package projet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CorrespondingTransportTestIT {
    
    @Test
    public void getAllTransportIsFull() {
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("src/data/transports_database.json");
        correspondingTransports.getAllTransport();

        assertEquals(500, correspondingTransports.getTransports().size());
    }

    @Test
    public void getAllTransportInexistantPathIsEmpty() {
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("/fqed/");
        correspondingTransports.getAllTransport();

        assertTrue(correspondingTransports.getTransports().isEmpty());
    }

    @Test
    public void getAllTransportBadPathIsEmpty() {
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("src/data/hotels_database.json");
        correspondingTransports.getAllTransport();

        assertTrue(correspondingTransports.getTransports().isEmpty());
    }

}
