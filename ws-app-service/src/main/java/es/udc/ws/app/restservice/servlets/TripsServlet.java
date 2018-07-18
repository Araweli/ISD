package es.udc.ws.app.restservice.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.dto.ServiceDriverDto;
import es.udc.ws.app.dto.ServiceTripDto;
import es.udc.ws.app.model.driver.Driver;
import es.udc.ws.app.model.driver.driverservice.DriverServiceFactory;
import es.udc.ws.app.model.driverservice.exceptions.InvalidDriverException;
import es.udc.ws.app.model.trip.Trip;
import es.udc.ws.app.restservice.xml.XmlServiceDriverDtoConversor;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.app.restservice.xml.XmlServiceTripDtoConversor;
import es.udc.ws.app.serviceutil.DriverToDriverDtoConversor;
import es.udc.ws.app.serviceutil.TripToTripDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.xml.exceptions.ParsingException;


@SuppressWarnings("serial")
public class TripsServlet  extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        ServiceTripDto tripDto;
        try {
            tripDto = XmlServiceTripDtoConversor.toServiceTripDto(req.getInputStream());
        } catch (ParsingException ex) {
            System.out.println("catch (ParsingException ex)");
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST, XmlServiceExceptionConversor
                .toInputValidationExceptionXml(new InputValidationException(ex.getMessage())), null);
            return;
        }

        Trip trip = TripToTripDtoConversor.toTrip(tripDto);
        try {
            trip = DriverServiceFactory.getService().hireTrip(trip.getDriverId(), trip.getOrigen(), trip.getDestino(), trip.getUser(),
                                                              trip.getCreditCardNumber());
        } catch (InputValidationException ex) {
            System.out.println("catch (InputValidationException ex)");
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
            return;
        } catch (InstanceNotFoundException ex) {
            System.out.println("catch (InstanceNotFoundException ex)");
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
            XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
            return;
        } catch (InvalidDriverException ex) {
            System.out.println("catch (InvalidDriverException ex)");
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
            XmlServiceExceptionConversor.toInvalidDriverExceptionXml(ex), null);
            return;
        }
        
        tripDto = TripToTripDtoConversor.toTripDto(trip);

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                XmlServiceTripDtoConversor.toXml(tripDto), null);
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if (path == null || path.length() == 0) {

            String user = req.getParameter("user");
            if (user == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
                                "Invalid Request: UrlParameter user required")),
                    null);
                return;
            }
            List<Trip> trips;
            try {
                trips = DriverServiceFactory.getService().findTripsByUser(user);
                List<ServiceTripDto> tripDtos = TripToTripDtoConversor.toTripDtos(trips);
//              for (int i = 0; i < tripDtos.size(); i++) {
//                  if (trips.get(i).getUser() == user)
//                      continue;
//                  
//              }
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                        XmlServiceTripDtoConversor.toXml(tripDtos), null);
            } catch (InstanceNotFoundException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                        XmlServiceExceptionConversor.toInstanceNotFoundException(e), null);
                return;
            }
            

            
        } else {
            String driverIdAsString = path.substring(1);
            Long driverId;
            try {
                driverId = Long.valueOf(driverIdAsString);
                List<Trip> trips;
                try {
                    trips = DriverServiceFactory.getService().findTripsByDriver(driverId);
                    
                    //comprobamos si nos han dado tambien usuario para filtrar por usuario y conductor
                    String user = req.getParameter("user");
                    if (user != null) {
                        int i = 0;
                        
                        do {
                            if (user.equals(trips.get(i).getUser())) {
                                i++;
                            } else {
                                trips.remove(i);
                            }
                        } while (i < (trips.size()));
                    } else {
                        int i = 0;
                        do {
                            trips.get(i).setCreditCardNumber(null); 
                            i++;
                        } while (i < trips.size());
                    }
                    List<ServiceTripDto> tripDtos = TripToTripDtoConversor.toTripDtos(trips);
                    
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                            XmlServiceTripDtoConversor.toXml(tripDtos), null);
                
                } catch (InstanceNotFoundException e) {
                    ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                            XmlServiceExceptionConversor.toInstanceNotFoundException(e), null);
                    return;
                }
            } catch (NumberFormatException ex) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
                                "Invalid Request: " + "invalid driver id'" + driverIdAsString + "'")),
                        null);

                return;
            }
            
        }
    }
}