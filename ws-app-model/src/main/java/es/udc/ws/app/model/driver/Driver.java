package es.udc.ws.app.model.driver;

import java.util.Calendar;

public class Driver {

	private Long driverId;
	private String name;
	private String city;
	private String car;
	private int startTime;
	private int endTime;
	private Calendar creationDate = Calendar.getInstance();
	private float puntuacionTotal = 0;
	private int numViajes = 0;

	public Driver(String name, String city, String car, int startTime, int endTime) {
		this.name = name;
		this.city = city;
		this.car = car;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Driver(Long driverId, String name, String city, String car, int startTime, int endTime) {
		this(name,city,car,startTime,endTime);
		this.driverId = driverId;
	}
	
	public Driver(Calendar creationDate, Long driverId, String name, String city, String car,
	       int startTime, int endTime) {
		this(driverId,name,city,car,startTime,endTime);
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
		}
	}
	
	
	public Driver(Calendar creationDate, Long driverId, String name, String city, String car,
	       int startTime, int endTime, float puntuacionTotal, int numviajes) {
        this(creationDate, driverId, name, city, car, startTime, endTime);
        this.puntuacionTotal = puntuacionTotal;
        this.numViajes = numviajes;
	}

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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
		this.endTime= endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Calendar creationDate) {
		this.creationDate = creationDate;
		if (creationDate != null) {
			this.creationDate.set(Calendar.MILLISECOND, 0);
		}
	}

	public float getPuntuacionTotal() {
		return puntuacionTotal;
	}

	public void setPuntuacionTotal(float valoracion) {
		this.puntuacionTotal = valoracion;
	}

	public int getNumViajes() {
		return numViajes;
	}

	public void sumarViaje() {
		this.numViajes++;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((car == null) ? 0 : car.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		result = prime * result + endTime;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + numViajes;
		result = prime * result + Float.floatToIntBits(puntuacionTotal);
		result = prime * result + startTime;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Driver other = (Driver) obj;
		if (car == null) {
			if (other.car != null)
				return false;
		} else if (!car.equals(other.car))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (driverId == null) {
			if (other.driverId != null)
				return false;
		} else if (!driverId.equals(other.driverId))
			return false;
		if (endTime != other.endTime)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numViajes != other.numViajes)
			return false;
		if (Float.floatToIntBits(puntuacionTotal) != Float.floatToIntBits(other.puntuacionTotal))
			return false;
		if (startTime!= other.startTime)
			return false;
		return true;
	}

}
