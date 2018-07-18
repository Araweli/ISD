package es.udc.ws.app.test.model.driverservice;

import static es.udc.ws.app.model.util.ModelConstants.DRIVER_DATA_SOURCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.Calendar;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;


import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.driver.driverservice.DriverService;
import es.udc.ws.app.model.driver.driverservice.DriverServiceFactory;
import es.udc.ws.app.model.driverservice.exceptions.InvalidDriverException;
import es.udc.ws.app.model.driverservice.exceptions.InvalidRatingException;
import es.udc.ws.app.model.driver.SqlDriverDao;
import es.udc.ws.app.model.driver.SqlDriverDaoFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.model.trip.SqlTripDao;
import es.udc.ws.app.model.trip.SqlTripDaoFactory;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;

public class DriverServiceTest {

	private final long NON_EXISTENT_DRIVER_ID = -1;
    private final long NON_EXISTENT_TRIP_ID = -1;

    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private final String INVALID_CREDIT_CARD_NUMBER = "";

    private final int CURRENT_HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    private final String  PHISHER = "annoying_user";

    private static final String USUARIO = "usuario";

	private static DriverService driverService = null;
    private static SqlDriverDao driverDao = null;	
	private static SqlTripDao tripDao = null;

	@BeforeClass
	public static void init() {

		DataSource dataSource = new SimpleDataSource();

		/* Add "dataSource" to "DataSourceLocator". */
		DataSourceLocator.addDataSource(DRIVER_DATA_SOURCE, dataSource);

		driverService = DriverServiceFactory.getService();
		driverDao = SqlDriverDaoFactory.getDao();
		tripDao = SqlTripDaoFactory.getDao();

	}

    private Driver getValidDriver(String name, String city, int starTime, int endTime) {
        return new Driver(name, city, "Todoterreno", starTime, endTime);
    }

	private Driver getValidDriver() {
		return getValidDriver("Pepe", "Barcelona", CURRENT_HOUR-1, CURRENT_HOUR+1);
	}

	private Driver createDriver(Driver driver) {

		Driver addedDriver = null;
		try {
			addedDriver = driverService.addDriver(driver);
		} catch (InputValidationException e) {
			throw new RuntimeException(e);
		}
		return addedDriver;

	}


