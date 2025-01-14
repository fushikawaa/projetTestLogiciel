package projet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import projet.enums.ActivityType;
import projet.enums.TransportType;

public class FileManagerTest {

    @Test
    public void testWriteTravelsToFile() throws IOException {
        // Arrange
        FileManager fileManager = new FileManager();
        
        // Crée un fichier temporaire
        File tempFile = File.createTempFile("travels_test", ".json");
        tempFile.deleteOnExit(); // Supprimer automatiquement après le test
        
        // Instantiation de travel
        Hotel hotel = new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0));
        
        Activity activity = new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30));
        List<Activity> listActivities = new ArrayList<Activity>();
        listActivities.add(activity);
        
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);
        
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listReturnTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);
        Travel travel = new Travel(mocksFindGoTransports, hotel, listActivities, mocksFindReturnTransports, new BigDecimal(100));

        // Instantiation list d'erreurs
        List<String> errors = new ArrayList<>();
        errors.add("une erreur");

        List<TravelErrors> mockTravels = new ArrayList<>();
        mockTravels.add(new TravelErrors(travel, errors));
        
        // Act
        fileManager.writeTravelsToFile(tempFile.getPath(), mockTravels);
        
        // Assert
        ObjectMapper mapper = ObjectMapperProvider.getMapper();
        List<TravelErrors> readTravels = mapper.readValue(tempFile, new TypeReference<List<TravelErrors>>() {});
        
        // Vérifie que le contenu écrit correspond aux données mockées
        assertEquals(mockTravels.size(), readTravels.size());
        assertEquals(mockTravels.get(0).getTravel().getHotel().getName(), readTravels.get(0).getTravel().getHotel().getName());
        assertEquals(mockTravels.get(0).getTravel().getActivities().get(0).getName(), readTravels.get(0).getTravel().getActivities().get(0).getName());
        assertEquals(mockTravels.get(0).getErrors(), readTravels.get(0).getErrors());
    }

    @Test
    public void testWriteTravelsToFile_ParentDirectoryDoesNotExist() throws IOException {
        // Arrange
        FileManager fileManager = new FileManager();
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "nonexistentDir");
        File tempFile = new File(tempDir, "travels_test.json");

        // Instantiation de travel
        Hotel hotel = new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0));
        
        Activity activity = new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30));
        List<Activity> listActivities = new ArrayList<Activity>();
        listActivities.add(activity);
        
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);
        
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listReturnTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);
        Travel travel = new Travel(mocksFindGoTransports, hotel, listActivities, mocksFindReturnTransports, new BigDecimal(100));

        // Crée un TravelErrors pour les données
        TravelErrors travelErrors = new TravelErrors(
            travel,
            List.of("Error 1")
        );
        List<TravelErrors> mockTravels = List.of(travelErrors);

        // Act
        fileManager.writeTravelsToFile(tempFile.getPath(), mockTravels);

        // Assert
        assertTrue(tempDir.exists()); // Le répertoire parent doit exister
        assertTrue(tempFile.exists()); // Le fichier doit être créé
    }

    @Test
    public void testWriteTravelsToFile_FileDoesNotExist() throws IOException {
        // Arrange
        FileManager fileManager = new FileManager();
        File tempFile = new File(System.getProperty("java.io.tmpdir"), "nonexistentFile.json");

        // Instantiation de travel
        Hotel hotel = new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0));
        
        Activity activity = new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30));
        List<Activity> listActivities = new ArrayList<Activity>();
        listActivities.add(activity);
        
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);
        
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listReturnTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);
        Travel travel = new Travel(mocksFindGoTransports, hotel, listActivities, mocksFindReturnTransports, new BigDecimal(100));

        // Crée un TravelErrors pour les données
        TravelErrors travelErrors = new TravelErrors(
            travel,
            List.of("Error 1")
        );
        List<TravelErrors> mockTravels = List.of(travelErrors);

        // Act
        fileManager.writeTravelsToFile(tempFile.getPath(), mockTravels);

        // Assert
        assertTrue(tempFile.exists()); // Le fichier doit être créé
    }

    @Test
    public void testWriteTravelsToFile_ExceptionThrown() {
        // Arrange
        FileManager fileManager = new FileManager();
        String invalidPath = "/invalid/path/travels_test.json";

        // Instantiation de travel
        Hotel hotel = new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0));
        
        Activity activity = new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30));
        List<Activity> listActivities = new ArrayList<Activity>();
        listActivities.add(activity);
        
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);
        
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listReturnTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);
        Travel travel = new Travel(mocksFindGoTransports, hotel, listActivities, mocksFindReturnTransports, new BigDecimal(100));

        // Crée un TravelErrors pour les données
        TravelErrors travelErrors = new TravelErrors(
            travel,
            List.of("Error 1")
        );
        List<TravelErrors> mockTravels = List.of(travelErrors);

        // Act & Assert
        assertThrows(IOException.class, () -> fileManager.writeTravelsToFile(invalidPath, mockTravels));
    }

}

