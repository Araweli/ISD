package es.udc.ws.app.model.trip;

import java.sql.Connection;
import java.util.List;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlTripDao {

    public Trip create(Connection c, Trip t);
    
    public void update(Connection c, Trip t)
            throws InstanceNotFoundException;
    
    public void remove(Connection c, Long tripId)
            throws InstanceNotFoundException;
	

    public Trip find(Connection c, Long tripId)
            throws InstanceNotFoundException;

    public List<Trip> findTripsByUser(Connection c,String user)
    		throws InstanceNotFoundException;
  
    public List<Trip> findTripsByDriver(Connection c,Long driverId)
    		throws InstanceNotFoundException;
}
