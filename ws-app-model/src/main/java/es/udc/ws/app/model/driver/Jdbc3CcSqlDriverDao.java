package es.udc.ws.app.model.driver;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;


public class Jdbc3CcSqlDriverDao extends AbstractSqlDriverDao{
	
	@Override
    public Driver create(Connection connection, Driver driver) {

        /* Create "queryString". */
        String queryString = "INSERT INTO Driver"
                + " (name, city, car, startTime, endTime, creationDate,"
                + " puntuacionTotal, numviajes)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                        queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, driver.getName());
            preparedStatement.setString(i++, driver.getCity());
            preparedStatement.setString(i++, driver.getCar());
            preparedStatement.setInt(i++, driver.getStartTime());
            preparedStatement.setInt(i++, driver.getEndTime());
            Timestamp date = driver.getCreationDate() != null ? new Timestamp(
                    driver.getCreationDate().getTime().getTime()) : null;
            preparedStatement.setTimestamp(i++, date);
            preparedStatement.setFloat(i++, driver.getPuntuacionTotal());
            preparedStatement.setInt(i++, driver.getNumViajes());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long driverId = resultSet.getLong(1);

            /* Return driver. */
            return new Driver(driver.getCreationDate(), driverId, driver.getName(), driver.getCity(),
                    driver.getCar(), driver.getStartTime(), driver.getEndTime(), driver.getPuntuacionTotal(),
                    driver.getNumViajes());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}
