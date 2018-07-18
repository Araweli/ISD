package es.udc.ws.app.client.service.dto;

public class ClientDriverDto {
	private Long driverId;
	private String name;
	private String city;
	private String car;
	private int startTime;
	private int endTime;
	private float puntuacionmedia = -1;


	public ClientDriverDto(Long driverId, String name, String city, String car, int startTime, int endTime) {
		this.driverId = driverId;
		this.name = name;
		this.city = city;
		this.car = car;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public ClientDriverDto(Long driverId, String name, String city, String car, int startTime,
                           int endTime, float puntuacionmedia) {
	    this(driverId,name,city,car,startTime,endTime);
	    this.puntuacionmedia = puntuacionmedia;
	}

	public Long getDriverId() {
		return driverId;
	}

	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCar() {
		return car;
	}

	public void setCar(String car) {
		this.car = car;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}
	
	public float getPuntuacionMedia() {
       return puntuacionmedia;
    }
	
	public void setPuntuacionMedia(float puntuacionmedia) {
       this.puntuacionmedia = puntuacionmedia;
	}

	@Override
    public String toString() {
        return "DriverDto [driverId=" + driverId + ", name=" + name
                + ", city=" + city + " car, " + car 
                + ", startTime=" + startTime + ", endTime=" + endTime + "]";
    }
}
