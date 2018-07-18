package es.udc.ws.app.model.driver;

import es.udc.ws.util.configuration.ConfigurationParametersManager;


public class SqlDriverDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlDriverDaoFactory.className";
    private static SqlDriverDao dao = null;

    private SqlDriverDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlDriverDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlDriverDao) daoClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlDriverDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
