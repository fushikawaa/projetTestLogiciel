package projet;

// Definition of the TravelRequirements class
import java.math.BigDecimal;
import java.time.Instant;

public class TravelRequirements {

    private String departureCity;
    private String travelCity;
    private String finalCity;
    private Instant departureDate;
    private Instant endDate;
    private BigDecimal activityDistance;

    // Constructor
    public TravelRequirements(String departureCity, String travelCity, String finalCity, Instant departureDate, Instant endDate, BigDecimal activityDistance) {
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

    public Instant getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(Instant departureDate) {
        this.departureDate = departureDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getActivityDistance() {
        return activityDistance;
    }

    public void setActivityDistance(BigDecimal activityDistance) {
        this.activityDistance = activityDistance;
    }
}
