package es.udc.ws.app.client.service.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.dto.ClientTripDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientTripDtoConversor {

    public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/trips/xml");

    public static Document toXml(ClientTripDto trip) throws IOException {

        Element tripElement = toJDOMElement(trip);

        return new Document(tripElement);
    }

    public static Element toJDOMElement(ClientTripDto trip) {

        Element tripElement = new Element("trip", XML_NS);

        if (trip.getTripId() != null) {
            Element identifierElement = new Element("tripId", XML_NS);
            identifierElement.setText(trip.getTripId().toString());
            tripElement.addContent(identifierElement);
        }
        
        Element driverIdElement = new Element("driverId", XML_NS);
        driverIdElement.setText(trip.getDriverId().toString());
        tripElement.addContent(driverIdElement);
        
        Element origenElement = new Element("origen", XML_NS);
        origenElement.setText(trip.getOrigen());
        tripElement.addContent(origenElement);
        
        Element destinoElement = new Element("destino", XML_NS);
        destinoElement.setText(trip.getDestino());
        tripElement.addContent(destinoElement);
        
        Element userElement = new Element("usuario", XML_NS);
        userElement.setText(trip.getUser());
        tripElement.addContent(userElement);
        
        Element creditCardElement = new Element("creditCard", XML_NS);
        creditCardElement.setText(trip.getCreditCardNumber());
        tripElement.addContent(creditCardElement);
        

        return tripElement;
    }

    public static ClientTripDto toClientTripDto(InputStream tripXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(tripXml);
            Element rootElement = document.getRootElement();

            return toClientTripDto(rootElement);
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientTripDto> toClientTripDtos(InputStream tripXml) throws ParsingException {
        try {

            SAXBuilder builder = new SAXBuilder();
            Document document = builder.build(tripXml);
            Element rootElement = document.getRootElement();

            if (!"trips".equalsIgnoreCase(rootElement.getName())) {
                throw new ParsingException("Unrecognized element '" + rootElement.getName() + "' ('trips' expected)");
            }
            List<Element> children = rootElement.getChildren();
            List<ClientTripDto> tripDtos = new ArrayList<>(children.size());
            for (int i = 0; i < children.size(); i++) {
                Element element = children.get(i);
                tripDtos.add(toClientTripDto(element));
            }

            return tripDtos;
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientTripDto toClientTripDto(Element tripElement)
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

        String usuario = tripElement.getChildTextNormalize("usuario", XML_NS);


        Element creditCardElement = tripElement.getChild("creditCard", XML_NS);
        String creditcardNumber = null;

        if (creditCardElement != null) {
            creditcardNumber = creditCardElement.getTextNormalize();
        }

        int valoracion = Integer.valueOf(tripElement.getChildTextNormalize("valoracion", XML_NS)).intValue();

        return new ClientTripDto(identifier, driverId, origen, destino, usuario, creditcardNumber,
                                 valoracion);
    }

}
