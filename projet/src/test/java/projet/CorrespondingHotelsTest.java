package projet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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

public class CorrespondingHotelsTest{

    @SuppressWarnings("unchecked")
    @Test
    public void testFindHotelsInGoodCity() throws IOException{ 
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        //On simule l'utilisation de getAllElements
        ArrayList<Hotel> mocksHotels = new ArrayList<>();
        mocksHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));
        mocksHotels.add(new Hotel("Marseille Hotel 6", "6 Cours Julien, Marseille", "Marseille", 3, new BigDecimal(50.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); //le path ne sera pas utilisé dans les tests unitaires

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(1000));

        assertEquals(1, hotels.size());
        assertEquals("Marseille Hotel 6", hotels.get(0).getName());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindNoHotelsInGoodCity() throws IOException{
        FileManager mockFileManager = Mockito.mock(FileManager.class);
        
        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Paris Hotel 1", "1 Avenue des Champs-Élysées, Paris", "Paris", 3, new BigDecimal(50.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 5", "5 Place Gambetta, Bordeaux", "Bordeaux", 5, new BigDecimal(100.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(10000));


        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(10000));

        assertTrue(hotels.isEmpty());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindNoHotelsWithMinimumStars() throws IOException {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

       
        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Bordeaux Hotel 8", "8 Rue Notre-Dame, Bordeaux",  "Bordeaux", 1, new BigDecimal(50.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 5", "5 Place Gambetta, Bordeaux", "Bordeaux", 1, new BigDecimal(100.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Bordeaux", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(10000));



        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(10000));

        assertTrue(hotels.isEmpty());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindHotelsWithPricePreferences() throws IOException {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Bordeaux Hotel 8", "8 Rue Notre-Dame, Bordeaux",  "Bordeaux", 3, new BigDecimal(150.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 5", "5 Place Gambetta, Bordeaux", "Bordeaux", 2, new BigDecimal(100.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(10000));


        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(10000));

        assertEquals(1, hotels.size());
        assertEquals("Bordeaux Hotel 5", hotels.get(0).getName());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindHotelsWithPricePreferencesThenStars() throws IOException {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Bordeaux Hotel 8", "8 Rue Notre-Dame, Bordeaux",  "Bordeaux", 3, new BigDecimal(150.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 5", "5 Place Gambetta, Bordeaux", "Bordeaux", 3, new BigDecimal(100.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 9", "9 Cours Victor Hugo, Bordeaux", "Bordeaux", 2, new BigDecimal(100.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(10000));


        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(10000));


        assertEquals(1, hotels.size());
        assertEquals("Bordeaux Hotel 5", hotels.get(0).getName());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindHotelsWithStarsPreferences() throws IOException {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Bordeaux Hotel 8", "8 Rue Notre-Dame, Bordeaux",  "Bordeaux", 3, new BigDecimal(150.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 9", "9 Cours Victor Hugo, Bordeaux", "Bordeaux", 2, new BigDecimal(100.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(10000));

        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(10000));


        assertEquals(1, hotels.size());
        assertEquals("Bordeaux Hotel 8", hotels.get(0).getName());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindHotelsWithStarsPreferencesThenPrice() throws IOException {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Bordeaux Hotel 8", "8 Rue Notre-Dame, Bordeaux",  "Bordeaux", 3, new BigDecimal(150.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 5", "5 Place Gambetta, Bordeaux", "Bordeaux", 3, new BigDecimal(100.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 9", "9 Cours Victor Hugo, Bordeaux", "Bordeaux", 2, new BigDecimal(100.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(10000));



        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(10000));


        assertEquals(1, hotels.size());
        assertEquals("Bordeaux Hotel 5", hotels.get(0).getName());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindHotelsWithMultipleChoices() throws IOException {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Bordeaux Hotel 8", "8 Rue Notre-Dame, Bordeaux",  "Bordeaux", 3, new BigDecimal(100.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 5", "5 Place Gambetta, Bordeaux", "Bordeaux", 3, new BigDecimal(100.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 9", "9 Cours Victor Hugo, Bordeaux", "Bordeaux", 2, new BigDecimal(100.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(10000));

        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(10000));


        assertEquals(2, hotels.size());
        assertEquals("Bordeaux Hotel 8", hotels.get(0).getName());
        assertEquals("Bordeaux Hotel 5", hotels.get(1).getName());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindNoHotelsOverBudget() throws IOException {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Bordeaux Hotel 8", "8 Rue Notre-Dame, Bordeaux",  "Bordeaux", 3, new BigDecimal(200.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 5", "5 Place Gambetta, Bordeaux", "Bordeaux", 3, new BigDecimal(200.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(100));

        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(100));


        assertTrue(hotels.isEmpty());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testFindHotelsEqualsToBudget() throws IOException {
        FileManager mockFileManager = Mockito.mock(FileManager.class);

        ArrayList<Hotel> mocksHotels = new ArrayList<Hotel>();
        mocksHotels.add(new Hotel("Bordeaux Hotel 8", "8 Rue Notre-Dame, Bordeaux",  "Bordeaux", 3, new BigDecimal(100.0)));
        mocksHotels.add(new Hotel("Bordeaux Hotel 5", "5 Place Gambetta, Bordeaux", "Bordeaux", 3, new BigDecimal(100.0)));

        when(mockFileManager.getAllElements(anyString(), any(TypeReference.class))).thenReturn((List<Hotel>) mocksHotels);

        CorrespondingHotels correspondingHotels = new CorrespondingHotels("example.csv", mockFileManager); 

        UserPreferences userPreferences = new UserPreferences(TransportType.TRAIN, PrivilegedTransport.PRIX_MINIMUM, 2, PrivilegedHotel.PRIX_MINIMUM, ActivityType.SPORT, ActivityType.CULTURE);
        TravelRequirements travelRequirements = new TravelRequirements("Paris", "Marseille", "Paris", LocalDateTime.now(), LocalDateTime.now().plusDays(10), new BigDecimal(10), new BigDecimal(1000));

        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, new BigDecimal(100));


        assertTrue(hotels.isEmpty());
        verify(mockFileManager, times(1)).getAllElements(anyString(), any(TypeReference.class));
    }

}