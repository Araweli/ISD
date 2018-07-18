package es.udc.ws.app.model.driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;


public abstract class AbstractSqlDriverDao implements SqlDriverDao {

	protected AbstractSqlDriverDao() {
    }
	
	@Override
    public void update(Connection connection, Driver driver) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "UPDATE Driver"
                + " SET city = ?, car = ?, startTime = ?, "
                + "endTime = ?, puntuacionTotal = ?, numviajes = ? "
                + "WHERE driverId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, driver.getCity());
            preparedStatement.setString(i++, driver.getCar());
            preparedStatement.setInt(i++, driver.getStartTime());
            preparedStatement.setInt(i++, driver.getEndTime());
            preparedStatement.setFloat(i++, driver.getPuntuacionTotal());
            preparedStatement.setInt(i++, driver.getNumViajes());
            preparedStatement.setLong(i++, driver.getDriverId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(driver.getDriverId(),
                        Driver.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
	
	
    @Override
    public void remove(Connection connection, Long driverId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Driver WHERE driverId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, driverId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(driverId,
                        Driver.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

	@Override
	public Driver find(Connection c, Long driverId) throws InstanceNotFoundException {

	        /* Create "queryString". */
	        String queryString = "SELECT name, city, "
	                + " car, startTime, endTime, creationDate, "
	                + " puntuacionTotal, numviajes"
	                + " FROM Driver WHERE driverId = ?";

	        try (PreparedStatement preparedStatement = c.prepareStatement(queryString)) {

	            /* Fill "preparedStatement". */
	            int i = 1;
	            preparedStatement.setLong(i++, driverId.longValue());

	            /* Execute query. */
	            ResultSet resultSet = preparedStatement.executeQuery();

	            if (!resultSet.next()) {
	                throw new InstanceNotFoundException(driverId,Driver.class.getName());
	            }

	            /* Get results. */
	            i = 1;
	            String name = resultSet.getString(i++);
	            String city = resultSet.getString(i++);
	            String car = resultSet.getString(i++);
	            int startTime = resultSet.getInt(i++);
	            int endTime = resultSet.getInt(i++);
	            Calendar creationDate = Calendar.getInstance();
	            creationDate.setTime(resultSet.getTimestamp(i++));
	            float puntuacionTotal = resultSet.getFloat(i++);
	            int numviajes = resultSet.getInt(i++);
	            /* Return driver. */
	            return new Driver(creationDate, driverId, name, city, car, startTime, endTime,
	                              puntuacionTotal, numviajes);

	        } catch (SQLException e) {
	            throw new RuntimeException(e);
	        }

	    }

	@Override
	public List<Driver> findDriversByCity(Connection c, String city) {

        String queryString = "SELECT driverId, name, city,"
        + " car, startTime, endTime, creationDate, puntuacionTotal,"
        + " numviajes "
        + " FROM Driver WHERE city = ? "
        + "             AND( (numviajes>0 AND (puntuacionTotal/numviajes) >= 5)"
        + "                               OR numviajes = 0)"
        + "             AND startTime <= ?"
        + "             AND endTime >= ?";

        try (PreparedStatement preparedStatement = c.prepareStatement(queryString)) {

            Calendar calendar = Calendar.getInstance();
            int current_time = calendar.get(Calendar.HOUR_OF_DAY);

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, city);
            preparedStatement.setInt(i++, current_time);
            preparedStatement.setInt(i++, current_time);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Retrieve matching drivers. */
            List<Driver> drivers = new ArrayList<Driver>();

            while (resultSet.next()) {
                /* Get results. */
                i = 1;
                Long driverId = resultSet.getLong(i++);
                String name = resultSet.getString(i++);
                city = resultSet.getString(i++);
                String car = resultSet.getString(i++);
                int startTime = resultSet.getInt(i++);
                int endTime = resultSet.getInt(i++);
                Calendar creationDate = Calendar.getInstance();
                creationDate.setTime(resultSet.getTimestamp(i++));
                float puntuacionTotal = resultSet.getFloat(i++);
                int numviajes = resultSet.getInt(i++);

                drivers.add(new Driver(creationDate, driverId, name, city, car, startTime, endTime,
                                       puntuacionTotal, numviajes));
            }
            
            return drivers;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}