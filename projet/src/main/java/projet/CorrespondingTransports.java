package projet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
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

    public ArrayList<ArrayList<Transport>> findTransports(UserPreferences user, String departure, String destination, LocalDateTime departureDate, BigDecimal budget) {
        ArrayList<ArrayList<Transport>> correspondingTransports = new ArrayList<ArrayList<Transport>>();
        TransportType transportType = user.getFavoriteTransport();

        //Transports qui vont au bon endroit le bon jour
        ArrayList<Transport> goodDestination = new ArrayList<Transport>();
        for (Transport transport : this.transports) {
            LocalDateTime transportDate = transport.getDestinationDateTime();
            if (transport.getDestinationCity().equals(destination) && transportDate.toLocalDate().equals(departureDate.toLocalDate()) && transport.getPrice().compareTo(budget) < 0) {
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
                TransportType firstTransportType = transport.getType();
                String stopover = transport.getDepartureCity();
                for (Transport secondTransport : this.transports) {
                    LocalDateTime secondTransportDate = secondTransport.getDepartureDateTime();
                    TransportType secondTransportType = secondTransport.getType();
                    //vérification de l'homogénéité des transports
                    if (secondTransportType == firstTransportType) {
                        //on vérifie le jour, que les heures correspondent et que les villes correspondent)
                        if (secondTransportDate.toLocalDate().equals(departureDate.toLocalDate()) && secondTransport.getDestinationDateTime().isBefore(transport.getDepartureDateTime().minusMinutes(10))
                                && secondTransport.getDepartureCity().equals(departure) && secondTransport.getDestinationCity().equals(stopover)) {
                            //on vérifie que le budget ne dépasse pas
                            if (secondTransport.getPrice().add(transport.getPrice()).compareTo(budget) < 0) {
                                ArrayList<Transport> fullTransport = new ArrayList<Transport>();
                                fullTransport.add(secondTransport);
                                fullTransport.add(transport);
                                correspondingTransports.add(fullTransport);
                            }
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
                //si plusieurs choix encore possibles, utiliser le second critère
                if (correspondingTransports.size() > 1){
                    correspondingTransports = findTransportsWithMinimumDuration(correspondingTransports);
                }
            }

            else if (preferences == PrivilegedTransport.DUREE_MINIMUM) {
                correspondingTransports = findTransportsWithMinimumDuration(correspondingTransports);
                if (correspondingTransports.size() > 1){
                    correspondingTransports = findTransportsWithMinimumPrice(correspondingTransports);
                }
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

    public void setTransports(List<Transport> transports){
        this.transports = transports;
    }

    public List<Transport> getTransports(){
        return this.transports;
    }
}


