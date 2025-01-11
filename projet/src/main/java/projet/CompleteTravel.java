package projet;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class CompleteTravel {
    private final CorrespondingTransports correspondingTransports;
    private final CorrespondingHotels correspondingHotels;
    private final CorrespondingActivities correspondingActivities;
    private final UserPreferences userPreferences;
    private final TravelRequirements travelRequirements;
    private final FileManager fileManager;

    public CompleteTravel(CorrespondingTransports correspondingTransports, CorrespondingHotels correspondingHotels, CorrespondingActivities correspondingActivities, UserPreferences userPreferences, TravelRequirements travelRequirements, FileManager fileManager){
        this.correspondingTransports = correspondingTransports;
        this.correspondingHotels = correspondingHotels;
        this.correspondingActivities = correspondingActivities;
        this.userPreferences = userPreferences;
        this.travelRequirements = travelRequirements;
        this.fileManager = fileManager;
    }

    public List<Travel> createTravels(){
        List<Travel> correspondingTravels = new ArrayList<>();
        BigDecimal actualBudget = travelRequirements.getBudget();
        ArrayList<ArrayList<Transport>> goTransports = correspondingTransports.findTransports(userPreferences, travelRequirements.getDepartureCity(), travelRequirements.getTravelCity(), travelRequirements.getDepartureDate(), actualBudget);
        if(!goTransports.isEmpty()){
            actualBudget = actualBudget.subtract(calculateTransportPrice(goTransports.get(0)));
        }
        ArrayList<ArrayList<Transport>> returnTransports = correspondingTransports.findTransports(userPreferences, travelRequirements.getTravelCity(), travelRequirements.getFinalCity(), travelRequirements.getEndDate(), actualBudget);
        if(!returnTransports.isEmpty()){
            actualBudget = actualBudget.subtract(calculateTransportPrice(returnTransports.get(0)));
        }
        List<Hotel> hotels = correspondingHotels.findHotels(userPreferences, travelRequirements, actualBudget);
        if(!hotels.isEmpty()){
            BigDecimal daysBetween = new BigDecimal(ChronoUnit.DAYS.between(travelRequirements.getDepartureDate(), travelRequirements.getEndDate()));
            actualBudget = actualBudget.subtract(hotels.get(0).getPricePerNight().multiply(daysBetween));
            for(Hotel hotel : hotels){
                List<Activity> activities = correspondingActivities.findActivities(userPreferences, travelRequirements, actualBudget, hotel);
                Travel travel = new Travel(goTransports, hotel, activities, returnTransports);
                correspondingTravels.add(travel);
            }
        }


        return correspondingTravels;
    }

    public BigDecimal calculateTransportPrice(ArrayList<Transport> transports){
        BigDecimal price = new BigDecimal(0);
        for(Transport transport : transports){
            price = price.add(transport.getPrice());
        }
        return price;
    }

    public void writeTravelsToFile() throws IOException{
        List<Travel> travels = createTravels();
        fileManager.writeTravelsToFile("src/result/travel.json", travels);
    }

}
