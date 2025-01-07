package projet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import projet.enums.*;


public class CorrespondingTransports {
    private List<Transport> transports;
    private final String transportCsvPath;

    public CorrespondingTransports(String path) {
        this.transports = new ArrayList<Transport>();
        this.transportCsvPath = path;
    }

    public void getAllTransport() {
        try {
            File file = new File(this.transportCsvPath);

            ObjectMapper mapper = new ObjectMapper();
            List<Transport> transportsList = mapper.readValue(file, new TypeReference<List<Transport>>() {
            });
            this.transports = transportsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ArrayList<Transport>> findTransports(UserPreferences user, TravelRequirements travel) {
        ArrayList<ArrayList<Transport>> correspondingTransports = new ArrayList<ArrayList<Transport>>();
        String destination = travel.getFinalCity();
        String departure = travel.getDepartureCity();
        TransportType transportType = user.getFavoriteTransport();
        LocalDate departureDate = travel.getDepartureDate().atZone(ZoneId.of("Europe/Paris")).toLocalDate();

        //Transports qui vont au bon endroit le bon jour
        ArrayList<Transport> goodDestination = new ArrayList<Transport>();
        for (Transport transport : this.transports) {
            LocalDate transportDate = transport.getDestinationDateTime().atZone(ZoneId.of("Europe/Paris")).toLocalDate();
            if (transport.getDestinationCity().equals(destination) && transportDate.isEqual(departureDate)) {
                if (transport.getType() == null) {
                    goodDestination.add(transport);
                } else {
                    if (transport.getType() == transportType) {
                        goodDestination.add(transport);
                    }
                }
            }
        }

        //Ajout du transport s'il y a une escale disponible ou si ça correspond déjà
        for (Transport transport : goodDestination) {
            if (transport.getDepartureCity().equals(departure)) {
                ArrayList<Transport> fullTransport = new ArrayList<Transport>();
                fullTransport.add(transport);
                correspondingTransports.add(fullTransport);
            } else {
                String stopover = transport.getDepartureCity();
                for (Transport secondTransport : this.transports) {
                    LocalDate secondTransportDate = secondTransport.getDepartureDateTime().atZone(ZoneId.of("Europe/Paris")).toLocalDate();
                    TransportType secondTransportType = secondTransport.getType();
                    if (transportType == null || secondTransportType == transportType) {
                        //on vérifie le jour, que les heures correspondent et que les villes correspondent)
                        if (secondTransportDate.isEqual(departureDate) && secondTransport.getDestinationDateTime().isBefore(transport.getDepartureDateTime().minusSeconds(600))
                                && secondTransport.getDepartureCity().equals(departure) && secondTransport.getDestinationCity().equals(stopover)) {
                            ArrayList<Transport> fullTransport = new ArrayList<Transport>();
                            fullTransport.add(secondTransport);
                            fullTransport.add(transport);
                            correspondingTransports.add(fullTransport);
                        }
                    }
                }
            }
        }

        //Prise en compte des préférences s'il y en a
        if (!correspondingTransports.isEmpty()) {
            PrivilegedTransport preferences = user.getPrivilegedTransport();
            if (preferences == PrivilegedTransport.PRIX_MINIMUM) {
                correspondingTransports = findTransportsWithMinimumPrice(correspondingTransports);
            }

            else if (preferences == PrivilegedTransport.DUREE_MINIMUM) {
                correspondingTransports = findTransportsWithMinimumDuration(correspondingTransports);
            }
        }

        return correspondingTransports;
    }


    private ArrayList<ArrayList<Transport>> findTransportsWithMinimumPrice(ArrayList<ArrayList<Transport>> correspondingTransports) {
        BigDecimal minPrice = BigDecimal.ZERO;
        ArrayList<ArrayList<Transport>> preferredTravels = new ArrayList<ArrayList<Transport>>();
        //initialisation du prix minimal au prix du premier ensemble de transports
        for (Transport transport : correspondingTransports.get(0)) {
            minPrice = minPrice.add(transport.getPrice());
        }

        //déterminer le prix min et garder seulement les trajets avec le prix minimal
        for (ArrayList<Transport> transports : correspondingTransports) {
            BigDecimal price = BigDecimal.ZERO;
            for (Transport transport : transports) {
                price = price.add(transport.getPrice());
            }
            if (price.compareTo(minPrice) == 0) {
                preferredTravels.add(transports);
            }
            else if (price.compareTo(minPrice) < 0) {
                minPrice = price;
                preferredTravels.clear();
                preferredTravels.add(transports);
            }
        }
        return preferredTravels;
    }

    private ArrayList<ArrayList<Transport>> findTransportsWithMinimumDuration(ArrayList<ArrayList<Transport>> correspondingTransports) {
        Duration minDuration = Duration.ZERO;
        ArrayList<ArrayList<Transport>> preferredTravels = new ArrayList<ArrayList<Transport>>();
        //initialisation de la durée minimale à la durée du premier ensemble de transports
        for (Transport transport : correspondingTransports.get(0)) {
            minDuration = minDuration.plus(Duration.between(transport.getDepartureDateTime(), transport.getDestinationDateTime()));
        }

        //déterminer la durée min et garder seulement les trajets avec la durée minimale
        for (ArrayList<Transport> transports : correspondingTransports) {
            Duration duration = Duration.ZERO;
            for (Transport transport : transports) {
                duration = duration.plus(Duration.between(transport.getDepartureDateTime(), transport.getDestinationDateTime()));
            }
            if (duration.compareTo(minDuration) == 0) {
                preferredTravels.add(transports);
            }
            else if (duration.compareTo(minDuration) < 0) {
                minDuration = duration;
                preferredTravels.clear();
                preferredTravels.add(transports);
            }
        }
        return preferredTravels;
    }
}


