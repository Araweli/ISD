package es.udc.ws.app.model.driver;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlDriverDao {

    public Driver create(Connection c, Driver d);
    
    public void update(Connection c, Driver d)
            throws InstanceNotFoundException;
    
    public void remove(Connection c, Long driverId)
            throws InstanceNotFoundException;
	

    public Driver find(Connection c, Long driverId)
            throws InstanceNotFoundException;

    public List<Driver> findDriversByCity(Connection c,String city);
}