package projet;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import projet.enums.PrivilegedHotel;

public class CorrespondingHotels {
    private List<Hotel> correspondingHotels;
    private String jsonPath;

    public CorrespondingHotels(String jsonPath) {
        this.jsonPath = jsonPath;
        correspondingHotels = new ArrayList<>();
    }

    // Méthode pour charger les hôtels depuis le fichier JSON
    private void loadHotelsFromJson() {
        Gson gson = new Gson();
        try {
            String jsonContent = new String(Files.readAllBytes(Paths.get(jsonPath)));
            correspondingHotels = gson.fromJson(jsonContent, new TypeToken<List<Hotel>>() {}.getType());
        } catch (IOException e) {
            System.err.println("Erreur lors de la lecture du fichier JSON : " + e.getMessage());
        }
    }

    // Méthode pour récupérer tous les hôtels
    public List<Hotel> getAllHotels() {
        loadHotelsFromJson();
        return correspondingHotels;
    }

    public List<Hotel> getCorrespondingHotels() {
        return correspondingHotels;
    }

    // Méthode pour récupérer les hôtels correspondant aux préférences de l'utilisateur
    public List<Hotel> findHotels(UserPreferences userPreferences, TravelRequirements travel, BigDecimal budget) {
        loadHotelsFromJson();

        // Pour garder uniquement les hôtels respectant les conditions
        BigDecimal daysBetween;
        BigDecimal priceForAllNights;
        for (int i = 0; i < correspondingHotels.size(); ++i) {
            if (correspondingHotels.get(i).getCity() == travel.getTravelCity() &&
                correspondingHotels.get(i).getStars() >= userPreferences.getMinNumberStars()
            ) {
                daysBetween = new BigDecimal(ChronoUnit.DAYS.between(travel.getDepartureDate(), travel.getEndDate()));
                priceForAllNights = correspondingHotels.get(i).getPricePerNight().multiply(daysBetween);
                if (priceForAllNights.compareTo(budget) > 0) {
                    correspondingHotels.remove(i);
                }
            } else {
                correspondingHotels.remove(i);
            }
        }

        if (correspondingHotels.isEmpty()) {
            return correspondingHotels;
        }

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

        } else if (userPreferences.getPrivilegedHotel() == PrivilegedHotel.NOMBRE_ETOILES) {
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
