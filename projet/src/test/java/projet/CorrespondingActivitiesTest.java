package projet;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
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

public class CorrespondingActivitiesTest{

    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_MatchingActivities_AllInList() throws Exception {
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
        mockActivities.add(new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités
        List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(100), hotel);

        // Assertions
        assertEquals(2, activities.size());
        assertTrue(activities.stream().anyMatch(activity -> activity.getName().equals("Sport Activity")));
        assertTrue(activities.stream().anyMatch(activity -> activity.getName().equals("Culture Activity")));
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_ExactPriceActivity_InList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(100)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités
        List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(100), hotel);

        // Assertions
        assertEquals(1, activities.size());
        assertTrue(activities.stream().anyMatch(activity -> activity.getName().equals("Sport Activity")));
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_TooExpensiveActivity_NotInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Expensive Activity", ActivityType.CULTURE, "15 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal("500.00"))); // Hors budget

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités
        List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(100), hotel);

        // Assertions
        assertEquals(0, activities.size());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_TooExpensiveMultipleActivities_OnlyFavoriteActivityInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager"Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(10000));

        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));
        mockActivities.add(new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(60));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités
        List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(60), hotel);

        // Assertions
        assertEquals(1, activities.size());
        assertEquals("Sport Activity", activities.get(0).getName());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_TooFarActivities_NotInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(20.0); // Distance simulée à 20 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));
        mockActivities.add(new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités
        List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(100), hotel);

        // Assertions
        assertTrue(activities.isEmpty());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_NotWantedCategory_NotInList() throws Exception {
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
        mockActivities.add(new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.GASTRONOMIE, ActivityType.LOISIR);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités
        List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(100), hotel);

        // Assertions
        assertTrue(activities.isEmpty());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_BeforeTravelActivities_NotInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().minusDays(2), new BigDecimal(50)));
        mockActivities.add(new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().minusDays(5), new BigDecimal(30)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités
        List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(100), hotel);

        // Assertions
        assertTrue(activities.isEmpty());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_AfterTravelActivities_NotInList() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenReturn(new double[]{48.8566, 2.3522}); // Paris
        when(mockCoordinatesManager.calculateDistance(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
                .thenReturn(5.0); // Distance simulée à 5 km

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(20), new BigDecimal(50)));
        mockActivities.add(new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(50), new BigDecimal(30)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités
        List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(100), hotel);

        // Assertions
        assertTrue(activities.isEmpty());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindActivities_MatchingActivities_NoResponseGeocode_ShouldThrow() throws Exception {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        // Mock de CoordinatesManager
        CoordinatesManager mockCoordinatesManager = Mockito.mock(CoordinatesManager.class);
        when(mockCoordinatesManager.getCoordinates(anyString())).thenThrow(Exception.class);

        // Liste simulée d'activités
        List<Activity> mockActivities = new ArrayList<>();
        mockActivities.add(new Activity("Sport Activity", ActivityType.SPORT, "1 Sport Street, Paris",
                LocalDateTime.now().plusDays(2), new BigDecimal(50)));
        mockActivities.add(new Activity("Culture Activity", ActivityType.CULTURE, "10 Culture Avenue, Paris",
                LocalDateTime.now().plusDays(5), new BigDecimal(30)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Activity>) mockActivities);
        
        // Création d'une instance de CorrespondingActivities avec des activités simulées
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("example.json", mockFileManager, mockCoordinatesManager);
        
        // Préférences utilisateur, critères de voyage et hotel
        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 3,
            PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Marseille", "Paris", "Marseille",
            LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));
        Hotel hotel = new Hotel("Paris Hotel", "1 Avenue des Champs-Élysées, Paris", "Paris", 4, new BigDecimal(200));

        // Filtrer les activités & assertions
        assertNull(correspondingActivities.findActivities(userPreferences, travelRequirements, new BigDecimal(100), hotel));
    }
}