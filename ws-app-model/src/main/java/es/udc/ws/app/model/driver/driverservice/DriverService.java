package es.udc.ws.app.model.driver.driverservice;

import java.util.List;

import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.driverservice.exceptions.InvalidDriverException;
import es.udc.ws.app.model.driverservice.exceptions.InvalidRatingException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface DriverService {

	public Driver addDriver(Driver d) throws InputValidationException;

	public void updateDriver(Driver d) throws InputValidationException, InstanceNotFoundException;

    public Driver findDriverById(Long driverId) throws InstanceNotFoundException;

	public List<Driver> findDriversByCity(String city);

    public Trip hireTrip(Long driverId, String origen, String destino, String user, String creditCardNumber)
            throws InstanceNotFoundException, InputValidationException,
                    InvalidDriverException;
    
	public List<Trip> findTripsByUser(String user) throws InstanceNotFoundException;
	
	public List<Trip> findTripsByDriver(Long driverId) throws InstanceNotFoundException;

    public void scoreTrip(String user, long tripId, int score) throws InstanceNotFoundException,
    InputValidationException, InvalidRatingException;
}
