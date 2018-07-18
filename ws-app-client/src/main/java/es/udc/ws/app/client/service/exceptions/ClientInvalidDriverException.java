package es.udc.ws.app.client.service.exceptions;

@SuppressWarnings("serial")
public class ClientInvalidDriverException extends Exception {

	private Long driverId;

	public ClientInvalidDriverException(Long driverId) {
		super("Driver with id=\"" + driverId + "\" isn´t working now \"");
		this.driverId = driverId;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}
}