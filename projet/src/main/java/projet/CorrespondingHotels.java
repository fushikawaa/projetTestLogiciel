package projet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.fasterxml.jackson.core.type.TypeReference;

import projet.enums.PrivilegedHotel;

public class CorrespondingHotels {
    private List<Hotel> correspondingHotels;
    private String jsonPath;
    private FileManager fileManager;

    public CorrespondingHotels(String jsonPath, FileManager fileManager) {
        this.jsonPath = jsonPath;
        this.fileManager = fileManager;
        correspondingHotels = new ArrayList<>();
        getAllHotels();
    }

    // Méthode pour récupérer tous les hôtels
    public void getAllHotels() {
        try {
            correspondingHotels = fileManager.getAllElements(jsonPath, new TypeReference<List<Hotel>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Hotel> getCorrespondingHotels() {
        return correspondingHotels;
    }

    // Méthode pour récupérer les hôtels correspondant aux préférences de l'utilisateur
    public List<Hotel> findHotels(UserPreferences userPreferences, TravelRequirements travel, BigDecimal budget) {
        List<Hotel> hotelsFound = new ArrayList<>();

        // Pour garder uniquement les hôtels respectant les conditions
        BigDecimal daysBetween;
        BigDecimal priceForAllNights;
        for (Hotel hotel: correspondingHotels) {
            if (hotel.getCity().equals(travel.getTravelCity()) &&
                hotel.getStars() >= userPreferences.getMinNumberStars()
            ) {
                daysBetween = new BigDecimal(ChronoUnit.DAYS.between(travel.getDepartureDate(), travel.getEndDate()));
                priceForAllNights = hotel.getPricePerNight().multiply(daysBetween);
                if (priceForAllNights.compareTo(budget) <= 0) {
                    hotelsFound.add(hotel);
                }
            } 
        }
        
        if (hotelsFound.isEmpty()) {
            return hotelsFound;
        }
        setCorrespondingHotels(hotelsFound);

        selectBestHotels(userPreferences);
        return correspondingHotels;
    }

    private void selectBestHotels(UserPreferences userPreferences) {
        
        if (userPreferences.getPrivilegedHotel() == PrivilegedHotel.PRIX_MINIMUM) {
            // 1. Trouver le prix minimum
            BigDecimal minPrice = correspondingHotels.stream()
                .map(Hotel::getPricePerNight)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

            // 2. Filtrer par le prix minimum
            List<Hotel> filteredByPrice = correspondingHotels.stream()
                .filter(hotel -> hotel.getPricePerNight().compareTo(minPrice) == 0)
                .collect(Collectors.toList());

            // 3. Trouver les étoiles maximales parmi les hôtels filtrés
            int maxStars = filteredByPrice.stream()
                .mapToInt(Hotel::getStars)
                .max()
                .orElse(0);

            // 4. Filtrer par étoiles maximales
            correspondingHotels = filteredByPrice.stream()
                .filter(hotel -> hotel.getStars() == maxStars)
                .collect(Collectors.toList());

        } else { // userPreferences.getPrivilegedHotel() == PrivilegedHotel.NOMBRE_ETOILES
            // 1. Trouver le nombre d'étoiles maximum
            int maxStars = correspondingHotels.stream()
                .mapToInt(Hotel::getStars)
                .max()
                .orElse(0);

            // 2. Filtrer par étoiles maximales
            List<Hotel> filteredByStars = correspondingHotels.stream()
                .filter(hotel -> hotel.getStars() == maxStars)
                .collect(Collectors.toList());

            // 3. Trouver le prix minimum parmi les hôtels filtrés
            BigDecimal minPrice = filteredByStars.stream()
                .map(Hotel::getPricePerNight)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

            // 4. Filtrer par prix minimum
            correspondingHotels = filteredByStars.stream()
                .filter(hotel -> hotel.getPricePerNight().compareTo(minPrice) == 0)
                .collect(Collectors.toList());
        }
    }

    public void setCorrespondingHotels(List<Hotel> hotels){
        this.correspondingHotels = hotels;
    }
}
