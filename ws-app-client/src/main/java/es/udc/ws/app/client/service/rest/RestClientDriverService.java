package es.udc.ws.app.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import es.udc.ws.app.client.service.ClientDriverService;
import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.xml.XmlClientDriverDtoConversor;
import es.udc.ws.app.client.service.xml.XmlClientExceptionConversor;
import es.udc.ws.app.client.service.xml.XmlClientTripDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class RestClientDriverService implements ClientDriverService {
	
	private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientDriverService.endpointAddress";
    private String endpointAddress;
	
	  @Override
	    public Long addDriver(ClientDriverDto driver) throws InputValidationException {

	        try {

	            HttpResponse response = Request.Post(getEndpointAddress() + "drivers").
	                    bodyStream(toInputStream(driver), ContentType.create("application/xml")).
	                    execute().returnResponse();

	            validateStatusCode(HttpStatus.SC_CREATED, response);
	            return XmlClientDriverDtoConversor.toClientDriverDto(response.getEntity().getContent()).getDriverId();

	        } catch (InputValidationException e) {
	            throw e;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }

	    }

		@Override
		public ClientDriverDto findDriverById(Long driverId) throws InstanceNotFoundException {
	
			try {
	
				HttpResponse response = Request.Get(getEndpointAddress() + "drivers/" + driverId).execute()
						.returnResponse();
	
				validateStatusCode(HttpStatus.SC_OK, response);
	
				return XmlClientDriverDtoConversor.toClientDriverDto(response.getEntity().getContent());
	
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	
		}


	    @Override
	    public void updateDriver(ClientDriverDto driver) throws InputValidationException,
	            InstanceNotFoundException {

	        try {
	            HttpResponse response = Request.Put(getEndpointAddress() + "drivers/" + driver.getDriverId()).
	                    bodyStream(toInputStream(driver), ContentType.create("application/xml")).
	                    execute().returnResponse();

	            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
	            
	        } catch (InputValidationException | InstanceNotFoundException e) {
	            throw e;
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        }

	    }


        @Override
        public List<ClientTripDto> findTripsByDriver(Long driverId) throws InputValidationException, InstanceNotFoundException {
            try {
                
                HttpResponse response = Request.Get(getEndpointAddress() + "trips/" + driverId).execute()
                        .returnResponse();
    
                validateStatusCode(HttpStatus.SC_OK, response);
    
                return XmlClientTripDtoConversor.toClientTripDtos(response.getEntity().getContent());
                
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    
        }


	    private synchronized String getEndpointAddress() {
	        if (endpointAddress == null) {
	            endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
	        }
	        return endpointAddress;
	    }

	    private InputStream toInputStream(ClientDriverDto driver) {

	        try {

	            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
	            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

	            outputter.output(XmlClientDriverDtoConversor.toXml(driver), xmlOutputStream);

	            return new ByteArrayInputStream(xmlOutputStream.toByteArray());

	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }

	    }

	    private void validateStatusCode(int successCode, HttpResponse response)
	            throws InstanceNotFoundException,
	            InputValidationException, ParsingException {

	        try {

	            int statusCode = response.getStatusLine().getStatusCode();

	            /* Success? */
	            if (statusCode == successCode) {
	                return;
	            }

	            /* Handler error. */
	            switch (statusCode) {

	                case HttpStatus.SC_NOT_FOUND:
	                    throw XmlClientExceptionConversor.fromInstanceNotFoundExceptionXml(
	                            response.getEntity().getContent());

	                case HttpStatus.SC_BAD_REQUEST:
	                    throw XmlClientExceptionConversor.fromInputValidationExceptionXml(
	                            response.getEntity().getContent());


	                default:
	                    throw new RuntimeException("HTTP error; status code = "
	                            + statusCode);
	            }

	        } catch (IOException e) {
	            throw new RuntimeException(e);
	        }

	    }
}