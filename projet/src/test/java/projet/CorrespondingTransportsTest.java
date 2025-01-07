package projet;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

public class CorrespondingTransportsTest {

    @Test
    public void getAllTransportFull() {
        CorrespondingTransports correspondingTransports = mock(CorrespondingTransports.class);
        correspondingTransports.getAllTransport();
        assertNotEquals(len(correspondingTransports.getTransports), 0);
        
    }
}
