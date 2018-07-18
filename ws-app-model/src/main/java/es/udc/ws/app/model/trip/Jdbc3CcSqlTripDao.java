package es.udc.ws.app.model.trip;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import es.udc.ws.app.model.trip.Trip;

public class Jdbc3CcSqlTripDao extends AbstractSqlTripDao {

	@Override
	public Trip create(Connection connection, Trip trip) {

		/* Create "queryString". */
		String queryString = "INSERT INTO Trip" 
		        + " (driverId, origen, destino, user, creditCardNumber, "
		        +" reservationDate, valoracion)"
				+ " VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (PreparedStatement preparedStatement = connection.prepareStatement(queryString,
				Statement.RETURN_GENERATED_KEYS)) {

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

			/* Execute query. */
			preparedStatement.executeUpdate();

			/* Get generated identifier. */
			ResultSet resultSet = preparedStatement.getGeneratedKeys();

			if (!resultSet.next()) {
				throw new SQLException("JDBC driver did not return generated key.");
			}
			Long tripId = resultSet.getLong(1);

			/* Return trip. */
			return new Trip(tripId,trip.getReservationDate(),-1, trip.getDriverId(), trip.getOrigen(), trip.getDestino(),
					trip.getUser(), trip.getCreditCardNumber());

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

	}


}