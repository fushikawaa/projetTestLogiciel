package projet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLStreamHandler;

import org.json.JSONArray;
import org.json.JSONObject;

public class CoordinatesManager {

    private final URLStreamHandler urlStreamHandler;

    // Constructeur par défaut
    public CoordinatesManager() {
        this.urlStreamHandler = null;
    }

    // Conctructeur pour pouvoir mocker URLStreamHandler
    public CoordinatesManager(URLStreamHandler urlStreamHandler) {
        this.urlStreamHandler = urlStreamHandler;
    }

    protected URL createUrl(String address) throws Exception {
        if (urlStreamHandler == null) {
            return new URL("https://geocode.maps.co/search?q=" + address.replace(" ", "%20") + "&api_key=678916bdb78b8494490842flg5735fc");
        }
        return new URL(null, "https://geocode.maps.co/search?q=" + address.replace(" ", "%20") + "&api_key=678916bdb78b8494490842flg5735fc", urlStreamHandler);
    }
    

    public double[] getCoordinates(String address) throws Exception {
        Thread.sleep(1000);

        URL url = createUrl(address);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        JSONArray jsonResponse = new JSONArray(response.toString());

        if (jsonResponse.length() > 0) {
            JSONObject firstResult = jsonResponse.getJSONObject(0);
            double latitude = firstResult.getDouble("lat");
            double longitude = firstResult.getDouble("lon");

            return new double[]{latitude, longitude};
        } else {
            return new double[]{};
        }
    }

    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
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
