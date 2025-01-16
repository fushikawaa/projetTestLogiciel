package projet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;

import projet.enums.ActivityType;
import projet.enums.PrivilegedHotel;
import projet.enums.PrivilegedTransport;
import projet.enums.TransportType;


public class CorrespondingTransportsTest {

    // Test unitaire

    @Test
public void testFindTransportsNoTransportsAvailable() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn(new ArrayList<Transport>());

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.now(), new BigDecimal(100.0));

    assertTrue(transports.isEmpty());
    verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
}

@Test
public void testFindTransportsDirectMatch() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 05:48:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:18:00", formatter), 
        new BigDecimal(70.0), TransportType.TRAIN));

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(100.0));

    assertEquals(1, transports.size());
    assertEquals(1, transports.get(0).size());
    assertEquals("Marseille", transports.get(0).get(0).getDestinationCity());
}

@Test
public void testFindTransportsDirectMatchWithoutType() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 05:48:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:18:00", formatter), 
        new BigDecimal(70.0), null));

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(100.0));

    assertEquals(1, transports.size());
    assertEquals(1, transports.get(0).size());
    assertEquals("Marseille", transports.get(0).get(0).getDestinationCity());
}



@Test
public void testFindTransportsWithStopover() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Lyon", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 08:00:00", formatter), 
        new BigDecimal(40.0), TransportType.TRAIN));
    mockTransports.add(new Transport("Lyon", "Marseille", 
        LocalDateTime.parse("2025-01-19 09:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 11:00:00", formatter), 
        new BigDecimal(50.0), TransportType.TRAIN));
    mockTransports.add(new Transport("Lyon", "Marseille", 
        LocalDateTime.parse("2025-01-19 09:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 11:00:00", formatter), 
        new BigDecimal(50.0), TransportType.AVION));

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(100.0));

    assertEquals(1, transports.size());
    assertEquals(2, transports.get(0).size());
    assertEquals("Lyon", transports.get(0).get(0).getDestinationCity());
    assertEquals("Marseille", transports.get(0).get(1).getDestinationCity());
}

@Test
public void testFindTransportsPreferredTransportType() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 05:48:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:18:00", formatter), 
        new BigDecimal(70.0), TransportType.AVION));
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:30:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(100.0));

    assertEquals(1, transports.size());
    assertEquals(1, transports.get(0).size());
    assertEquals(TransportType.TRAIN, transports.get(0).get(0).getType());
}

@Test
public void testFindTransportsWithMinimumPricePreference() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 05:48:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:18:00", formatter), 
        new BigDecimal(100.0), TransportType.TRAIN));
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:30:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(150.0));

    assertEquals(1, transports.size());
    assertEquals(new BigDecimal(80.0), transports.get(0).get(0).getPrice());
}

@Test
public void testFindTransportsWithMinimumPricePreferenceAndTimeDifference() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 05:48:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:18:00", formatter), 
        new BigDecimal(100.0), TransportType.TRAIN));
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:30:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:15:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(150.0));

    assertEquals(1, transports.size());
    assertEquals(LocalDateTime.parse("2025-01-19 09:15:00", formatter), transports.get(0).get(0).getDestinationDateTime());
}

@Test
public void testFindTransportsWithMinimumTime() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:30:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:15:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.DUREE_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(150.0));

    assertEquals(1, transports.size());
    assertEquals(LocalDateTime.parse("2025-01-19 09:15:00", formatter), transports.get(0).get(0).getDestinationDateTime());
}

@Test
public void testFindTransportsWithBadTimeTransport() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:30:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-18 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-18 09:15:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.DUREE_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(150.0));

    assertEquals(1, transports.size());
    assertEquals(LocalDateTime.parse("2025-01-19 09:30:00", formatter), transports.get(0).get(0).getDestinationDateTime());
}

@Test
public void testFindTransportsWithNoMoneyForStopover() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:30:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        mockTransports.add(new Transport("Marseille", "Nice", 
        LocalDateTime.parse("2025-01-19 10:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 12:00:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.DUREE_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Nice", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(150.0));

    assertEquals(0, transports.size());

}

@Test
public void testFindTransportsWithMinimumTimeAndMinimumPrice() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:30:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:15:00", formatter), 
        new BigDecimal(80.0), TransportType.TRAIN));
        mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:15:00", formatter), 
        new BigDecimal(65.0), TransportType.TRAIN));

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.DUREE_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(150.0));

    assertEquals(1, transports.size());
    assertEquals(LocalDateTime.parse("2025-01-19 09:15:00", formatter), transports.get(0).get(0).getDestinationDateTime());
    assertEquals(new BigDecimal(65.0), transports.get(0).get(0).getPrice());
}

//Test de mutation

@Test
public void testFindTransportsBoundaryConditionOnPrice() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 05:48:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:18:00", formatter), 
        new BigDecimal(100.0), TransportType.TRAIN)); 

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(100.0));

    assertTrue(transports.isEmpty());

}

@Test
public void testFindTransportsStopoverBoundaryConditionOnPrice() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Lyon", 
        LocalDateTime.parse("2025-01-19 06:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 08:00:00", formatter), 
        new BigDecimal(60.0), TransportType.TRAIN)); // Prix de la première partie
    mockTransports.add(new Transport("Lyon", "Marseille", 
        LocalDateTime.parse("2025-01-19 09:00:00", formatter), 
        LocalDateTime.parse("2025-01-19 11:00:00", formatter), 
        new BigDecimal(40.0), TransportType.TRAIN)); // Prix de la deuxième partie

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(100.0)); 

    assertTrue(transports.isEmpty());
}

@Test
public void testFindTransportsWithMinimumPricePreferenceAndTransportPriceEqualToBudget() throws IOException {
    FileManager mockFileManager = Mockito.mock(FileManager.class);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    ArrayList<Transport> mockTransports = new ArrayList<>();
    mockTransports.add(new Transport("Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 05:48:00", formatter), 
        LocalDateTime.parse("2025-01-19 09:18:00", formatter), 
        new BigDecimal(100.0), TransportType.TRAIN));

    when(mockFileManager.getAllElements(anyString(), any(TypeReference.class)))
        .thenReturn((List<Transport>) mockTransports);

    CorrespondingTransports correspondingTransports = new CorrespondingTransports("example.csv", mockFileManager);

    UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);

    ArrayList<ArrayList<Transport>> transports = correspondingTransports.findTransports(
        userPreferences, "Paris", "Marseille", 
        LocalDateTime.parse("2025-01-19 00:00:00", formatter), 
        new BigDecimal(100.0));

    assertEquals(0, transports.size());
}




}
