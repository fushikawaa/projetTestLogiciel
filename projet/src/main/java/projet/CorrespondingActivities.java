package projet;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


import java.net.URL;
import org.json.JSONObject;

public class CorrespondingActivities {
    private String activityJsonPath;
    private List<Activity> activities;
    private Hotel hotel;

    public CorrespondingActivities(String path) {
        this.activityJsonPath = path;
        this.activities = new ArrayList<>();
    }

    public void getAllActivity() {

        try {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Activity> activityList = objectMapper.readValue(new File(this.activityJsonPath), new TypeReference<List<Activity>>() {});

        activities.addAll(activityList);

    } catch (Exception e) {
        e.printStackTrace();
    }

    } 

    public List<Activity> findActivities(UserPreferences userPreferences, TravelRequirements travelRequirements, BigDecimal budget) {
        List<Activity> goodActivities = new ArrayList<>();

        for(Activity activity : activities) {
            if(activity.getDate().isAfter(travelRequirements.getDepartureDate()) 
            && activity.getDate().isBefore(travelRequirements.getEndDate())
            && (activity.getCategory() == userPreferences.getPreferedActivity()
            || activity.getCategory() == userPreferences.getSecondActivity())
            && activity.getPrice().compareTo(budget) <= 0
            ) {
                try {
                    double[] cooHotel = getCoordinates(this.hotel.getAddress());
                    double[] cooActivity = getCoordinates(activity.getAddress());

                    BigDecimal dist = BigDecimal.valueOf(calculateDistance(cooHotel[0], cooHotel[1], cooActivity[0], cooActivity[1]));

                    if(travelRequirements.getActivityDistance().compareTo(dist) >= 0 ) {
                        goodActivities.add(activity);
                    }
                

                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return goodActivities;
    }

    
    
    public static double[] getCoordinates(String address) throws Exception {
        
        // Formater l'URL pour faire la requête à l'API
        String urlString = "https://geocode.maps.co/search?q=" + address.replace(" ", "%20");
        
        try {
            // Créer l'URL et ouvrir la connexion HTTP
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            // Lire la réponse
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            
            // Analyser la réponse JSON
            JSONObject jsonResponse = new JSONObject(response.toString());
            
            // Vérifier si des résultats sont retournés
            if (jsonResponse.length() > 0) {
                // Extraire les coordonnées de la première réponse
                JSONObject firstResult = jsonResponse.getJSONArray("results").getJSONObject(0);
                double latitude = firstResult.getDouble("lat");
                double longitude = firstResult.getDouble("lon");

                // Afficher les résultats
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                
                return new double[]{latitude, longitude};
            } else {
                System.out.println("Aucun résultat trouvé.");
                return new double[]{};
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new double[]{};
        }
        
    }
    

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int EARTH_RADIUS = 6371; // Rayon de la Terre en kilomètres

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                 + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                 * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c; // Distance en kilomètres
    }
}
