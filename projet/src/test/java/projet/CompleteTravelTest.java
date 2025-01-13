package projet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.type.TypeReference;

import projet.enums.ActivityType;
import projet.enums.PrivilegedHotel;
import projet.enums.PrivilegedTransport;
import projet.enums.TransportType;

public class CompleteTravelTest {
    @SuppressWarnings("unchecked")
    @Test
    public void testCreateTravels_MatchingAttributes_AllInList_NoError() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockFileManager.getAllElements(eq("activity.json"), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Liste simulée d'hotels
        ArrayList<Hotel> mocksHotels = new ArrayList<>();
        mocksHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));

        when(mockFileManager.getAllElements(eq("hotel.json"), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        // Liste simulée de transports
        ArrayList<Transport> mocksTransports = new ArrayList<>();
        mocksTransports.add(new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION));
        mocksTransports.add(new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN));

        when(mockFileManager.getAllElements(eq("transport.json"), any(TypeReference.class))).thenReturn((List<Transport>) mocksTransports);

        // Création des instances corresponding avec les objets simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("activity.json", mockFileManager);
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("hotel.json", mockFileManager);
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("transport.json", mockFileManager);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(correspondingTransports, correspondingHotels, correspondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertTrue(travel.get(0).getErrors().isEmpty());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertEquals(1, travel.get(0).getTravel().getActivities().size());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());
        verify(mockFileManager, times(3)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateTravels_WrongGoTravel_OneError_AllTheOthersInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockFileManager.getAllElements(eq("activity.json"), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Liste simulée d'hotels
        ArrayList<Hotel> mocksHotels = new ArrayList<>();
        mocksHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));

        when(mockFileManager.getAllElements(eq("hotel.json"), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        // Liste simulée de transports
        ArrayList<Transport> mocksTransports = new ArrayList<>();
        mocksTransports.add(new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(5000.0), TransportType.AVION)); // Cost too much
        mocksTransports.add(new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN));

        when(mockFileManager.getAllElements(eq("transport.json"), any(TypeReference.class))).thenReturn((List<Transport>) mocksTransports);

        // Création des instances corresponding avec les objets simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("activity.json", mockFileManager);
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("hotel.json", mockFileManager);
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("transport.json", mockFileManager);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(correspondingTransports, correspondingHotels, correspondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertEquals(1, travel.get(0).getErrors().size());
        assertTrue(travel.get(0).getTravel().getGoTrip().isEmpty());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertEquals(1, travel.get(0).getTravel().getActivities().size());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());
        verify(mockFileManager, times(3)).getAllElements(anyString(), any(TypeReference.class));
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testCreateTravels_WrongReturnTravel_OneError_AllTheOthersInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockFileManager.getAllElements(eq("activity.json"), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Liste simulée d'hotels
        ArrayList<Hotel> mocksHotels = new ArrayList<>();
        mocksHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));

        when(mockFileManager.getAllElements(eq("hotel.json"), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        // Liste simulée de transports
        ArrayList<Transport> mocksTransports = new ArrayList<>();
        mocksTransports.add(new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION));
        mocksTransports.add(new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(5000.0),  TransportType.TRAIN)); // Cost too much

        when(mockFileManager.getAllElements(eq("transport.json"), any(TypeReference.class))).thenReturn((List<Transport>) mocksTransports);

        // Création des instances corresponding avec les objets simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("activity.json", mockFileManager);
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("hotel.json", mockFileManager);
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("transport.json", mockFileManager);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(correspondingTransports, correspondingHotels, correspondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertEquals(1, travel.get(0).getErrors().size());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertTrue(travel.get(0).getTravel().getReturnTrip().isEmpty());
        assertEquals(1, travel.get(0).getTravel().getActivities().size());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());
        verify(mockFileManager, times(3)).getAllElements(anyString(), any(TypeReference.class));
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testCreateTravels_WrongHotel_OneError_AllTheOthersInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockFileManager.getAllElements(eq("activity.json"), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Liste simulée d'hotels
        ArrayList<Hotel> mocksHotels = new ArrayList<>();
        mocksHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(5000.0))); // Cost too much

        when(mockFileManager.getAllElements(eq("hotel.json"), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        // Liste simulée de transports
        ArrayList<Transport> mocksTransports = new ArrayList<>();
        mocksTransports.add(new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION)); 
        mocksTransports.add(new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN));

        when(mockFileManager.getAllElements(eq("transport.json"), any(TypeReference.class))).thenReturn((List<Transport>) mocksTransports);

        // Création des instances corresponding avec les objets simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("activity.json", mockFileManager);
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("hotel.json", mockFileManager);
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("transport.json", mockFileManager);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(correspondingTransports, correspondingHotels, correspondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertEquals(1, travel.get(0).getErrors().size());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertEquals(1, travel.get(0).getTravel().getActivities().size());
        assertNull(travel.get(0).getTravel().getHotel());
        verify(mockFileManager, times(3)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateTravels_WrongActivity_OneError_AllTheOthersInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(5000)));  // Cost too much

        when(mockFileManager.getAllElements(eq("activity.json"), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Liste simulée d'hotels
        ArrayList<Hotel> mocksHotels = new ArrayList<>();
        mocksHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));

        when(mockFileManager.getAllElements(eq("hotel.json"), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        // Liste simulée de transports
        ArrayList<Transport> mocksTransports = new ArrayList<>();
        mocksTransports.add(new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION)); 
        mocksTransports.add(new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN));

        when(mockFileManager.getAllElements(eq("transport.json"), any(TypeReference.class))).thenReturn((List<Transport>) mocksTransports);

        // Création des instances corresponding avec les objets simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("activity.json", mockFileManager);
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("hotel.json", mockFileManager);
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("transport.json", mockFileManager);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(correspondingTransports, correspondingHotels, correspondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertEquals(1, travel.get(0).getErrors().size());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertTrue(travel.get(0).getTravel().getActivities().isEmpty());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());
        verify(mockFileManager, times(3)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCreateTravels_MatchingAttributesMultipleTravels_AllInList_NoError() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockFileManager.getAllElements(eq("activity.json"), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Liste simulée d'hotels
        ArrayList<Hotel> mocksHotels = new ArrayList<>();
        mocksHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));
        mocksHotels.add(new Hotel("Paris Hotel 2", "2 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));

        when(mockFileManager.getAllElements(eq("hotel.json"), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        // Liste simulée de transports
        ArrayList<Transport> mocksTransports = new ArrayList<>();
        mocksTransports.add(new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION));
        mocksTransports.add(new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN));

        when(mockFileManager.getAllElements(eq("transport.json"), any(TypeReference.class))).thenReturn((List<Transport>) mocksTransports);

        // Création des instances corresponding avec les objets simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("activity.json", mockFileManager);
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("hotel.json", mockFileManager);
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("transport.json", mockFileManager);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(correspondingTransports, correspondingHotels, correspondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(2, travel.size());
        // First travel assertion
        assertTrue(travel.get(0).getErrors().isEmpty());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertEquals(1, travel.get(0).getTravel().getActivities().size());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());

        // Second travel assertion
        assertTrue(travel.get(1).getErrors().isEmpty());
        assertEquals(1, travel.get(1).getTravel().getGoTrip().size());
        assertEquals(1, travel.get(1).getTravel().getReturnTrip().size());
        assertEquals(1, travel.get(1).getTravel().getActivities().size());
        assertEquals("Paris Hotel 2", travel.get(1).getTravel().getHotel().getName());
        verify(mockFileManager, times(3)).getAllElements(anyString(), any(TypeReference.class));
    }
}
