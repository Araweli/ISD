package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.ClientDriverService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientDriverServiceFactory {

	private final static String CLASS_NAME_PARAMETER = "ClientDriverServiceFactory.className";

	private static Class<ClientDriverService> serviceClass = null;

	private ClientDriverServiceFactory() {
	}

	@SuppressWarnings("unchecked")
	private synchronized static Class<ClientDriverService> getServiceClass() {

		if (serviceClass == null) {
			try {
				String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
				serviceClass = (Class<ClientDriverService>) Class.forName(serviceClassName);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return serviceClass;

	}

	public static ClientDriverService getService() {

		try {
			return (ClientDriverService) getServiceClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}
}
