package projet;

// Definition of the TravelRequirements class
import java.math.BigDecimal;

public class TravelRequirements {

    private String departureCity;
    private String travelCity;
    private String finalCity;
    private long departureDate;
    private long endDate;
    private BigDecimal activityDistance;

    // Constructor
    public TravelRequirements(String departureCity, String travelCity, String finalCity, long departureDate, long endDate, BigDecimal activityDistance) {
        this.departureCity = departureCity;
        this.travelCity = travelCity;
        this.finalCity = finalCity;
        this.departureDate = departureDate;
        this.endDate = endDate;
        this.activityDistance = activityDistance;
    }

    // Getters and Setters

    public String getDepartureCity() {
        return departureCity;
    }

    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }

    public String getTravelCity() {
        return travelCity;
    }

    public void setTravelCity(String travelCity) {
        this.travelCity = travelCity;
    }

    public String getFinalCity() {
        return finalCity;
    }

    public void setFinalCity(String finalCity) {
        this.finalCity = finalCity;
    }

    public long getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(long departureDate) {
        this.departureDate = departureDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getActivityDistance() {
        return activityDistance;
    }

    public void setActivityDistance(BigDecimal activityDistance) {
        this.activityDistance = activityDistance;
    }
}
