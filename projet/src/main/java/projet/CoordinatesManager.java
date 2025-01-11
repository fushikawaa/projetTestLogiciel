package projet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class CoordinatesManager {

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
