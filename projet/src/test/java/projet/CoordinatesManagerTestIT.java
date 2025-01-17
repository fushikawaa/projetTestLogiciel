package projet;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoordinatesManagerTestIT {
    
    @Test
    public void testGetCoordinates_ExistingAddress_ShouldReturnCoordinates() {
        CoordinatesManager coordinatesManager = new CoordinatesManager();
        String addr = "1 avenue des champs Elysées";

        double[] coord;
        try {
            coord = coordinatesManager.getCoordinates(addr);

            assertEquals(46.0360831, coord[0]);
            assertEquals(-73.4350982, coord[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testGetCoordinates_InvalidAddress_ShouldThrowException() {
        CoordinatesManager coordinatesManager = new CoordinatesManager();
        String invalidAddr = "AdresseInvalide123456";

        try {
            double[] expected = new double[]{};
            double[] coord = coordinatesManager.getCoordinates(invalidAddr);
            
            Assertions.assertArrayEquals(expected, coord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
