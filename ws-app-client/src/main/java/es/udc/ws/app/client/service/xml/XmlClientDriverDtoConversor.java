package es.udc.ws.app.client.service.xml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;

import es.udc.ws.app.client.service.dto.ClientDriverDto;
import es.udc.ws.util.xml.exceptions.ParsingException;

public class XmlClientDriverDtoConversor {

	public final static Namespace XML_NS = Namespace.getNamespace("http://ws.udc.es/drivers/xml");

	public static Document toXml(ClientDriverDto driver) throws IOException {

		Element driverElement = toJDOMElement(driver);

		return new Document(driverElement);
	}

	public static Element toJDOMElement(ClientDriverDto driver) {

		Element driverElement = new Element("driver", XML_NS);

		if (driver.getDriverId() != null) {
			Element identifierElement = new Element("driverId", XML_NS);
			identifierElement.setText(driver.getDriverId().toString());
			driverElement.addContent(identifierElement);
		}

		
		Element nameElement = new Element("name", XML_NS);
		nameElement.setText(driver.getName());
		driverElement.addContent(nameElement);
		
		Element cityElement = new Element("city", XML_NS);
		cityElement.setText(driver.getCity());
		driverElement.addContent(cityElement);
		
		Element carElement = new Element("car", XML_NS);
		carElement.setText(driver.getCar());
		driverElement.addContent(carElement);
		
		Element startTimeElement = new Element("startTime", XML_NS);
		startTimeElement.setText(Integer.toString(driver.getStartTime()));
		driverElement.addContent(startTimeElement);
		
		Element endTimeElement = new Element("endTime", XML_NS);
		endTimeElement.setText(Integer.toString(driver.getEndTime()));
		driverElement.addContent(endTimeElement);
		

		return driverElement;
	}

	public static ClientDriverDto toClientDriverDto(InputStream driverXml) throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(driverXml);
			Element rootElement = document.getRootElement();

			return toClientDriverDto(rootElement);
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	public static List<ClientDriverDto> toClientDriverDtos(InputStream driverXml) throws ParsingException {
		try {

			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(driverXml);
			Element rootElement = document.getRootElement();

			if (!"drivers".equalsIgnoreCase(rootElement.getName())) {
				throw new ParsingException("Unrecognized element '" + rootElement.getName() + "' ('drivers' expected)");
			}
			List<Element> children = rootElement.getChildren();
			List<ClientDriverDto> driverDtos = new ArrayList<>(children.size());
			for (int i = 0; i < children.size(); i++) {
				Element element = children.get(i);
				driverDtos.add(toClientDriverDto(element));
			}

			return driverDtos;
		} catch (ParsingException ex) {
			throw ex;
		} catch (Exception e) {
			throw new ParsingException(e);
		}
	}

	private static ClientDriverDto toClientDriverDto(Element driverElement)
			throws ParsingException, DataConversionException {

		if (!"driver".equals(driverElement.getName())) {
			throw new ParsingException("Unrecognized element '" + driverElement.getName() + "' ('driver' expected)");
		}

		Element identifierElement = driverElement.getChild("driverId", XML_NS);
		Long identifier = null;

		if (identifierElement != null) {
			identifier = Long.valueOf(identifierElement.getTextTrim());
		}

		String name = driverElement.getChildTextNormalize("name", XML_NS);

		String city = driverElement.getChildTextNormalize("city", XML_NS);
		
		String car = driverElement.getChildTextNormalize("car", XML_NS);

		int startTime = Integer.valueOf(driverElement.getChildTextNormalize("startTime", XML_NS));

		int endTime = Integer.valueOf(driverElement.getChildTextNormalize("endTime", XML_NS));

		float puntuacionmedia = Float.valueOf(driverElement.getChildTextNormalize("puntuacionmedia", XML_NS));

		return new ClientDriverDto(identifier,name, city, car, startTime, endTime, puntuacionmedia);
	}

}
