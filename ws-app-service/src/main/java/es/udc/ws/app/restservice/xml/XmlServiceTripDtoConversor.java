package es.udc.ws.app.restservice.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.dto.ServiceDriverDto;
import es.udc.ws.app.dto.ServiceTripDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlServiceTripDtoConversor {
	
	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/trips/xml");

    public static Document toXml(ServiceTripDto trip) throws IOException {

        Element tripElement = toJDOMElement(trip);

        return new Document(tripElement);
    }



	public static Document toXml(List<ServiceTripDto> trip) throws IOException {
	
	    Element tripsElement = new Element("trips", XML_NS);
	    for (int i = 0; i < trip.size(); i++) {
	        ServiceTripDto xmlTripDto = trip.get(i);
	        Element tripElement = toJDOMElement(xmlTripDto);
	        tripsElement.addContent(tripElement);
	    }
	
	    return new Document(tripsElement);
	}
	
	
	public static Element toJDOMElement(ServiceTripDto trip) {
	
	    Element tripElement = new Element("trip", XML_NS);
	
	    if (trip.getTripId() != null) {
	        Element identifierElement = new Element("tripId", XML_NS);
	        identifierElement.setText(trip.getTripId().toString());
	        tripElement.addContent(identifierElement);
	    }
	    
	    Element identifierElement = new Element("driverId", XML_NS);
        identifierElement.setText(trip.getDriverId().toString());
        tripElement.addContent(identifierElement);
        
	    Element origenElement = new Element("origen", XML_NS);
	    origenElement.setText(trip.getOrigen());
	    tripElement.addContent(origenElement);
	    
	    Element destinoElement = new Element("destino", XML_NS);
	    destinoElement.setText(trip.getDestino());
	    tripElement.addContent(destinoElement);
	    
	    Element userElement = new Element("usuario", XML_NS);
	    userElement.setText(trip.getUser());
	    tripElement.addContent(userElement);

        if (trip.getCreditCardNumber() != null) {
            Element creditCardElement = new Element("creditCard", XML_NS);
            creditCardElement.setText(trip.getCreditCardNumber());
            tripElement.addContent(creditCardElement); 
        }

        Element ratingElement = new Element("valoracion", XML_NS);
        ratingElement.setText(Integer.valueOf(trip.getValoracion()).toString());
        tripElement.addContent(ratingElement);

	    return tripElement;
	}

	
    public static ServiceTripDto toServiceTripDto(InputStream tripXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(tripXml);
            Element rootElement = document.getRootElement();

            return toServiceTripDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    
    private static ServiceTripDto toServiceTripDto(Element tripElement)
            throws ParsingException, DataConversionException {

        if (!"trip".equals(tripElement.getName())) {
            throw new ParsingException("Unrecognized element '" + tripElement.getName() + "' ('trip' expected)");
        }

        Element identifierElement = tripElement.getChild("tripId", XML_NS);
        Long identifier = null;

        if (identifierElement != null) {
            identifier = Long.valueOf(identifierElement.getTextTrim());
        }

        Long driverId = Long.valueOf(tripElement.getChildTextNormalize("driverId", XML_NS));
        
        String origen = tripElement.getChildTextNormalize("origen", XML_NS);
        
        String destino = tripElement.getChildTextNormalize("destino", XML_NS);
        
        String user = tripElement.getChildTextNormalize("usuario", XML_NS);
        
        String creditCard = tripElement.getChildTextNormalize("creditCard", XML_NS);
        
        if (creditCard != null)
            System.out.println("creditCardNumber tag != NULL, value: " + creditCard);

        return new ServiceTripDto(identifier, driverId, origen, destino, user, creditCard);
    }
}