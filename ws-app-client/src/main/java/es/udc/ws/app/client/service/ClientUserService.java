package es.udc.ws.app.client.service;

import java.util.List;
import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.exceptions.ClientInvalidRatingException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidDriverException;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;


public interface ClientUserService {

    public List <ClientDriverDto> findDriversByCity(String city);

    public ClientTripDto addTravel(ClientTripDto trip) throws InstanceNotFoundException,
        InputValidationException, ClientInvalidDriverException;

    public void scoreTrip(String userId, long tripId, int score) throws InstanceNotFoundException,
    InputValidationException, ClientInvalidRatingException;

    public List<ClientTripDto> findTripsByUser(String user) ;

    public List <ClientDriverDto> hiredDrivers(String user) throws InstanceNotFoundException;
}