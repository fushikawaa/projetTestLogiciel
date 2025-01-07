package projet;

// Definition of the TravelRequirements class
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TravelRequirements {

    private String departureCity;
    private String travelCity;
    private String finalCity;
    private LocalDateTime departureDate;
    private LocalDateTime endDate;
    private BigDecimal activityDistance;
    private BigDecimal budget;

    // Constructor
    public TravelRequirements(String departureCity, String travelCity, String finalCity, LocalDateTime departureDate, LocalDateTime endDate, BigDecimal activityDistance, BigDecimal budget) {
        this.departureCity = departureCity;
        this.travelCity = travelCity;
        this.finalCity = finalCity;
        this.departureDate = departureDate;
        this.endDate = endDate;
        this.activityDistance = activityDistance;
        this.budget = budget;
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

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getActivityDistance() {
        return activityDistance;
    }

    public void setActivityDistance(BigDecimal activityDistance) {
        this.activityDistance = activityDistance;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}
