package es.udc.ws.app.model.trip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public abstract class AbstractSqlTripDao implements SqlTripDao {

	protected AbstractSqlTripDao() {
	}

	@Override
	public Trip find(Connection connection, Long tripId) throws InstanceNotFoundException {

		/* Create "queryString". */
		String queryString = "SELECT driverId, origen, destino, user,"
				+ " creditCardNumber, reservationDate, valoracion FROM Trip WHERE tripId = ?";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

			/* Fill "preparedStatement". */
			int i = 1;
			preparedStatement.setLong(i++, tripId.longValue());

			/* Execute query. */
			ResultSet resultSet = preparedStatement.executeQuery();

			if (!resultSet.next()) {
				throw new InstanceNotFoundException(tripId, Trip.class.getName());
			}

            /* Get results. */
            i = 1;
            Long driverId = resultSet.getLong(i++);
            String origen = resultSet.getString(i++);
            String destino = resultSet.getString(i++);
            String user = resultSet.getString(i++);
            String creditCardNumber = resultSet.getString(i++);
            Calendar reservationDate = Calendar.getInstance();
            reservationDate.setTime(resultSet.getTimestamp(i++));
            int valoracion = resultSet.getInt(i++);

			/* Return Trip. */
			return new Trip(tripId,reservationDate, valoracion, 
					driverId, origen, destino, user, creditCardNumber);

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	public void update(Connection c, Trip trip) throws InstanceNotFoundException{

        /* Create "queryString". */
        String queryString = "UPDATE Trip"
                + " SET driverId = ?, origen = ?, destino = ?, user = ?, "
                + "creditCardNumber = ?, reservationDate = ?, valoracion = ? "
                + "WHERE tripId = ?";

		try (PreparedStatement preparedStatement = c.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, trip.getDriverId());
            preparedStatement.setString(i++, trip.getOrigen());
            preparedStatement.setString(i++, trip.getDestino());
            preparedStatement.setString(i++, trip.getUser());
            preparedStatement.setString(i++, trip.getCreditCardNumber());
            Timestamp date = trip.getReservationDate() != null
                    ? new Timestamp(trip.getReservationDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setInt(i++, trip.getValoracion());
            preparedStatement.setLong(i++, trip.getTripId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(trip.getDriverId(),
                        Trip.class.getName());
            }

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}
	

	
    @Override
    public List<Trip> findTripsByUser(Connection c, String user) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT tripId, driverId, origen, destino,"
                + " creditCardNumber, reservationDate, valoracion FROM Trip WHERE user = ?";

        try (PreparedStatement preparedStatement = c.prepareStatement(queryString)) {
            
            /*rellenamos la ? con el driverId*/
            int i = 1;
            preparedStatement.setString(i++, user);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(user, Trip.class.getName());
            }

            /* Get results. */
            
            List<Trip> trips = new ArrayList<Trip>();
            
            do {
                i = 1;
                Long tripId = resultSet.getLong(i++);
                Long driverId = resultSet.getLong(i++);
                String origen = resultSet.getString(i++);
                String destino = resultSet.getString(i++);
                String creditCardNumber = resultSet.getString(i++);
                Calendar reservationDate = Calendar.getInstance();
                reservationDate.setTime(resultSet.getTimestamp(i++));
                int valoracion = resultSet.getInt(i++);
                
                trips.add(new Trip(tripId,reservationDate, valoracion, 
                        driverId, origen, destino, user, creditCardNumber));
            } while (resultSet.next());

            /* Return trips. */
            return trips; 
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    @Override
    public List<Trip> findTripsByDriver(Connection c, Long driverId) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "SELECT tripId, origen, destino, user, creditCardNumber,"
                + "  reservationDate, valoracion FROM Trip WHERE driverId = ?";
        
        try (PreparedStatement preparedStatement = c.prepareStatement(queryString)) {
            
            /*rellenamos la ? con el driverId*/
            int i = 1;
            preparedStatement.setLong(i++, driverId);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(driverId, Trip.class.getName());
            }

            /* Get results. */
            
            List<Trip> trips = new ArrayList<Trip>();
            
            do {
                i = 1;
                Long tripId = resultSet.getLong(i++);
                String origen = resultSet.getString(i++);
                String destino = resultSet.getString(i++);
                String user = resultSet.getString(i++);
                String creditCardNumber = resultSet.getString(i++);
                Calendar reservationDate = Calendar.getInstance();
                reservationDate.setTime(resultSet.getTimestamp(i++));
                int valoracion = resultSet.getInt(i++);
                
                trips.add(new Trip(tripId,reservationDate, valoracion, 
                        driverId, origen, destino, user, creditCardNumber));
            } while(resultSet.next());

            /* Return trips. */
            return trips; 
            
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    
    
    
    @Override
    public void remove(Connection connection, Long tripId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Trip WHERE tripId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, tripId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(tripId,
                        Trip.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}