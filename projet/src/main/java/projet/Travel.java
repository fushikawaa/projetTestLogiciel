package projet;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Travel {
    private ArrayList<ArrayList<Transport>> goTrip;
    private Hotel hotel;
    private List<Activity> activities;
    private ArrayList<ArrayList<Transport>> returnTrip;
    private BigDecimal totalPrice;

    public Travel(
        @JsonProperty("goTrip") ArrayList<ArrayList<Transport>> goTrip, 
        @JsonProperty("hotel") Hotel hotel, 
        @JsonProperty("activities") List<Activity> activities, 
        @JsonProperty("returnTrip") ArrayList<ArrayList<Transport>> returnTrip,
        @JsonProperty("totalPrice") BigDecimal totalPrice
    ) {
        this.goTrip = goTrip;
        this.hotel = hotel;
        this.activities = activities;
        this.returnTrip = returnTrip;
        this.totalPrice = totalPrice;
    }

    public ArrayList<ArrayList<Transport>> getGoTrip() {
        return goTrip;
    }

    public void setGoTrip(ArrayList<ArrayList<Transport>> goTrip) {
        this.goTrip = goTrip;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public ArrayList<ArrayList<Transport>> getReturnTrip() {
        return returnTrip;
    }

    public void setReturnTrip(ArrayList<ArrayList<Transport>> returnTrip) {
        this.returnTrip = returnTrip;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

}
