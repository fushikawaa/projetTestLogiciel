package projet;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import projet.enums.TransportType;

public class Transport {
    private String departureCity;
    private String destinationCity;
    private LocalDateTime departureDateTime;
    private LocalDateTime destinationDateTime;
    private BigDecimal price;
    private TransportType type;


    public Transport(
        @JsonProperty("departureCity") String departureCity, 
        @JsonProperty("destinationCity") String destinationCity, 
        @JsonProperty("departureDateHour") LocalDateTime departureDateTime, 
        @JsonProperty("destinationDateHour") LocalDateTime destinationDateTime, 
        @JsonProperty("price") BigDecimal price, 
        @JsonProperty("type") TransportType type
    ) {
        this.departureCity = departureCity;
        this.destinationCity = destinationCity;
        this.departureDateTime = departureDateTime;
        this.destinationDateTime = destinationDateTime;
        this.price = price;
        this.type = type;
    }

    public String getDepartureCity() {
        return departureCity;
    }
    
    public void setDepartureCity(String departureCity) {
        this.departureCity = departureCity;
    }
    
    public String getDestinationCity() {
        return destinationCity;
    }
    
    public void setDestinationCity(String destinationCity) {
        this.destinationCity = destinationCity;
    }
    
    public LocalDateTime getDepartureDateTime() {
        return departureDateTime;
    }
    
    public void setDepartureDateTime(LocalDateTime departureDateTime) {
        this.departureDateTime = departureDateTime;
    }
    
    public LocalDateTime getDestinationDateTime() {
        return destinationDateTime;
    }
    
    public void setDestinationDateTime(LocalDateTime destinationDateTime) {
        this.destinationDateTime = destinationDateTime;
    }
    
    public BigDecimal getPrice() {
        return price;
    }
    
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    
    public TransportType getType() {
        return type;
    }
    
    public void setType(TransportType type) {
        this.type = type;
    }
}