    private void removeDriver(Long driverId) {
    
        DataSource dataSource = DataSourceLocator
                .getDataSource(DRIVER_DATA_SOURCE);
    
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                driverDao.remove(connection, driverId);
            
                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException|Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


	private void removeTrip(Long tripId) {
    
        DataSource dataSource = DataSourceLocator
                .getDataSource(DRIVER_DATA_SOURCE);
    
        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                tripDao.remove(connection, tripId);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException|Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
	
	
	private Trip findTripById(Long tripId) throws InstanceNotFoundException {

	    DataSource dataSource = DataSourceLocator.getDataSource(DRIVER_DATA_SOURCE);	
        try (Connection connection = dataSource.getConnection()) {
            return tripDao.find(connection, tripId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
	}
	

	@Test
	public void testAddDriverAndFindDriverById()
	throws InputValidationException, InstanceNotFoundException {

		Driver driver = getValidDriver();
		Driver addedDriver = null;

		addedDriver = driverService.addDriver(driver);
		Driver foundDriver = driverService.findDriverById(addedDriver.getDriverId());

		assertEquals(addedDriver, foundDriver);

		// Clear Database
		removeDriver(addedDriver.getDriverId());

	}

	@Test
	public void testAddInvalidDriver() {

		Driver driver = getValidDriver();
		Driver addedDriver = null;
		boolean exceptionCatched = false;

		try {
			// Check driver name not null
			driver.setName(null);
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver name not empty
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setName("");
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver city not null
            exceptionCatched = false;
            driver = getValidDriver();			
			driver.setCity(null);
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver city not empty
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setCity("");
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver car not null
            exceptionCatched = false;
            driver = getValidDriver();
			driver.setCar(null);
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver car not empty
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setCar("");
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver startTime >= 0
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setStartTime((short) -1);
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver startTime <= 23
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setStartTime((short) (23 + 1));
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver endTime >= 0
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setEndTime((short) -1);
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);

			// Check driver endTime <= 23
			exceptionCatched = false;
			driver = getValidDriver();
			driver.setEndTime((short) (23 + 1));
			try {
				addedDriver = driverService.addDriver(driver);
			} catch (InputValidationException e) {
				exceptionCatched = true;
			}
			assertTrue(exceptionCatched);
			
            // Check driver endTime <= 23
            exceptionCatched = false;
            driver = getValidDriver();
            driver.setEndTime((short) (23 + 1));
            try {
                addedDriver = driverService.addDriver(driver);
            } catch (InputValidationException e) {
                exceptionCatched = true;
            }
            assertTrue(exceptionCatched);			

		} finally {
			if (!exceptionCatched) {
				// Clear Database
				removeDriver(addedDriver.getDriverId());
			}
		}

	}


    @Test(expected = InstanceNotFoundException.class)
    public void testFindNonExistentDriver() throws InstanceNotFoundException {
        driverService.findDriverById(NON_EXISTENT_DRIVER_ID);
    }


	@Test
	public void testUpdateDriver() throws InputValidationException, InstanceNotFoundException {

		Driver driver = createDriver(getValidDriver());
		try {
			
			driver.setCity("Santiago");
			driver.setCar("Deportivo");
			driver.setStartTime(10);
			driver.setStartTime(20);

			driverService.updateDriver(driver);

			Driver updatedDriver = driverService.findDriverById(driver.getDriverId());
			assertEquals(driver, updatedDriver);

		} finally {
			// Clear Database
			removeDriver(driver.getDriverId());
		}

	}


    @Test(expected = InputValidationException.class)
    public void testUpdateInvalidDriver() throws InputValidationException,
            InstanceNotFoundException {

        Driver driver = createDriver(getValidDriver());
        try {
            // Check driver name not null
            driver = driverService.findDriverById(driver.getDriverId());
            driver.setName(null);
            driverService.updateDriver(driver);
        } finally {
            // Clear Database
            removeDriver(driver.getDriverId());
        }

    }


    @Test(expected = InstanceNotFoundException.class)
    public void testUpdateNonExistentDriver() throws InputValidationException,
            InstanceNotFoundException {

        Driver driver = getValidDriver();
        driver.setDriverId(NON_EXISTENT_DRIVER_ID);
        driver.setCreationDate(Calendar.getInstance());
        driverService.updateDriver(driver);

    }
    

    @Test
    public void testfindDriversByCityNotYetScored() {

        // The following two drivers are in the situation of a driver 
        // that has been added to the database and has not yet been scored. 
        // Therefore, they are initially active.

        // Sara is active but out of time, she should not be included in the result. 
        Driver driver1 = createDriver(getValidDriver("Sara","Valencia",(CURRENT_HOUR)-2,(CURRENT_HOUR)-1));

        // Carmelo is active and on time, he should be included in the result 
        Driver driver2 = createDriver(getValidDriver("Carmelo","Valencia",(CURRENT_HOUR)-2,(CURRENT_HOUR)+2));

        List<Driver> valencian_drivers = new LinkedList<Driver>();
        List<Driver> available_valencian_drivers = new LinkedList<Driver>();
        
        valencian_drivers.add(driver1); valencian_drivers.add(driver2);
        available_valencian_drivers.add(driver2);

        try {
            List<Driver> foundDrivers = driverService.findDriversByCity("Valencia");
            assertEquals(available_valencian_drivers, foundDrivers);
            assertEquals(1, foundDrivers.size());
            assertEquals(available_valencian_drivers.get(0), foundDrivers.get(0));
        } finally {
            // Clear Database
            for (Driver driver: valencian_drivers)
                removeDriver(driver.getDriverId());
            }
    }


    @Test
    public void testfindDriversByCityWithActiveDrivers() {

        // The following two drivers are in the situation of a driver 
        // that has an average score greater or equal than 5 points.

        // Pepe is active but out of time, he should not be included in the result         
        Driver driver1 = getValidDriver("Pepe","Pontevedra",(CURRENT_HOUR)-2,(CURRENT_HOUR)-1);
        driver1.setPuntuacionTotal((float)35);
        for (int i=0;i<4;i++)
            driver1.sumarViaje();
        driver1 = createDriver(driver1);

        // Jose is available, he should be included in the result 
        Driver driver2 = getValidDriver("Jose","Pontevedra",(CURRENT_HOUR)-1,(CURRENT_HOUR)+1);
        driver2.setPuntuacionTotal((float)30);
        for (int i=0;i<5;i++)
            driver2.sumarViaje();
        driver2 = createDriver(driver2);
        
        List<Driver> galician_drivers = new LinkedList<Driver>();
        List<Driver> available_galician_drivers = new LinkedList<Driver>();

        galician_drivers.add(driver1); galician_drivers.add(driver2);
        available_galician_drivers.add(driver2);

        try {
            List<Driver> foundDrivers = driverService.findDriversByCity("Pontevedra");
            assertEquals(available_galician_drivers, foundDrivers);
            assertEquals(1, foundDrivers.size());
            assertEquals(available_galician_drivers.get(0), foundDrivers.get(0));
        } finally {
            for (Driver driver: galician_drivers)
                removeDriver(driver.getDriverId());
        }
    }


    @Test
    public void testfindDriversByCityWithInactiveDrivers() {

        // The following two drivers are in the situation of a driver 
        // that has an average score less than 5 points.

        // Ramon is on time but inactive, he should not be included in the result         
        Driver driver1 = getValidDriver("Ramon","Barcelona",(CURRENT_HOUR)-2,(CURRENT_HOUR)+2);
        driver1.setPuntuacionTotal((float)35);
        for (int i=0;i<10;i++)
            driver1.sumarViaje();
        driver1 = createDriver(driver1);

        // Amalia is inactive and out of time, she should not be included in the result
        Driver driver2 = getValidDriver("Amalia","Barcelona",(CURRENT_HOUR)-2,(CURRENT_HOUR)-1);
        driver2.setPuntuacionTotal((float)50);
        for (int i=0;i<12;i++)
            driver2.sumarViaje();
        driver2 = createDriver(driver2);

        // Carmen is available, she should be included in the result 
        Driver driver3 = getValidDriver("Carmen","Barcelona",(CURRENT_HOUR)-2,(CURRENT_HOUR)+1);
        driver3.setPuntuacionTotal((float)40);
        for (int i=0;i<4;i++)
            driver3.sumarViaje();
        driver3 = createDriver(driver3);

        List<Driver> catalonian_drivers = new LinkedList<Driver>();
        List<Driver> available_catalonian_drivers = new LinkedList<Driver>();

        catalonian_drivers.add(driver1); catalonian_drivers.add(driver2);
        catalonian_drivers.add(driver3);
        available_catalonian_drivers.add(driver3);

        try {
            List<Driver> foundDrivers = driverService.findDriversByCity("Barcelona");
            assertEquals(available_catalonian_drivers, foundDrivers);
            assertEquals(1, foundDrivers.size());
            assertEquals(available_catalonian_drivers.get(0), foundDrivers.get(0));

        } finally {
            // Clear Database
            for (Driver driver: catalonian_drivers)
                removeDriver(driver.getDriverId());
        }
    }


    @Test
    public void testfindDriversByCityWithNonExistentCity() {
        // No driver working in Luxembourg resides in our database
        List<Driver> foundDrivers = driverService.findDriversByCity("Luxembourg");
        assertTrue(foundDrivers.isEmpty());
    }


    @Test
    public void testHireTrip() throws InstanceNotFoundException,
                InputValidationException, InvalidDriverException {
        
        Driver driver = createDriver(getValidDriver());
        Trip trip = null;

        try{
            trip = driverService.hireTrip(driver.getDriverId(), "Calle Real", "Plaza de Vigo", "Jose",
                                          VALID_CREDIT_CARD_NUMBER);

            Trip foundTrip = findTripById(trip.getTripId());
            //Trip foundTrip = driverService.findTripsByDriver(driver.getDriverId()).get(0);
            
            assertEquals(trip, foundTrip);
            assertEquals(foundTrip.getOrigen(),"Calle Real");
            assertEquals(foundTrip.getDestino(),"Plaza de Vigo");
            assertEquals(foundTrip.getUser(),"Jose");
            assertEquals(foundTrip.getCreditCardNumber(),VALID_CREDIT_CARD_NUMBER);
            
        } finally {
            if (trip!=null){
                removeTrip(trip.getTripId());
            }
            removeDriver(driver.getDriverId());
        }
    }

    @Test(expected = InputValidationException.class)
    public void testHireTripWithInvalidCreditCard() throws InputValidationException,
           InstanceNotFoundException, InvalidDriverException {
        
        Driver driver = createDriver(getValidDriver());
        Trip trip = null;
        try {
            trip = driverService.hireTrip(driver.getDriverId(), "Calle Real", "Plaza de Vigo", "Jose",
                                               INVALID_CREDIT_CARD_NUMBER);
        } finally{
            if (trip!=null){
                removeTrip(trip.getTripId());
            }
            removeDriver(driver.getDriverId());
        }
    }

    @Test(expected = InstanceNotFoundException.class)
    public void testHireTripNonExistentDriver() throws InputValidationException,
                InstanceNotFoundException, InvalidDriverException {
        Trip trip = null;
        try {
            trip = driverService.hireTrip(NON_EXISTENT_DRIVER_ID, "Calle Real", "Plaza de Vigo", "Jose", 
                                               VALID_CREDIT_CARD_NUMBER);
        } finally{
            if (trip!=null)
                removeTrip(trip.getTripId());
        }
    }


    @Test(expected = InstanceNotFoundException.class)
    public void testscoreNonExistentTrip() throws InstanceNotFoundException,
            InputValidationException, InvalidRatingException
    {
        // Should throw an InstanceNotFoundException
        driverService.scoreTrip(USUARIO, NON_EXISTENT_TRIP_ID, 7);
    }


    @Test(expected = InputValidationException.class)
    public void testscoreTripWithInvalidScore() throws InstanceNotFoundException,
           InputValidationException, InvalidDriverException, InvalidRatingException
    {
        // Available driver
        Driver driver = createDriver(getValidDriver());
        Trip trip = null;
        try {
            // Hire the trip with the recently created driver
            trip = driverService.hireTrip(driver.getDriverId(), "Barcelona", "Huesca", USUARIO,
                                          VALID_CREDIT_CARD_NUMBER);
            // Should throw an InputValidationException
            driverService.scoreTrip(USUARIO, trip.getTripId(), 17);
        }
        finally {
            if (trip != null)
                removeTrip(trip.getTripId());
            removeDriver(driver.getDriverId());
        }
    }

    @Test(expected = InvalidRatingException.class)
    public void testscoreTripInvalidUser() throws InstanceNotFoundException,
           InputValidationException, InvalidDriverException, InvalidRatingException
    {
        //Available driver
        Driver driver = createDriver(getValidDriver());
        Trip trip = null;
        try {
            //Hire the trip with the recently created driver
            trip = driverService.hireTrip(driver.getDriverId(), "Barcelona", "Alicante", USUARIO,
                                           VALID_CREDIT_CARD_NUMBER);
            //Should throw an InvalidRatingException
            driverService.scoreTrip(PHISHER, trip.getTripId(), 8);
        }
        finally {
            if (trip != null)
                removeTrip(trip.getTripId());
            removeDriver(driver.getDriverId());
        }
    }
 
    @Test(expected = InvalidRatingException.class)
    public void testscoreTripAlreadyRated() throws InstanceNotFoundException,
           InputValidationException, InvalidDriverException, InvalidRatingException
    {
        //Available driver
        Driver driver = createDriver(getValidDriver());
        Trip trip = null;
        try {
            //Hire the trip with the recently created driver
            trip = driverService.hireTrip(driver.getDriverId(), "Barcelona", "Tarragona", USUARIO,
                                       VALID_CREDIT_CARD_NUMBER);
            //Should throw an InvalidRatingException
            driverService.scoreTrip(USUARIO, trip.getTripId(), 8);
            driverService.scoreTrip(USUARIO, trip.getTripId(), 3);
        }
        finally {
            if (trip != null)
                removeTrip(trip.getTripId());
            removeDriver(driver.getDriverId());
        }
    }


    @Test
    public void testscoreTrip() throws InstanceNotFoundException,
           InputValidationException, InvalidDriverException, InvalidRatingException
    {
        //Available driver
        Driver driver = createDriver(getValidDriver());
        Trip trip = null;
        try {
            //Hire the trip the recently created driver
            trip = driverService.hireTrip(driver.getDriverId(), "Barcelona", "Teruel", USUARIO,
                                           VALID_CREDIT_CARD_NUMBER);
/*
            Trip foundTrip = findTripById(trip.getTripId());
            assertEquals(foundTrip.getTripId(), trip.getTripId());
            assertEquals(foundTrip.getUser(), trip.getUser());
            assertEquals(foundTrip.getUser(), USUARIO);
            System.out.println("Poraca");
*/
            //Score the trip
            driverService.scoreTrip(USUARIO, trip.getTripId(), 4);
            System.out.println("Nollega");

            Trip foundTrip = findTripById(trip.getTripId());
            assertEquals(foundTrip.getUser(), USUARIO);
            assertEquals(foundTrip.getValoracion(), 4);
        }
        finally {
            if (trip != null)
                removeTrip(trip.getTripId());
            removeDriver(driver.getDriverId());
        }
    }

    @Test(expected = InstanceNotFoundException.class)
    public void testfindTripsByDriverWhitNoTrips () throws InstanceNotFoundException {  	
    	
    	Driver driver = createDriver(getValidDriver());
    	try {
	    	driver =  driverService.findDriverById(driver.getDriverId());
	        driverService.findTripsByDriver(driver.getDriverId());	
    	} finally {
            removeDriver(driver.getDriverId());
        }
    }
    
    @Test
    public void testfindTripsByDriver() throws InstanceNotFoundException, InputValidationException, InvalidDriverException{
        
    	Driver driver = createDriver(getValidDriver());
    	Trip trip = driverService.hireTrip((driver.getDriverId()), "origen1", "destino1", "usuario1", VALID_CREDIT_CARD_NUMBER);
    	Trip trip2 = driverService.hireTrip((driver.getDriverId()), "origen1", "destino1", "usuario1", VALID_CREDIT_CARD_NUMBER);
    	try {
	        driver =  driverService.findDriverById(driver.getDriverId());                
	        try {
	        driverService.hireTrip((driver.getDriverId()), "origen1", "destino1", "usuario1", INVALID_CREDIT_CARD_NUMBER);
	        } catch (Exception e){};
	        int numViajes = (driverService.findTripsByDriver(driver.getDriverId())).size();
	        assertEquals(numViajes,2);
    	} finally {
    		removeTrip(trip.getTripId());
    		removeTrip(trip2.getTripId());
    		removeDriver(driver.getDriverId());
    	}
    }
    
    @Test(expected = InstanceNotFoundException.class)
    public void testfindTripsByUserNoExists () throws InstanceNotFoundException {  	
    	
    	driverService.findTripsByUser("usuarioNoExistente");
    }

    @Test
    public void testfindTripsByUser () throws InstanceNotFoundException, InputValidationException, InvalidDriverException {  	
    	
    	Driver driver = createDriver(getValidDriver());
    	Trip trip = driverService.hireTrip((driver.getDriverId()), "origen1", "destino1", USUARIO, VALID_CREDIT_CARD_NUMBER);
    	
    	assertEquals((driverService.findTripsByUser(USUARIO)).size(),1);
    	removeTrip(trip.getTripId());
    	removeDriver(driver.getDriverId());
    }
}
