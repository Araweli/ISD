package es.udc.ws.app.restservice.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import es.udc.ws.app.model.driver.driverservice.DriverServiceFactory;
import es.udc.ws.app.model.driverservice.exceptions.InvalidRatingException;
import es.udc.ws.app.restservice.xml.XmlServiceExceptionConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;

@SuppressWarnings("serial")
public class TripsRatingServlet extends HttpServlet {
	
	
	  @Override
	    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

	        String tripIdParameter = req.getParameter("tripId");
	        if (tripIdParameter == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
	                            new InputValidationException("Invalid Request: " + "parameter 'tripId' is mandatory")),
	                    null);
	            return;
	        }
	        Long tripId;
	        try {
	        	tripId = Long.valueOf(tripIdParameter).longValue();
	        } catch (NumberFormatException ex) {
	            ServletUtils
	                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                            XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
	                                    "Invalid Request: " + "parameter 'tripId' is invalid '" + tripIdParameter + "'")),
	                            null);

	            return;
	        }

	        String user = req.getParameter("user");
	        if (user == null) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(
	                            new InputValidationException("Invalid Request: " + "parameter 'user' is mandatory")),
	                    null);
	            return;
	        }

            String scoreParameter = req.getParameter("score");
            if (scoreParameter == null) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                        XmlServiceExceptionConversor.toInputValidationExceptionXml(
                                new InputValidationException("Invalid Request: " + "parameter 'score' is mandatory")),
                        null);
                return;
            }
            int score;
            try {
                score = Integer.valueOf(tripIdParameter).intValue();
            } catch (NumberFormatException ex) {
                ServletUtils
                        .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                                XmlServiceExceptionConversor.toInputValidationExceptionXml(new InputValidationException(
                                        "Invalid Request: " + "parameter 'score' is invalid '" + scoreParameter + "'")),
                                null);
                return;
            }


	        try {
	            DriverServiceFactory.getService().scoreTrip(user, tripId, score);
	        } catch (InstanceNotFoundException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
	                    XmlServiceExceptionConversor.toInstanceNotFoundException(ex), null);
	            return;
	        } catch (InputValidationException ex) {
	            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
	                    XmlServiceExceptionConversor.toInputValidationExceptionXml(ex), null);
	            return;
	        } catch (InvalidRatingException ex) {
	        	ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN,
	                    XmlServiceExceptionConversor.toInvalidRatingExceptionXml(ex), null);
	        	return;
			}

	        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);

	    }
}