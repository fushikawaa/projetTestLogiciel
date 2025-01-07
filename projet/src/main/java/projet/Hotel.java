package projet;

import java.math.BigDecimal;

public class Hotel {
    private String name;
    private String address;
    private String city;
    private int stars;
    private BigDecimal pricePerNight;

    public Hotel(String name, String address, String city, int stars, BigDecimal pricePerNight) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.stars = stars;
        this.pricePerNight = pricePerNight;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    public BigDecimal getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(BigDecimal pricePerNight) {
        this.pricePerNight = pricePerNight;
    }
}
