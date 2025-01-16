package projet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CorrespondingTransportsTestIT {
    @Test
    public void testGetAllTransports(){
        FileManager fileManager = new FileManager();
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("src/data/transports_database_test.json", fileManager);
        
        correspondingTransports.getAllTransport();

        List<Transport> allTransport = correspondingTransports.getTransports();
        
        assertEquals(12, allTransport.size());
    }

    @Test
    public void testGetAllTransportsEmpty(){
        FileManager fileManager = new FileManager();
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("src/data/empty_file.json", fileManager);
        
        correspondingTransports.getAllTransport();

        List<Transport> allTransport = correspondingTransports.getTransports();
        
        assertEquals(0, allTransport.size());
    }

    @Test
    public void testGetAllTransportsBadPath(){
        FileManager fileManager = new FileManager();
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("src/data/hotels_database_test.json", fileManager);
        
        correspondingTransports.getAllTransport();

        List<Transport> allTransport = correspondingTransports.getTransports();
        
        assertEquals(0, allTransport.size());
    }
}
