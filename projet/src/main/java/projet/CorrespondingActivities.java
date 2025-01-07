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
            && activity.getPrice().compareTo(budget) <= 0) {

                goodActivities.add(activity);
            }
        }

        return goodActivities;
    }


}
