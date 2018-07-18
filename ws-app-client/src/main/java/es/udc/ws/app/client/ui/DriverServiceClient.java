package es.udc.ws.app.client.ui;

import java.util.List;

import es.udc.ws.app.client.service.ClientDriverService;
import es.udc.ws.app.client.service.ClientDriverServiceFactory;
import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class DriverServiceClient {

	public static void main(String[] args) {

		if (args.length == 0) {
			System.err.println(	"numero argumentos" + args.length);
			printUsageAndExit();
		}

		ClientDriverService clientDriverService = ClientDriverServiceFactory.getService();
		if ("-a".equalsIgnoreCase(args[0])) {
			
			validateArgs(args, 6, new int[] { 4, 5 });

			// [add]    DriverServiceClient -a <name> <city> <car> <startDate> <endDate>

			try {
				Long driverId = clientDriverService.addDriver(new ClientDriverDto(null, args[1], args[2],
						args[3], Integer.valueOf(args[4]), Integer.valueOf(args[5])));

				System.out.println("Driver " + driverId + " created sucessfully");

			} catch (NumberFormatException | InputValidationException ex) {
				ex.printStackTrace(System.err);
			} catch (Exception ex) {
				ex.printStackTrace(System.err);
			}
			
		} else if ("-u".equalsIgnoreCase(args[0])) {

			validateArgs(args, 7, new int[] {5, 6});

			// [update] DriverServiceClient -u <driverId> <name> <city> <car> <startDate> <endDate>

			try {
			    clientDriverService.updateDriver(new ClientDriverDto(
	                        Long.valueOf(args[1]),
	                        args[2], (args[3]), (args[4]),
	                        Integer.valueOf(args[5]), Integer.valueOf(args[6])));

			    System.out.println("Driver " + args[1] + " updated sucessfully");

			} catch (NumberFormatException | InputValidationException |
			        InstanceNotFoundException ex) {
			    ex.printStackTrace(System.err);
			} catch (Exception ex) {
			    ex.printStackTrace(System.err);
			}
			
		} else if ("-f".equalsIgnoreCase(args[0])) {

			validateArgs(args, 2, new int[] {1});

            // [findDriverById] DriverServiceClient -f <driverId>

            try {
                ClientDriverDto driver = clientDriverService.findDriverById(Long.valueOf(args[1]));
                System.out.println("Found driver with id '" + args[1] + "'");
              
                    
                    System.out.println(
                            "Name: " + driver.getName() +
                            ", car: " + driver.getCar() +
                            ", city: " + driver.getCity()+
                            ", startTime: " + driver.getStartTime() +
                            ", endTime: " + driver.getEndTime() +
                            ", puntuacionmedia: " + driver.getPuntuacionMedia());

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }
		} else if ("-t".equalsIgnoreCase(args[0])) {
			 
			// [findTripsByDriver] DriverServiceClient -t <driverId>
		
			try {
                List <ClientTripDto> trips = clientDriverService.findTripsByDriver(Long.valueOf(args[1]));
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
		}
	}

	public static void printUsageAndExit() {
		printUsage();
		System.exit(-1);
	}

    public static void printUsage() {
    System.err.println(
            "Usage:\n" + "               [add] DriverServiceClient -a <name> <city> <car> <startDate> <endDate>\n"
                       + "            [update] DriverServiceClient -u <driverId> <name> <city> <car> <startDate> <endDate>\n"
                       + "    [findDriverById] DriverServiceClient -f <driverId>\n"
                       + " [findTripsByDriver] DriverServiceClient -t <driverId>\n"
            );
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
