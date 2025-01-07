package projet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.*;

public class CorrespondingActivities {
    private String activityCsvPath;
    private List<Activity> activities;
    private Hotel hotel;

    public CorrespondingActivities(String path) {
        this.activityCsvPath = path;
        this.activities = new ArrayList<>();
    }

    public void getAllActivity() {

        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(this.activityCsvPath))) {

            String header = br.readLine();
            if (header == null) {
                throw new IllegalArgumentException("Le fichier est vide ou incorrect.");
            }

            // Parcourir chaque ligne
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length != 5) {
                    System.out.println("Erreur: Ligne invalide, ignorée: " + line);
                    continue;
                }

                
                long ts = Long.valueOf(values[3]);
                BigDecimal price = new BigDecimal(values[4]);

                Activity activity = new Activity(values[0], values[1], values[2], ts, price);                
                activities.add(activity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    } 

    public List<Activity> findActivities(UserPreferences userPreferences, TravelRequirements travelRequirements) {
        List<Activity> goodActivities = new ArrayList<>();

        for(Activity activity : activities) {
            if(activity.getDate() > travelRequirements.getDepartureDate() 
            && activity.getDate() < travelRequirements.getEndDate()
            && (activity.getCategory() == userPreferences.getPreferedActivity()
            || activity.getCategory() == userPreferences.getSecondActivity())) {

                goodActivities.add(activity);

            }
        }



        return goodActivities;
    }


}
