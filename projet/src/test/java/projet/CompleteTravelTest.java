package projet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import projet.enums.ActivityType;
import projet.enums.PrivilegedHotel;
import projet.enums.PrivilegedTransport;
import projet.enums.TransportType;

public class CompleteTravelTest {
    @Test
    public void testCreateTravels_MatchingAttributes_AllInList_NoError() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Liste simulée d'activités trouvées
        CorrespondingActivities mockCorrespondingActivities = Mockito.mock(CorrespondingActivities.class);
        List<Activity> mockFindActivities = new ArrayList<>();
        mockFindActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockCorrespondingActivities.findActivities(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class),
            any(Hotel.class)
            )).thenReturn((List<Activity>) mockFindActivities);
        
        // Liste simulée d'hotels trouvés
        CorrespondingHotels mockCorrespondingHotels = Mockito.mock(CorrespondingHotels.class);
        ArrayList<Hotel> mocksFindHotels = new ArrayList<>();
        mocksFindHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(5000.0))); // Cost too much

        when(mockCorrespondingHotels.findHotels(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class)
            )).thenReturn((List<Hotel>) mocksFindHotels);

        // Liste simulée de transports
        CorrespondingTransports mockCorrespondingTransports = Mockito.mock(CorrespondingTransports.class);

        // Pour l'aller
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Bordeaux"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindGoTransports);

        // Pour le retour
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listReturnTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Paris"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindReturnTransports);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(mockCorrespondingTransports, mockCorrespondingHotels, mockCorrespondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertTrue(travel.get(0).getErrors().isEmpty());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertEquals(1, travel.get(0).getTravel().getActivities().size());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());
    }

    @Test
    public void testCreateTravels_WrongGoTravel_OneError_AllTheOthersInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Liste simulée d'activités trouvées
        CorrespondingActivities mockCorrespondingActivities = Mockito.mock(CorrespondingActivities.class);
        List<Activity> mockFindActivities = new ArrayList<>();
        mockFindActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockCorrespondingActivities.findActivities(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class),
            any(Hotel.class)
            )).thenReturn((List<Activity>) mockFindActivities);
        
        // Liste simulée d'hotels trouvés
        CorrespondingHotels mockCorrespondingHotels = Mockito.mock(CorrespondingHotels.class);
        ArrayList<Hotel> mocksFindHotels = new ArrayList<>();
        mocksFindHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(5000.0))); // Cost too much

        when(mockCorrespondingHotels.findHotels(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class)
            )).thenReturn((List<Hotel>) mocksFindHotels);

        // Liste simulée de transports
        CorrespondingTransports mockCorrespondingTransports = Mockito.mock(CorrespondingTransports.class);

        // Pour l'aller
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Bordeaux"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindGoTransports);

        // Pour le retour
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listReturnTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Paris"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindReturnTransports);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(mockCorrespondingTransports, mockCorrespondingHotels, mockCorrespondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertEquals(1, travel.get(0).getErrors().size());
        assertTrue(travel.get(0).getTravel().getGoTrip().isEmpty());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertEquals(1, travel.get(0).getTravel().getActivities().size());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());
    }

    @Test
    public void testCreateTravels_WrongReturnTravel_OneError_AllTheOthersInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Liste simulée d'activités trouvées
        CorrespondingActivities mockCorrespondingActivities = Mockito.mock(CorrespondingActivities.class);
        List<Activity> mockFindActivities = new ArrayList<>();
        mockFindActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockCorrespondingActivities.findActivities(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class),
            any(Hotel.class)
            )).thenReturn((List<Activity>) mockFindActivities);
        
        // Liste simulée d'hotels trouvés
        CorrespondingHotels mockCorrespondingHotels = Mockito.mock(CorrespondingHotels.class);
        ArrayList<Hotel> mocksFindHotels = new ArrayList<>();
        mocksFindHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(5000.0))); // Cost too much

        when(mockCorrespondingHotels.findHotels(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class)
            )).thenReturn((List<Hotel>) mocksFindHotels);

        // Liste simulée de transports
        CorrespondingTransports mockCorrespondingTransports = Mockito.mock(CorrespondingTransports.class);

        // Pour l'aller
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Bordeaux"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindGoTransports);

        // Pour le retour
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Paris"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindReturnTransports);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(mockCorrespondingTransports, mockCorrespondingHotels, mockCorrespondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertEquals(1, travel.get(0).getErrors().size());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertTrue(travel.get(0).getTravel().getReturnTrip().isEmpty());
        assertEquals(1, travel.get(0).getTravel().getActivities().size());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());
    }

    @Test
    public void testCreateTravels_WrongHotel_OneError_AllTheOthersInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Liste simulée d'activités trouvées
        CorrespondingActivities mockCorrespondingActivities = Mockito.mock(CorrespondingActivities.class);
        List<Activity> mockFindActivities = new ArrayList<>();
        mockFindActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockCorrespondingActivities.findActivities(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class),
            any(Hotel.class)
            )).thenReturn((List<Activity>) mockFindActivities);
        
        // Liste simulée d'hotels trouvés
        CorrespondingHotels mockCorrespondingHotels = Mockito.mock(CorrespondingHotels.class);
        ArrayList<Hotel> mocksFindHotels = new ArrayList<>();
        
        when(mockCorrespondingHotels.findHotels(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class)
            )).thenReturn((List<Hotel>) mocksFindHotels);

        // Liste simulée de transports
        CorrespondingTransports mockCorrespondingTransports = Mockito.mock(CorrespondingTransports.class);

        // Pour l'aller
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Bordeaux"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindGoTransports);

        // Pour le retour
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listGoTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Paris"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindReturnTransports);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(mockCorrespondingTransports, mockCorrespondingHotels, mockCorrespondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertEquals(1, travel.get(0).getErrors().size());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertNull(travel.get(0).getTravel().getHotel());
        assertTrue(travel.get(0).getTravel().getActivities().isEmpty());
    }

    @Test
    public void testCreateTravels_WrongActivity_OneError_AllTheOthersInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Liste simulée d'activités trouvées
        CorrespondingActivities mockCorrespondingActivities = Mockito.mock(CorrespondingActivities.class);
        List<Activity> mockFindActivities = new ArrayList<>();

        when(mockCorrespondingActivities.findActivities(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class),
            any(Hotel.class)
            )).thenReturn((List<Activity>) mockFindActivities);
        
        // Liste simulée d'hotels trouvés
        CorrespondingHotels mockCorrespondingHotels = Mockito.mock(CorrespondingHotels.class);
        ArrayList<Hotel> mocksFindHotels = new ArrayList<>();
        mocksFindHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(5000.0))); // Cost too much

        when(mockCorrespondingHotels.findHotels(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class)
            )).thenReturn((List<Hotel>) mocksFindHotels);

        // Liste simulée de transports
        CorrespondingTransports mockCorrespondingTransports = Mockito.mock(CorrespondingTransports.class);

        // Pour l'aller
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Bordeaux"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindGoTransports);

        // Pour le retour
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listReturnTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Paris"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindReturnTransports);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(mockCorrespondingTransports, mockCorrespondingHotels, mockCorrespondingActivities, userPreferences, travelRequirements, mockFileManager);

        // Filtrer les activités
        List<TravelErrors> travel = completeTravel.createTravels();

        // Assertions
        assertEquals(1, travel.size());
        assertEquals(1, travel.get(0).getErrors().size());
        assertEquals(1, travel.get(0).getTravel().getGoTrip().size());
        assertEquals(1, travel.get(0).getTravel().getReturnTrip().size());
        assertTrue(travel.get(0).getTravel().getActivities().isEmpty());
        assertEquals("Paris Hotel 1", travel.get(0).getTravel().getHotel().getName());
    }

    @Test
    public void testCreateTravels_MatchingAttributesMultipleTravels_AllInList_NoError() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Liste simulée d'activités trouvées
        CorrespondingActivities mockCorrespondingActivities = Mockito.mock(CorrespondingActivities.class);
        List<Activity> mockFindActivities = new ArrayList<>();
        mockFindActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));

        when(mockCorrespondingActivities.findActivities(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class),
            any(Hotel.class)
            )).thenReturn((List<Activity>) mockFindActivities);
        
        // Liste simulée d'hotels trouvés
        CorrespondingHotels mockCorrespondingHotels = Mockito.mock(CorrespondingHotels.class);
        ArrayList<Hotel> mocksFindHotels = new ArrayList<>();
        mocksFindHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));
        mocksFindHotels.add(new Hotel("Paris Hotel 2", "2 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));

        when(mockCorrespondingHotels.findHotels(
            any(UserPreferences.class), 
            any(TravelRequirements.class), 
            any(BigDecimal.class)
            )).thenReturn((List<Hotel>) mocksFindHotels);

        // Liste simulée de transports
        CorrespondingTransports mockCorrespondingTransports = Mockito.mock(CorrespondingTransports.class);

        // Pour l'aller
        Transport goTransport = new Transport("Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusHours(3), new BigDecimal(50.0), TransportType.AVION);
        ArrayList<Transport> listGoTransports = new ArrayList<Transport>();
        listGoTransports.add(goTransport);
        ArrayList<ArrayList<Transport>> mocksFindGoTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindGoTransports.add(listGoTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Bordeaux"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindGoTransports);

        // Pour le retour
        Transport returnTransport = new Transport("Paris", "Bordeaux", LocalDateTime.now().plusDays(10), LocalDateTime.now().plusDays(10).plusHours(4), new BigDecimal(50.0),  TransportType.TRAIN);
        ArrayList<Transport> listReturnTransports = new ArrayList<Transport>();
        listReturnTransports.add(returnTransport);
        ArrayList<ArrayList<Transport>> mocksFindReturnTransports = new ArrayList<ArrayList<Transport>>();
        mocksFindReturnTransports.add(listReturnTransports);

        when(mockCorrespondingTransports.findTransports(
            any(UserPreferences.class), 
            eq("Paris"),
            any(String.class),
            any(LocalDateTime.class),
            any(BigDecimal.class)
            )).thenReturn((ArrayList<ArrayList<Transport>>) mocksFindReturnTransports);
        
        // Préférences utilisateur et critères de voyage
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 1,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Bordeaux", "Paris", "Bordeaux",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        CompleteTravel completeTravel = new CompleteTravel(mockCorrespondingTransports, mockCorrespondingHotels, mockCorrespondingActivities, userPreferences, travelRequirements, mockFileManager);

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
    }
}
