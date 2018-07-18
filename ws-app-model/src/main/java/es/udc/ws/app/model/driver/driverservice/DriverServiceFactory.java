package es.udc.ws.app.model.driver.driverservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class DriverServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "DriverServiceFactory.className";
    private static DriverService service = null;

    private DriverServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static DriverService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            System.out.println(serviceClassName);
            Class serviceClass = Class.forName(serviceClassName);
            return (DriverService) serviceClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static DriverService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
