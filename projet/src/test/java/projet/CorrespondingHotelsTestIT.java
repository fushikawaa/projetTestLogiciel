package projet;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CorrespondingHotelsTestIT {
    @Test
    public void testGetAllHotels(){
        FileManager fileManager = new FileManager();
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("src/data/hotels_database_test.json", fileManager);
        
        correspondingHotels.getAllHotels();

        List<Hotel> allHotels = correspondingHotels.getCorrespondingHotels();
        
        assertEquals(7, allHotels.size());
    }
    
    @Test
    public void testGetAllHotelsEmpty(){
        FileManager fileManager = new FileManager();
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("src/data/empty_file.json", fileManager);
        
        correspondingHotels.getAllHotels();

        List<Hotel> allHotels = correspondingHotels.getCorrespondingHotels();
        
        assertEquals(0, allHotels.size());
    }
}
