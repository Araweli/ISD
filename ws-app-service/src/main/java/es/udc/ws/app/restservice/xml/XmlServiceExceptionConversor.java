package es.udc.ws.app.restservice.xml;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;

import es.udc.ws.app.model.driverservice.exceptions.InvalidDriverException;
import es.udc.ws.app.model.driverservice.exceptions.InvalidRatingException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class XmlServiceExceptionConversor {

	public final static String CONVERSION_PATTERN = "HH:mm Z";

    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/drivers/xml");

    
    public static Document toInputValidationExceptionXml(InputValidationException ex) throws IOException {

        Element exceptionElement = new Element("InputValidationException", XML_NS);

        Element messageElement = new Element("message", XML_NS);
        messageElement.setText(ex.getMessage());
        exceptionElement.addContent(messageElement);

        return new Document(exceptionElement);
    }

    
    public static Document toInstanceNotFoundException(InstanceNotFoundException ex) throws IOException {

        Element exceptionElement = new Element("InstanceNotFoundException", XML_NS);

        if (ex.getInstanceId() != null) {
            Element instanceIdElement = new Element("instanceId", XML_NS);
            instanceIdElement.setText(ex.getInstanceId().toString());

            exceptionElement.addContent(instanceIdElement);
        }

        if (ex.getInstanceType() != null) {
            Element instanceTypeElement = new Element("instanceType", XML_NS);
            instanceTypeElement.setText(ex.getInstanceType());

            exceptionElement.addContent(instanceTypeElement);
        }
        return new Document(exceptionElement);
    }
    
    
    public static Document toInvalidDriverExceptionXml(InvalidDriverException ex) throws IOException {

        Element exceptionElement = new Element("InvalidDriverException", XML_NS);

        SimpleDateFormat dateFormatter = new SimpleDateFormat(CONVERSION_PATTERN, Locale.ENGLISH);
        
        if (ex.getDriverId() != null) {
            Element driverIdElement = new Element("driverId", XML_NS);
            driverIdElement.setText(ex.getDriverId().toString());
            exceptionElement.addContent(driverIdElement);
        }
        if (ex.getExpirationDate() != null) {
            Element expirationDateElement = new Element("expirationDate", XML_NS);
            expirationDateElement.setText(dateFormatter.format(ex.getExpirationDate().getTime()));
            exceptionElement.addContent(expirationDateElement);
        }
        
        if (ex.getMessage() != null) {
            Element messageElement = new Element("message", XML_NS);
            messageElement.setText(ex.getMessage());
            exceptionElement.addContent(messageElement);
        }
        
        return new Document(exceptionElement);
    }

    
    
    public static Document toInvalidRatingExceptionXml(InvalidRatingException ex) throws IOException {

        Element exceptionElement = new Element("InvalidRatingException", XML_NS);
    
        if (ex.getUserId() != null) {
            Element userIdElement = new Element("userId", XML_NS);
            userIdElement.setText(ex.getUserId().toString());
            exceptionElement.addContent(userIdElement);
        }

        if (Integer.valueOf(ex.getValoracion()) != null) {
            Element ratingElement = new Element("valoracion", XML_NS);
            ratingElement.setText(Integer.valueOf(ex.getValoracion()).toString());
            exceptionElement.addContent(ratingElement);
        }
    
        if (ex.getMessage() != null) {
            Element messageElement = new Element("message", XML_NS);
            messageElement.setText(ex.getMessage());
            exceptionElement.addContent(messageElement);
        }

        return new Document(exceptionElement);
    }

}