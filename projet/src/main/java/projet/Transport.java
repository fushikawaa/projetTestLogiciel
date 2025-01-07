package projet;

import java.math.BigDecimal;
import java.time.Instant;

import projet.enums.TransportType;

public class Transport {
    private String departureCity;
    private String destinationCity;
    private Instant departureDateTime;
    private Instant destinationDateTime;
    private BigDecimal price;
    private TransportType type;


    public Transport(String departureCity, String destinationCity, Instant departureDateTime, Instant destinationDateTime,BigDecimal price, TransportType type ){
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
    
    public Instant getDepartureDateTime() {
        return departureDateTime;
    }
    
    public void setDepartureDateTime(Instant departureDateTime) {
        this.departureDateTime = departureDateTime;
    }
    
    public Instant getDestinationDateTime() {
        return destinationDateTime;
    }
    
    public void setDestinationDateTime(Instant destinationDateTime) {
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
