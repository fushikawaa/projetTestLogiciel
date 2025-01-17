package projet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLStreamHandler;

import org.junit.jupiter.api.Test;

public class CoordinatesManagerTest {
    
    @Test
    public void testGetCoordinates_ExistingAddress_ShouldReturnCoordinates() throws Exception {
        // Mock du HttpURLConnection
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream("[{\"lat\": 48.86985, \"lon\": 2.30745}]".getBytes()));

        // Mock du URLStreamHandler pour éviter de contacter une vraie API
        URLStreamHandler mockHandler = new URLStreamHandler() {
            @Override
            protected HttpURLConnection openConnection(URL u) throws IOException {
                return mockConnection;
            }
        };

        // Instancier CoordinatesManager avec le handler mocké
        CoordinatesManager coordinatesManager = new CoordinatesManager(mockHandler);

        // Exécuter le test
        double[] coord = coordinatesManager.getCoordinates("1 avenue des Champs-Élysées");

        // Vérifications
        assertEquals(48.86985, coord[0], 0.0001);
        assertEquals(2.30745, coord[1], 0.0001);
    }

    @Test
    public void testGetCoordinates_NotExistingAddress_ShouldReturnEmptyArray() throws Exception {
        // Mock du HttpURLConnection
        HttpURLConnection mockConnection = mock(HttpURLConnection.class);
        when(mockConnection.getInputStream()).thenReturn(new ByteArrayInputStream("[]".getBytes()));

        // Mock du URLStreamHandler pour éviter de contacter une vraie API
        URLStreamHandler mockHandler = new URLStreamHandler() {
            @Override
            protected HttpURLConnection openConnection(URL u) throws IOException {
                return mockConnection;
            }
        };

        // Instancier CoordinatesManager avec le handler mocké
        CoordinatesManager coordinatesManager = new CoordinatesManager(mockHandler);

        // Exécuter le test avec une adresse inexistante
        double[] coord = coordinatesManager.getCoordinates("Adresse Inexistante");

        // Vérification : le tableau retourné doit être vide
        assertNotNull(coord); // Vérifier que ce n'est pas null
        assertEquals(0, coord.length); // Vérifier que le tableau est vide
    }
}
