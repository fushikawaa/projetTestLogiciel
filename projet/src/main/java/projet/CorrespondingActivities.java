package projet;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;


public class CorrespondingActivities {
    private String activityJsonPath;
    private List<Activity> activities;
    private CoordinatesManager coordinatesManager;
    private FileManager fileManager;

    public CorrespondingActivities(String path, FileManager fileManager, CoordinatesManager coordinatesManager) {
        this.activityJsonPath = path;
        this.activities = new ArrayList<>();
        this.fileManager = fileManager;
        getAllActivity();
        this.coordinatesManager = coordinatesManager;
    }

    public void getAllActivity() {
        try {
            activities = fileManager.getAllElements(activityJsonPath, new TypeReference<List<Activity>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    } 

    public List<Activity> findActivities(UserPreferences userPreferences, TravelRequirements travelRequirements, BigDecimal budget, Hotel hotel) {
        List<Activity> goodActivities = new ArrayList<>();

        BigDecimal money = budget;
        for(Activity activity : activities) {
            if(activity.getDate().isAfter(travelRequirements.getDepartureDate()) 
            && activity.getDate().isBefore(travelRequirements.getEndDate())
            && (activity.getCategory().equals(userPreferences.getPreferedActivity())
            || activity.getCategory().equals(userPreferences.getSecondActivity()))
            && activity.getPrice().compareTo(money) <= 0
            ) {
                double[] cooHotel;
                double[] cooActivity;
                try {
                    cooHotel = coordinatesManager.getCoordinates(hotel.getAddress());
                    cooActivity = coordinatesManager.getCoordinates(activity.getAddress());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                
                BigDecimal dist = new BigDecimal(coordinatesManager.calculateDistance(cooHotel[0], cooHotel[1], cooActivity[0], cooActivity[1]));
                
                if(travelRequirements.getActivityDistance().compareTo(dist) >= 0 ) {
                    goodActivities.add(activity);
                    money = money.subtract(activity.getPrice());
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
