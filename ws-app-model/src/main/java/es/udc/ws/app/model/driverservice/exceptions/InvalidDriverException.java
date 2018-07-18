package es.udc.ws.app.model.driverservice.exceptions;

import java.util.Calendar;


@SuppressWarnings("serial")
public class InvalidDriverException extends Exception{

	private Long driverId;
    private Calendar expirationDate;

    public InvalidDriverException(Long driverId, Calendar expirationDate) {
        super("Driver with id=" + driverId + 
              " isn´t working now. He stopped working at "+ expirationDate);
        this.driverId = driverId;
        this.expirationDate = expirationDate;
    }

    public Long getDriverId() {
        return driverId;
    }

    public Calendar getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Calendar expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
    }
}
	

