package es.udc.ws.app.model.driver.driverservice;


import javax.sql.DataSource;
import static es.udc.ws.app.model.util.ModelConstants.DRIVER_DATA_SOURCE;
import static es.udc.ws.app.model.util.ModelConstants.MAX_ID;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.driver.SqlDriverDao;
import es.udc.ws.app.model.driver.SqlDriverDaoFactory;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.driverservice.exceptions.InvalidDriverException;
import es.udc.ws.app.model.driverservice.exceptions.InvalidRatingException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

public class DriverServiceImpl implements DriverService{

	
	private final DataSource dataSource;
	private SqlDriverDao driverDao = null;
	private SqlTripDao tripDao = null;
	
	
	public DriverServiceImpl() {
		dataSource = DataSourceLocator.getDataSource(DRIVER_DATA_SOURCE);
		driverDao = SqlDriverDaoFactory.getDao();
		tripDao = SqlTripDaoFactory.getDao();
	}
	
	
	private void validateDriver(Driver driver) throws InputValidationException {

		PropertyValidator.validateMandatoryString("name", driver.getName());
		PropertyValidator.validateMandatoryString("city", driver.getCity());
		PropertyValidator.validateMandatoryString("car", driver.getCar());
		PropertyValidator.validateLong("startTime", driver.getStartTime(), 0,23);
		PropertyValidator.validateLong("endTime", driver.getEndTime(), 0,23);
	}



	private void validateTrip(Trip trip) throws InputValidationException {

		PropertyValidator.validateLong("driverId", trip.getDriverId(),0,MAX_ID);
		PropertyValidator.validateMandatoryString("origen", trip.getOrigen());
		PropertyValidator.validateMandatoryString("destino", trip.getDestino());
		PropertyValidator.validateMandatoryString("user", trip.getUser());
		PropertyValidator.validateMandatoryString("creditCardNumber", trip.getCreditCardNumber());
        PropertyValidator.validateLong("valoracion", trip.getValoracion(), 0,10);
	}
	
    
	@Override
	public Driver addDriver(Driver driver) throws InputValidationException {

		validateDriver(driver);
		driver.setCreationDate(Calendar.getInstance());

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				Driver createdDriver = driverDao.create(connection, driver);

				/* Commit. */
				connection.commit();

				return createdDriver;

			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}


	@Override
	public void updateDriver(Driver driver) throws InputValidationException, InstanceNotFoundException  {

		validateDriver(driver);
		

		try (Connection connection = dataSource.getConnection()) {

			try {

				/* Prepare connection. */
				connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
				connection.setAutoCommit(false);

				/* Do work. */
				driverDao.update(connection, driver);

				/* Commit. */
				connection.commit();

			} catch (InstanceNotFoundException e) {
				connection.commit();
				throw e;
			} catch (SQLException e) {
				connection.rollback();
				throw new RuntimeException(e);
			} catch (RuntimeException | Error e) {
				connection.rollback();
				throw e;
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}


	@Override
	public Driver findDriverById(Long driverId) throws InstanceNotFoundException {

	    try (Connection connection = dataSource.getConnection()) {
		    return driverDao.find(connection, driverId);
		} catch (SQLException e) {
		    throw new RuntimeException(e);
		}

	}



	@Override
	public Trip hireTrip(Long driverId, String origen, String destino, String user, String creditCardNumber)
	            throws InstanceNotFoundException, InputValidationException, InvalidDriverException {

	    PropertyValidator.validateCreditCard(creditCardNumber);
	    System.out.print("Modelo, creditCardNumber: " + creditCardNumber);

	    try (Connection connection = dataSource.getConnection()) {
	        try {
	            /* Prepare connection. */
	            connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
	            connection.setAutoCommit(false);

	            /* Do work. */  
	            Driver driver = driverDao.find(connection, driverId);
	            
	            //Obtenemenos la fecha actual y la modifcamos para decir cual es la fecha de fin de trabajo
	            Calendar expirationDriverDate = Calendar.getInstance();
	            expirationDriverDate.set(Calendar.HOUR_OF_DAY,driver.getEndTime());
	            expirationDriverDate.set(Calendar.MINUTE, 0);
	            expirationDriverDate.set(Calendar.SECOND,0);
	            
	            
	            //Obtenemos fecha actual
	            Calendar calendar = Calendar.getInstance();
	            int current_time = calendar.get(Calendar.HOUR_OF_DAY);

	            //Comprobacion de conductor activo y en hora
	            if ( ( (driver.getNumViajes()==0) || ( ((driver.getNumViajes()) > 0) &&   
                        ((driver.getPuntuacionTotal()/
                          driver.getNumViajes()) >= 5) ) )  &&
                        ( (driver.getStartTime()<=current_time) &&
                         (driver.getEndTime()>=current_time ) )  )
	            {
	                /* Inicializamos la valoracion a -1 para los viajes no puntuados */
	                //System.out.println("crear viaje");
	                Trip trip = tripDao.create(connection, new Trip(Calendar.getInstance(), -1,driverId, origen, destino, user, creditCardNumber));
	                //System.out.println("viaje creado");

	                /* Commit. */
	                connection.commit();
	                //validateTrip(trip);

	                return trip;
	            } else
	                throw new InvalidDriverException(driver.getDriverId(),expirationDriverDate);
	        } catch (InstanceNotFoundException e) {
	              connection.commit();
	              throw e;
	        } catch (SQLException e) {
	              connection.rollback();
	              throw new RuntimeException(e);
	        } catch (RuntimeException | Error e) {
	              connection.rollback();
	              throw e;
	        }
	    } catch (SQLException e) {
	              throw new RuntimeException(e);
	    }
	}


    @Override
    public List<Driver> findDriversByCity(String city) {

        try (Connection connection = dataSource.getConnection()) {
            return driverDao.findDriversByCity(connection, city);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }



    @Override
    public void scoreTrip(String user, long tripId, int score) throws InstanceNotFoundException,
    InputValidationException, InvalidRatingException {

        PropertyValidator.validateLong("valoracion", score, 0, 10);

        try (Connection connection = dataSource.getConnection()) {
        
            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                
                //Update trip
                Trip trip =  tripDao.find(connection, tripId);

                if (trip.getValoracion() != -1)
                    throw new InvalidRatingException(trip.getUser(), score);

                if (user != trip.getUser())
                    throw new InvalidRatingException(user);
 
                trip.setValoracion(score);
                tripDao.update(connection, trip);

                //Update Driver
                Driver driver = driverDao.find(connection, trip.getDriverId());
                driver.setPuntuacionTotal(driver.getPuntuacionTotal() + score);
                driver.sumarViaje();
                driverDao.update(connection, driver);

                /* Work Done */


                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    

	@Override
	public List<Trip> findTripsByUser(String user) throws InstanceNotFoundException {
		try (Connection connection = dataSource.getConnection()) {
			return tripDao.findTripsByUser(connection, user);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public List<Trip> findTripsByDriver(Long driverId) throws InstanceNotFoundException {
		try (Connection connection = dataSource.getConnection()) {
			return tripDao.findTripsByDriver(connection, driverId);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	
}