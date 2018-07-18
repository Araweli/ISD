package es.udc.ws.app.serviceutil;

import java.util.ArrayList;
import java.util.List;

import es.udc.ws.app.dto.ServiceTripDto;
import es.udc.ws.app.model.trip.Trip;

public class TripToTripDtoConversor {


    public static List<ServiceTripDto> toTripDtos(List<Trip> trips) {
        List<ServiceTripDto> tripDtos = new ArrayList<>(trips.size());
            for (int i = 0; i < trips.size(); i++) {
                Trip trip = trips.get(i);
				tripDtos.add(toTripDto(trip));
			}
			return tripDtos;
    }
		
    public static ServiceTripDto toTripDto(Trip trip) {
        return new ServiceTripDto(trip.getTripId(), trip.getDriverId(), trip.getOrigen(), trip.getDestino(),
                                  trip.getUser(), trip.getCreditCardNumber(), trip.getValoracion());
    }

    public static Trip toTrip(ServiceTripDto trip) {
        return new Trip(trip.getDriverId(), trip.getOrigen(),
                        trip.getDestino(), trip.getUser(), trip.getCreditCardNumber());
    }

}