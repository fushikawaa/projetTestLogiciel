package projet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import projet.enums.ActivityType;
import projet.enums.PrivilegedHotel;
import projet.enums.PrivilegedTransport;
import projet.enums.TransportType;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez votre ville de départ (parmis Paris, Bordeaux, Marseille, Lyon, Nice, Rennes, Strasbourg, Lille et Toulouse) : ");
        String departureCity = scanner.nextLine();

        System.out.print("Entrez votre ville de voyage (parmis Paris, Bordeaux, Marseille, Lyon, Nice, Rennes, Strasbourg, Lille et Toulouse) : ");
        String travelCity = scanner.nextLine();

        System.out.print("Entrez votre ville de retour (parmis Paris, Bordeaux, Marseille, Lyon, Nice, Rennes, Strasbourg, Lille et Toulouse) : ");
        String finalCity = scanner.nextLine();

        System.out.print("Entrez votre date de départ au format 2025-MM-DD : ");
        String departureDateString = scanner.nextLine();
        LocalDate departureDate = LocalDate.parse(departureDateString, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime departureDateTime = departureDate.atStartOfDay();

        System.out.print("Entrez votre date de retour au format 2025-MM-DD : ");
        String endDateString = scanner.nextLine();
        LocalDate endDate = LocalDate.parse(endDateString, DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDateTime endDateTime = endDate.atStartOfDay();

        System.out.print("Entrez votre distance maximum en km entre l'hôtel et les activités : ");
        double activityDistanceDouble = scanner.nextDouble();
        BigDecimal activityDistance = new BigDecimal(activityDistanceDouble);

        System.out.print("Entrez votre budget total pour ce voyage : ");
        double budgetDouble = scanner.nextDouble();
        BigDecimal budget = new BigDecimal(budgetDouble);

        System.out.println("Entrez le chiffre qui correspond au type de transport que vous souhaitez : ");
        System.out.println("1 - Train");
        System.out.println("2 - Avion");
        System.out.print("Votre choix : ");
        int transportTypeInt = scanner.nextInt();
        TransportType transportType;
        switch(transportTypeInt){
            case (1) -> transportType = TransportType.TRAIN;
            case (2) -> transportType = TransportType.AVION;
            default -> {
                System.out.println("Choix invalide. Par défaut, le transport sera le train.");
                transportType = TransportType.TRAIN;
            }
        }

        System.out.println("Entrez le chiffre qui correspond à votre critère prioritaire lors du choix du transport : ");
        System.out.println("1 - Durée de trajet minimale");
        System.out.println("2 - Prix du trajet minimal");
        System.out.print("Votre choix : ");
        int privilegedTransportInt = scanner.nextInt();
        PrivilegedTransport privilegedTransport;
        switch(privilegedTransportInt){
            case (1) -> privilegedTransport = PrivilegedTransport.DUREE_MINIMUM;
            case (2) -> privilegedTransport = PrivilegedTransport.PRIX_MINIMUM;
            default -> {
                System.out.println("Choix invalide. Par défaut, le critère de choix du transport sera le prix minimal.");
                privilegedTransport = PrivilegedTransport.PRIX_MINIMUM;
            }
        }

        System.out.print("Entrez le nombre d'étoiles minimal que vous souhaitez pour un hotel entre 1 et 5 : ");
        int minNumberStars = scanner.nextInt();
        List<Integer> numberList = Arrays.asList(1, 2, 3, 4, 5);
        if(!numberList.contains(minNumberStars)){
            minNumberStars = 1;
            System.out.println("Choix invalide. Par défaut, le nombre d'étoiles minimal sera 1.");
        }

        System.out.println("Entrez le chiffre qui correspond à votre critère prioritaire lors du choix de l'hôtel : ");
        System.out.println("1 - Nombre d'étoiles maximal");
        System.out.println("2 - Prix minimal");
        System.out.print("Votre choix : ");
        int privilegedHotelInt = scanner.nextInt();
        PrivilegedHotel privilegedHotel;
        switch(privilegedHotelInt){
            case (1) -> privilegedHotel = PrivilegedHotel.NOMBRE_ETOILES;
            case (2) -> privilegedHotel = PrivilegedHotel.PRIX_MINIMUM;
            default -> {
                System.out.println("Choix invalide. Par défaut, le critère de choix de l'hôtel sera le prix minimal.");
                privilegedHotel = PrivilegedHotel.PRIX_MINIMUM;
            }
        }

        System.out.println("Entrez le chiffre qui correspond à la première catégorie d'activités que vous souhaitez : ");
        System.out.println("1 - Sport");
        System.out.println("2 - Culture");
        System.out.println("3 - Loisir");
        System.out.println("4 - Gastronomie");
        System.out.print("Votre choix : ");
        int firstActivityTypeInt = scanner.nextInt();
        ActivityType firstActivityType;
        switch(firstActivityTypeInt){
            case (1) -> firstActivityType = ActivityType.SPORT;
            case (2) -> firstActivityType = ActivityType.CULTURE;
            case (3) -> firstActivityType = ActivityType.LOISIR;
            case (4) -> firstActivityType = ActivityType.GASTRONOMIE;
            default -> {
                System.out.println("Choix invalide. Par défaut, le premier type d'activité sera le sport.");
                firstActivityType = ActivityType.SPORT;
            }
        }

        System.out.println("Entrez le chiffre qui correspond à la deuxième catégorie d'activités que vous souhaitez : ");
        System.out.println("1 - Sport");
        System.out.println("2 - Culture");
        System.out.println("3 - Loisir");
        System.out.println("4 - Gastronomie");
        System.out.print("Votre choix : ");
        int secondActivityTypeInt = scanner.nextInt();
        ActivityType secondActivityType;
        switch(secondActivityTypeInt){
            case (1) -> secondActivityType = ActivityType.SPORT;
            case (2) -> secondActivityType = ActivityType.CULTURE;
            case (3) -> secondActivityType = ActivityType.LOISIR;
            case (4) -> secondActivityType = ActivityType.GASTRONOMIE;
            default -> {
                System.out.println("Choix invalide. Par défaut, le deuxième type d'activité sera la culture.");
                secondActivityType = ActivityType.CULTURE;
            }
        }

        UserPreferences userPreferences = new UserPreferences(transportType, privilegedTransport, minNumberStars, privilegedHotel, firstActivityType, secondActivityType);
        TravelRequirements travelRequirements = new TravelRequirements(departureCity, travelCity, finalCity, departureDateTime, endDateTime, activityDistance, budget);
        FileManager fileManager = new FileManager();
        CorrespondingTransports correspondingTransports = new CorrespondingTransports("src/data/transports_database.json", fileManager);
        CorrespondingHotels correspondingHotels = new CorrespondingHotels("src/data/hotels_database.json", fileManager);
        CorrespondingActivities correspondingActivities = new CorrespondingActivities("src/data/activities_database.json", fileManager, new CoordinatesManager());
        CompleteTravel completeTravel = new CompleteTravel(correspondingTransports, correspondingHotels, correspondingActivities, userPreferences, travelRequirements, fileManager);
        
        List<TravelErrors> travels = completeTravel.createTravels();
        try {
            fileManager.writeTravelsToFile(new File("src/result/travel.json"), "src/result/travel.json", travels);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture des données dans le fichier 'travel.json': " + e.getMessage());
        }

        System.out.println("Voici vos propositions de voyage : ");
        System.out.println("Transports aller : ");
        ArrayList<ArrayList<Transport>> transports = travels.get(0).getTravel().getGoTrip();
        if(transports.isEmpty()){
            System.out.println("    Aucun transport aller disponible pour ces villes à cette date et dans votre budget.");
        }
        else{
            int count = 1;
            for(ArrayList<Transport> propositions : transports){
                System.out.println("    Proposition " + count + " : ");
                for(Transport transport : propositions){
                    System.out.println("        " + transport.getType() + " part de " + transport.getDepartureCity() + " à " + transport.getDepartureDateTime().getHour() +"h" + transport.getDepartureDateTime().getMinute() + " et arrive à " + transport.getDestinationCity() + " à " + transport.getDestinationDateTime().getHour() + "h" + transport.getDestinationDateTime().getMinute() + ", prix : " + transport.getPrice() + "€");
                }
            }
        }

        System.out.println("Transports retour : ");
        ArrayList<ArrayList<Transport>> returnTransports = travels.get(0).getTravel().getReturnTrip();
        if(returnTransports.isEmpty()){
            System.out.println("    Aucun transport retour disponible pour ces villes à cette date et dans votre budget.");
        }
        else{
            int count = 1;
            for(ArrayList<Transport> propositions : returnTransports){
                System.out.println("    Proposition " + count + " : ");
                for(Transport transport : propositions){
                    System.out.println("        " + transport.getType() + " part de " + transport.getDepartureCity() + " à " + transport.getDepartureDateTime().getHour() +"h" + transport.getDepartureDateTime().getMinute() + " et arrive à " + transport.getDestinationCity() + " à " + transport.getDestinationDateTime().getHour() + "h" + transport.getDestinationDateTime().getMinute() + ", prix : " + transport.getPrice() + "€");
                }
                ++count;
            }
        }

        System.out.println("Hotels et Activités : ");
        int count = 1;
        for(TravelErrors travel : travels){
            System.out.println("    Proposition " + count + " : ");
            Hotel hotel = travel.getTravel().getHotel();
            if(hotel == null){
                System.out.println("        Aucun hotel disponible dans cette ville dans votre budget.");
            }
            else{
                System.out.println("        Hotel : " + hotel.getName() + " au " + hotel.getAddress() +  ", " + hotel.getStars() + " étoiles, prix par nuit : " + hotel.getPricePerNight() + "€");
                List<Activity> activities = travel.getTravel().getActivities();
                if(!activities.isEmpty()){
                    for(Activity activity : travel.getTravel().getActivities()){
                        System.out.print("        Activité du " + activity.getDate() + " : ");
                        System.out.println(activity.getCategory() + " ; " + activity.getName() + " au " + activity.getAddress() + ", prix : " + activity.getPrice() + "€");
                    }
                }
                else{
                    System.out.println("        Aucune activité disponible dans cette ville dans votre budget.");
                }
            }
            System.out.println("    Prix total du voyage : " + travel.getTravel().getTotalPrice() + "€");
            System.out.println();
            ++count;
        }

        scanner.close(); 
    }
}
    

