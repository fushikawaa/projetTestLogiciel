package projet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CorrespondingTransportTestIT {
    
    @Test
    public void getAllTransportFull() {
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("../../../../src/data/transports_database.json");
        correspondingTransports.getAllTransport();

        assertEquals(500, correspondingTransports.getTransports().size());
    }


}
