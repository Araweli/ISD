package es.udc.ws.app.client.ui;

import java.util.List;

import es.udc.ws.app.client.service.ClientUserService;
import es.udc.ws.app.client.service.ClientUserServiceFactory;
import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;

import es.udc.ws.app.client.service.exceptions.ClientInvalidDriverException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidRatingException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;



public class UserServiceClient {

    public static void main(String[] args) {

        if (args.length == 0) {
            System.err.println( "numero argumentos" + args.length);
            printUsageAndExit();
        }

        ClientUserService clientUserService = ClientUserServiceFactory.getService();
        if ("-f".equalsIgnoreCase(args[0])) {

            validateArgs(args, 2, new int[] {});

            // [find] UserServiceClient -f <city>

            try {
                List<ClientDriverDto> drivers = clientUserService.findDriversByCity(args[1]);
                for(int i=0; i< drivers.size(); i++) {
                    System.out.println(
                            "DriverId: " + drivers.get(i).getDriverId() +
                            ", Name: " + drivers.get(i).getName() +
                            ", car: " + drivers.get(i).getCar() +
                            ", city: " +drivers.get(i).getCity()+
                            ", startTime: " + drivers.get(i).getStartTime() +
                            ", endTime: " + drivers.get(i).getEndTime() +
                            ", puntuacionmedia: " + drivers.get(i).getPuntuacionMedia() + "\n");
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-a".equalsIgnoreCase(args[0])) {

            validateArgs(args, 6, new int[] {1});

            // [addTravel] UserServiceClient -a <driverId> <sourceAddress> <targetAddress> <userLogin> <creditCardNumber>
            try {
                ClientTripDto tripDto = clientUserService.addTravel(new ClientTripDto(null,Long.valueOf(args[1]),
                                                                    args[2],args[3],args[4],args[5],-1));

                System.out.println("The trip " + "(id: " + tripDto.getTripId() + ") " 
                                  + "was hired with driver " 
                                  + tripDto.getDriverId());

            } catch (NumberFormatException ex) {
                ex.printStackTrace(System.err);
            } catch (InstanceNotFoundException ex) {
                System.err.println("Driver Not Found"); ex.printStackTrace(System.err);
            } catch (InputValidationException ex) {
                System.err.println("Invalid arguments"); ex.printStackTrace(System.err);
            } catch (ClientInvalidDriverException ex) {
                System.err.println("Driver not available to hire the trip");
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-s".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {2,3});

            // [scoreTrip] UserServiceClient -s <userId> <tripId> <score>
            try {
                clientUserService.scoreTrip(args[1],Long.valueOf(args[2]).longValue(),
                                            Integer.valueOf(args[3]).intValue());

                System.out.println("The trip with id " + Long.valueOf(args[2]).longValue() +
                                    " was scored succesfully");

            } catch (NumberFormatException ex) {
                ex.printStackTrace(System.err);
            } catch (InstanceNotFoundException ex) {
                System.err.println("Trip Not Found");
                ex.printStackTrace(System.err);
            } catch (InputValidationException ex) {
                System.err.println("Invalid arguments");
                ex.printStackTrace(System.err);
            } catch (ClientInvalidRatingException ex) {
                System.err.println("Rating forbidden");
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-t".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [findTripsByUser] UserServiceClient -t <userId>
            try {
                List <ClientTripDto> trips = clientUserService.findTripsByUser(args[1]);
                for(int i=0; i< trips.size(); i++) {
                    System.out.println(
                        "tripId: " + trips.get(i).getTripId() +
                        ", driverId: " + trips.get(i).getDriverId() +
                        ", sourceAddress: " + trips.get(i).getOrigen() +
                        ", targetAddress: " +trips.get(i).getDestino() +
                        ", user: " + trips.get(i).getUser() +
                        ", creditCardNumber: " + trips.get(i).getCreditCardNumber() +
                        ", rating: " + trips.get(i).getValoracion() + "\n");
                }

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
            
        } else if ("-d".equalsIgnoreCase(args[0])) {
        validateArgs(args, 2, new int[] {});

            // [hiredDrivers] UserServiceClient -d  <user>
            try {
                List <ClientDriverDto> drivers = clientUserService.hiredDrivers(args[1]);
                for(int i=0; i< drivers.size(); i++) {
                    System.out.println(
                        "DriverId: " + drivers.get(i).getDriverId() +
                        ", Name: " + drivers.get(i).getName() +
                        ", car: " + drivers.get(i).getCar() +
                        ", city: " +drivers.get(i).getCity()+
                        ", startTime: " + drivers.get(i).getStartTime() +
                        ", endTime: " + drivers.get(i).getEndTime() +
                        ", puntuacionmedia: " + drivers.get(i).getPuntuacionMedia() + "\n");
                }

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
        }

    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" + 
                "[findDriversByCity]    UserServiceClient -f <city>\n" +
                "[addTravel]            UserServiceClient -a <driverId> <sourceAddress> <targetAddress> " +
                                                            "<userLogin> <creditCardNumber>\n" + 
                "[scoreTrip]            UserServiceClient -s <userId> <tripId> <score>\n"    + 
                "[findTripsByUser]      UserServiceClient -t <userId>\n" + 
                "[hiredDrivers]         UserServiceClient -d <driverId> <userId>\n");
    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if (expectedArgs != args.length) {
            printUsageAndExit();
        }
        for (int i = 0; i < numericArguments.length; i++) {
            int position = numericArguments[i];
            try {
                Integer.parseInt(args[position]);
            } catch (NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

}