package projet;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import projet.enums.TransportType;

public class Transport {
    private String departureCity;
    private String destinationCity;
    private LocalDateTime departureDateTime;
    private LocalDateTime destinationDateTime;
    private BigDecimal price;
    private TransportType type;


    public Transport(String departureCity, String destinationCity, LocalDateTime departureDateTime, LocalDateTime destinationDateTime,BigDecimal price, TransportType type ){
        setDepartureCity(departureCity);
        setDestinationCity(destinationCity);
        setDepartureDateTime(departureDateTime);
        setDestinationDateTime(destinationDateTime);
        setPrice(price);
        setType(type);
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
