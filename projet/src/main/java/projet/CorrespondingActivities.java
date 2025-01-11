package projet;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


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
                    double[] cooHotel = CoordinatesManager.getCoordinates(this.hotel.getAddress());
                    double[] cooActivity = CoordinatesManager.getCoordinates(activity.getAddress());

                    BigDecimal dist = BigDecimal.valueOf(CoordinatesManager.calculateDistance(cooHotel[0], cooHotel[1], cooActivity[0], cooActivity[1]));

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

    public void setActivities(List<Activity> activities){
        this.activities = activities;
    }

    public List<Activity> getActivities(){
        return this.activities;
    }
}
