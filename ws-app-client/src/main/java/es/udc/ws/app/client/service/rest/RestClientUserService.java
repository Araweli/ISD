package es.udc.ws.app.client.service.rest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import es.udc.ws.app.client.service.ClientUserService;

import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.app.client.service.dto.ClientDriverDto;

import es.udc.ws.app.client.service.exceptions.ClientInvalidDriverException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidRatingException;
import es.udc.ws.app.client.service.xml.XmlClientDriverDtoConversor;
import es.udc.ws.app.client.service.xml.XmlClientExceptionConversor;
import es.udc.ws.app.client.service.xml.XmlClientTripDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

import es.udc.ws.util.configuration.ConfigurationParametersManager;


public class RestClientUserService implements ClientUserService {
    
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientUserService.endpointAddress";
    private String endpointAddress;


    @Override
    public List <ClientDriverDto> findDriversByCity(String city) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "drivers?city="
                    + URLEncoder.encode(city, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return XmlClientDriverDtoConversor.toClientDriverDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public void scoreTrip(String user, long tripId, int score) throws InstanceNotFoundException,
                        InputValidationException, ClientInvalidRatingException
    {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "rating").
                    bodyForm(
                            Form.form().
                            add("user", user).
                            add("tripId", Long.toString(tripId)).
                            add("score", Integer.toString(score)).
                            build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public ClientTripDto addTravel(ClientTripDto trip)
            throws InstanceNotFoundException, InputValidationException, ClientInvalidDriverException
    {
    
        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "trips").
                    bodyStream(toInputStream(trip), ContentType.create("application/xml")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);
            
            return XmlClientTripDtoConversor.toClientTripDto(
                    response.getEntity().getContent());

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public List <ClientTripDto> findTripsByUser(String user) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "trips?user="
                    + URLEncoder.encode(user, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return XmlClientTripDtoConversor.toClientTripDtos(response.getEntity()
                    .getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    @Override
    public List <ClientDriverDto> hiredDrivers(String user) throws InstanceNotFoundException {

        
        try{ 
         HttpResponse response = Request.Get(getEndpointAddress() +  "drivers?user="
                 + URLEncoder.encode(user.replace("\'", ""), "UTF-8")).
                 execute().returnResponse();

         validateStatusCode(HttpStatus.SC_OK, response);

         return XmlClientDriverDtoConversor.toClientDriverDtos(response.getEntity()
                 .getContent());
        } catch (InstanceNotFoundException e) {
            throw e;
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

    private InputStream toInputStream(ClientTripDto trip) {

        try {

            ByteArrayOutputStream xmlOutputStream = new ByteArrayOutputStream();
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());

            outputter.output(XmlClientTripDtoConversor.toXml(trip), xmlOutputStream);

            return new ByteArrayInputStream(xmlOutputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    private void validateStatusCode(int successCode, HttpResponse response)
            throws InstanceNotFoundException, InputValidationException,
            ParsingException, ClientInvalidRatingException, ClientInvalidDriverException
    {
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

                case HttpStatus.SC_FORBIDDEN:
                throw XmlClientExceptionConversor.fromInvalidRatingExceptionXml(
                            response.getEntity().getContent());

                case HttpStatus.SC_GONE:
                throw XmlClientExceptionConversor.fromInvalidDriverExceptionXml(
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