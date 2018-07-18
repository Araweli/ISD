package es.udc.ws.app.model.trip;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class SqlTripDaoFactory {


    private final static String CLASS_NAME_PARAMETER = "SqlTripDaoFactory.className";
    private static SqlTripDao dao = null;

    private SqlTripDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlTripDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlTripDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlTripDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
	
}
