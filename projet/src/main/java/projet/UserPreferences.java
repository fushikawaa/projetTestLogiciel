package projet;

import com.fasterxml.jackson.annotation.JsonProperty;

import projet.enums.ActivityType;
import projet.enums.PrivilegedHotel;
import projet.enums.PrivilegedTransport;
import projet.enums.TransportType;

// Classe UserPreferences
public class UserPreferences {
    private TransportType favoriteTransport;
    private PrivilegedTransport privilegedTransport;
    private int minNumberStars; // Entre 1 et 5
    private PrivilegedHotel privilegedHotel;
    private ActivityType preferedActivity;
    private ActivityType secondActivity; // Peut être null

    // Constructeur
    public UserPreferences(
        @JsonProperty("favoriteTransport") TransportType favoriteTransport, 
        @JsonProperty("privilegedTransport") PrivilegedTransport privilegedTransport, 
        @JsonProperty("minNumberStars") int minNumberStars, 
        @JsonProperty("privilegedHotel") PrivilegedHotel privilegedHotel, 
        @JsonProperty("preferedActivity") ActivityType preferedActivity, 
        @JsonProperty("secondActivity") ActivityType secondActivity
    ) {
        this.favoriteTransport = favoriteTransport;
        this.privilegedTransport = privilegedTransport;
        setMinNumberStars(minNumberStars);
        this.privilegedHotel = privilegedHotel;
        this.preferedActivity = preferedActivity;
        this.secondActivity = secondActivity;
    }

    // Getters et Setters
    public TransportType getFavoriteTransport() {
        return favoriteTransport;
    }

    public void setFavoriteTransport(TransportType TransportType) {
        this.favoriteTransport = TransportType;
    }

    public PrivilegedTransport getPrivilegedTransport() {
        return privilegedTransport;
    }

    public void setPrivilegedTransport(PrivilegedTransport privilegedTransport) {
        this.privilegedTransport = privilegedTransport;
    }

    public int getMinNumberStars() {
        return minNumberStars;
    }

    public void setMinNumberStars(int minNumberStars) {
        if (minNumberStars < 1 || minNumberStars > 5) {
            throw new IllegalArgumentException("minNumberStars doit être entre 1 et 5.");
        }
        this.minNumberStars = minNumberStars;
    }

    public PrivilegedHotel getPrivilegedHotel() {
        return privilegedHotel;
    }

    public void setPrivilegedHotel(PrivilegedHotel privilegedHotel) {
        this.privilegedHotel = privilegedHotel;
    }

    public ActivityType getPreferedActivity() {
        return preferedActivity;
    }

    public void setPreferedActivity(ActivityType preferedActivity) {
        this.preferedActivity = preferedActivity;
    }

    public ActivityType getSecondActivity() {
        return secondActivity;
    }

    public void setSecondActivity(ActivityType secondActivity) {
        this.secondActivity = secondActivity;
    }
}
