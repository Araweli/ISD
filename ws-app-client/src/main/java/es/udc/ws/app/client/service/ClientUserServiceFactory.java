package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.ClientUserService;
import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class ClientUserServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "ClientUserServiceFactory.className";

    private static Class<ClientUserService> serviceClass = null;

    private ClientUserServiceFactory() {}


    @SuppressWarnings("unchecked")
    private synchronized static Class<ClientUserService> getServiceClass() {

        if (serviceClass == null) {
            try {
                String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
                serviceClass = (Class<ClientUserService>) Class.forName(serviceClassName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return serviceClass;
    }


    public static ClientUserService getService() {

        try {
            return (ClientUserService) getServiceClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }
}
