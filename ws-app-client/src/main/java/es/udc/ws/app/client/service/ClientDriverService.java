package es.udc.ws.app.client.service;


import java.util.List;

import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface ClientDriverService {

    public Long addDriver(ClientDriverDto driver)
            throws InputValidationException;
    
    public void updateDriver(ClientDriverDto driver)
            throws InputValidationException, InstanceNotFoundException;

	public ClientDriverDto findDriverById(Long driverId) throws InstanceNotFoundException;
	
	public List<ClientTripDto> findTripsByDriver(Long driverId)throws InputValidationException, InstanceNotFoundException ;
}
