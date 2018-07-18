package es.udc.ws.app.client.service.xml;

import java.io.IOException;
import java.io.InputStream;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.exceptions.ClientInvalidDriverException;
import es.udc.ws.app.client.service.exceptions.ClientInvalidRatingException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientExceptionConversor {

	
	 public final static String CONVERSION_PATTERN = "EEE, d MMM yyyy HH:mm:ss Z";

	    public final static Namespace XML_NS = 
	            Namespace.getNamespace("http://ws.udc.es/drivers/xml");

	    public static InputValidationException fromInputValidationExceptionXml(InputStream ex) 
	            throws ParsingException {
	        try {

	            SAXBuilder builder = new SAXBuilder();
	            Document document = builder.build(ex);
	            Element rootElement = document.getRootElement();

	            Element message = rootElement.getChild("message", XML_NS);

	            return new InputValidationException(message.getText());
	        } catch (JDOMException | IOException e) {
	            throw new ParsingException(e);
	        } catch (Exception e) {
	            throw new ParsingException(e);
	        }
	    }

	    public static InstanceNotFoundException fromInstanceNotFoundExceptionXml(InputStream ex) 
	            throws ParsingException {
	        try {

	            SAXBuilder builder = new SAXBuilder();
	            Document document = builder.build(ex);
	            Element rootElement = document.getRootElement();

	            Element instanceId = rootElement.getChild("instanceId", XML_NS);
	            Element instanceType = rootElement.getChild("instanceType", XML_NS);

	            return new InstanceNotFoundException(instanceId.getText(), instanceType.getText());
	        } catch (JDOMException | IOException e) {
	            throw new ParsingException(e);
	        } catch (Exception e) {
	            throw new ParsingException(e);
	        }
	    }

        public static ClientInvalidRatingException fromInvalidRatingExceptionXml(InputStream ex) 
                throws ParsingException {
            try {

                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(ex);
                Element rootElement = document.getRootElement();

                Element userIdElement = rootElement.getChild("userId", XML_NS);
                Element valoracionElement = rootElement.getChild("valoracion", XML_NS);

                int valoracion = (Integer.valueOf(valoracionElement.getText())).intValue();

                if (valoracion != -1)
                    return new ClientInvalidRatingException(userIdElement.getText(), valoracion);
                else
                    return new ClientInvalidRatingException(userIdElement.getText());

            } catch (JDOMException | IOException e) {
                throw new ParsingException(e);
            } catch (Exception e) {
                throw new ParsingException(e);
            }
        }
        
        public static ClientInvalidDriverException fromInvalidDriverExceptionXml(InputStream ex) 
                throws ParsingException {
            try {

                SAXBuilder builder = new SAXBuilder();
                Document document = builder.build(ex);
                Element rootElement = document.getRootElement();

                Element driverIdElement = rootElement.getChild("driverId", XML_NS);
                Long driverId = (Long.valueOf(driverIdElement.getText()));

                return new ClientInvalidDriverException(driverId);

            } catch (JDOMException | IOException e) {
                throw new ParsingException(e);
            } catch (Exception e) {
                throw new ParsingException(e);
            }
        }
}