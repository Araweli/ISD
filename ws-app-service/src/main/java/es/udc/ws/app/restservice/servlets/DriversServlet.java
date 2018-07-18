package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceDriverDto;
import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.driver.driverservice.DriverServiceFactory;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.restservice.xml.XmlServiceDriverDtoConversor;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.serviceutil.DriverToDriverDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;

@SuppressWarnings("serial")
public class DriversServlet  extends HttpServlet {

	
	@Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServiceDriverDto xmldriver;
        try {
            xmldriver = XmlServiceDriverDtoConversor.toServiceDriverDto(req.getInputStream());
        } catch (ParsingException ex) {

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                    .toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);
            return;

        }
        Driver driver = DriverToDriverDtoConversor.toDriver(xmldriver);
        try {
            driver = DriverServiceFactory.getService().addDriver(driver);
        } catch (InputValidationException ex) {

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
            return;
        }
        ServiceDriverDto driverDto = DriverToDriverDtoConversor.toDriverDto(driver);

        String driverURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + driver.getDriverId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", driverURL);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlServiceDriverDtoConversor.toXml(driverDto), headers);
    }
	


	@Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {
            
            String user = req.getParameter("user"); //Encontrar conductores de un usuario
            if (user!=null){
                
                try{
                    List<Trip> trips = DriverServiceFactory.getService().findTripsByUser(user);
                    int i= 0;
                    List<Driver> drivers = new ArrayList<Driver>();
                    
                    do {
                        Driver driver = DriverServiceFactory.getService().findDriverById(trips.get(i).getDriverId());
                        drivers.add(driver);
                        i++;
                    } while (i< trips.size());
                    
                    HashSet<Driver> hashSet = new HashSet<Driver>(drivers);
                    drivers.clear();
                    drivers.addAll(hashSet);
                    
                    List<ServiceDriverDto> driverDtos = DriverToDriverDtoConversor.toDriverDtos(drivers);
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                            XmlServiceDriverDtoConversor.toXml(driverDtos), null);
                } catch (InstanceNotFoundException e) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                            XmlServiceExceptionConversor.toInstanceNotFoundException(e), null);
                    return;
                }
                
            } else {
                String city = req.getParameter("city"); //encontrar conductores disponibles por ciudad
                if (city == null) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
                                    "Invalid Request: DriverId or UrlParameter city required")),
                        null);
                    return;
                }
                List<Driver> drivers = DriverServiceFactory.getService().findDriversByCity(city);
                List<ServiceDriverDto> driverDtos = DriverToDriverDtoConversor.toDriverDtos(drivers);
                for (int i = 0; i < driverDtos.size(); i++) {
                    ServiceDriverDto driverDto = driverDtos.get(i);
                    if (drivers.get(i).getNumViajes() == 0)
                        continue;
                    driverDto.setPuntuacionMedia(drivers.get(i).getPuntuacionTotal()/drivers.get(i).getNumViajes());
                }

                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                        XmlServiceDriverDtoConversor.toXml(driverDtos), null);
            }
        } else {
            String driverIdAsString = path.substring(1); //encontrar conductor por id
            Long driverId;
            try {
                driverId = Long.valueOf(driverIdAsString);
            } catch (NumberFormatException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
                                "Invalid Request: " + "invalid driver id'" + driverIdAsString + "'")),
                        null);

                return;
            }
            Driver driver;
            try {
                driver = DriverServiceFactory.getService().findDriverById(driverId);
            } catch (InstanceNotFoundException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
                return;
            }
            ServiceDriverDto driverDto = DriverToDriverDtoConversor.toDriverDto(driver);
            if (driver.getNumViajes() != 0)
                driverDto.setPuntuacionMedia(driver.getPuntuacionTotal()/driver.getNumViajes());
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    XmlServiceDriverDtoConversor.toXml(driverDto), null);
        }
    }


/*
	 @Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
	        String path = ServletUtils.normalizePath(req.getPathInfo());
	        if (path == null || path.length() == 0) {

	            String city = req.getParameter("city");
	            if (city == null) {
	                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                        XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                                "Invalid Request: DriverId or UrlParameter city required")),
                        null);
	                return;
	            }
	            List<Driver> drivers = DriverServiceFactory.getService().findDriversByCity(city);
	            List<ServiceDriverDto> driverDtos = DriverToDriverDtoConversor.toDriverDtos(drivers);
	            for (int i = 0; i < driverDtos.size(); i++) {
                    ServiceDriverDto driverDto = driverDtos.get(i);
                    if (drivers.get(i).getNumViajes() == 0)
                        continue;
	                driverDto.setPuntuacionMedia(drivers.get(i).getPuntuacionTotal()/drivers.get(i).getNumViajes());
	            }

	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
	                    XmlServiceDriverDtoConversor.toXml(driverDtos), null);
	        } else {
	            String driverIdAsString = path.substring(1);
	            Long driverId;
	            try {
	                driverId = Long.valueOf(driverIdAsString);
	            } catch (NumberFormatException ex) {
	                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                        XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                                "Invalid Request: " + "invalid driver id'" + driverIdAsString + "'")),
	                        null);

	                return;
	            }
	            Driver driver;
	            try {
	                driver = DriverServiceFactory.getService().findDriverById(driverId);
	            } catch (InstanceNotFoundException ex) {
	                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	                        XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
	                return;
	            }
	            ServiceDriverDto driverDto = DriverToDriverDtoConversor.toDriverDto(driver);
	            if (driver.getNumViajes() != 0)
	                driverDto.setPuntuacionMedia(driver.getPuntuacionTotal()/driver.getNumViajes());
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
	                    XmlServiceDriverDtoConversor.toXml(driverDto), null);
	        }
	    }
*/
	 
	    @Override
	    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	        String path = ServletUtils.normalizePath(req.getPathInfo());
	        
	        if (path == null || path.length() == 0) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
	                            new InputValidationException("Invalid Request: " + "invalid driver id " + ": you must indicate a driver id")),
	                    null);
	            return;
	        }
	        
	        String driverIdAsString = path.substring(1);
	
	        Long driverId;
	        try {
	            driverId = Long.valueOf(driverIdAsString);
	        } catch (NumberFormatException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                            "Invalid Request: " + "invalid driver id '" + driverIdAsString + "'")),
	                    null);
	            return;
	        }

	        ServiceDriverDto driverDto;
	        try {
	            driverDto = XmlServiceDriverDtoConversor.toServiceDriverDto(req.getInputStream());
	        } catch (ParsingException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
	                    .toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);
	            return;

	        }
	        if (!driverId.equals(driverDto.getDriverId())) {

	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
	                            new InputValidationException("Invalid Request: " + "invalid driverId" + ": different driverId")),
	                    null);
	            return;
	        }       
	        
	        Driver driver = DriverToDriverDtoConversor.toDriver(driverDto);
	        try {
	            DriverServiceFactory.getService().updateDriver(driver);
	        } catch (InputValidationException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
	            return;
	        } catch (InstanceNotFoundException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	                    XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
	            return;
	        }
	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
	    }
	
	
}
