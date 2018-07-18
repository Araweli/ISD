package es.udc.ws.app.model.trip;

import java.util.Calendar;

public class Trip {
	
	private Long tripId;
	private Long driverId;
	private String origen;
	private String destino;
	private String user;
	private String creditCardNumber;
	private Calendar reservationDate;
	private int valoracion = -1;

	/* Constructor punto 5 */
	public Trip(Long driverId, String origen, String destino, String user, String creditCardNumber) {
		this.driverId = driverId;
		this.origen = origen;
		this.destino = destino;
		this.user = user;
		this.creditCardNumber = creditCardNumber;
	}
	
	public Trip(int valoracion, Long driverId, String origen, String destino, String user, String creditCardNumber) {
		this(driverId, origen, destino, user, creditCardNumber);
		this.valoracion = valoracion;
	}
	
	public Trip(Calendar reservationDate, int valoracion, Long driverId, String origen, String destino, String user,
			String creditCardNumber) {
		this(valoracion, driverId, origen, destino, user, creditCardNumber);
        this.reservationDate = reservationDate;
	    if (reservationDate != null) {
            this.reservationDate.set(Calendar.MILLISECOND, 0);
	    }
	}

	public Trip ( Long tripId, Calendar reservationDate, int valoracion, Long driverId, String origen,
			String destino, String user, String creditCardNumber) {
		this(reservationDate,valoracion, driverId, origen, destino, user, creditCardNumber);
		this.tripId = tripId;
	}
	
	public Trip(Long tripId,Long driverId,String origen, String destino, String user,int valoracion){
		this.tripId = tripId;
		this.driverId = driverId;
		this.origen = origen;
		this.destino = destino;
		this.user = user;
		this.valoracion = valoracion;
		}

	public Long getTripId() {
		return tripId;
	}

	public Long getDriverId() {
		return driverId;
	}
	
	public void setDriverId(Long driverId) {
		this.driverId = driverId;
	}

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getDestino() {
		return destino;
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getCreditCardNumber() {
		return creditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		this.creditCardNumber = creditCardNumber;
	}

	public Calendar getReservationDate() {
		return reservationDate;
	}


	public void setReservationDate(Calendar reservationDate) {
        this.reservationDate = reservationDate;
        if (reservationDate != null) {
            this.reservationDate.set(Calendar.MILLISECOND, 0);
        }
	}
        
	public int getValoracion() {
		return valoracion;
	}

	public void setValoracion(int valoracion) {
		this.valoracion = valoracion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((creditCardNumber == null) ? 0 : creditCardNumber.hashCode());
		result = prime * result + ((destino == null) ? 0 : destino.hashCode());
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		result = prime * result + ((origen == null) ? 0 : origen.hashCode());
		result = prime * result + ((reservationDate == null) ? 0 : reservationDate.hashCode());
		result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		result = prime * result + Float.floatToIntBits(valoracion);
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
		Trip other = (Trip) obj;
		if (creditCardNumber == null) {
			if (other.creditCardNumber != null)
				return false;
		} else if (!creditCardNumber.equals(other.creditCardNumber))
			return false;
		if (destino == null) {
			if (other.destino != null)
				return false;
		} else if (!destino.equals(other.destino))
			return false;
		if (driverId == null) {
			if (other.driverId != null)
				return false;
		} else if (!driverId.equals(other.driverId))
			return false;
		if (origen == null) {
			if (other.origen != null)
				return false;
		} else if (!origen.equals(other.origen))
			return false;
		if (reservationDate == null) {
			if (other.reservationDate != null)
				return false;
		} else if (!reservationDate.equals(other.reservationDate))
			return false;
		if (tripId == null) {
			if (other.tripId != null)
				return false;
		} else if (!tripId.equals(other.tripId))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		if (Float.floatToIntBits(valoracion) != Float.floatToIntBits(other.valoracion))
			return false;
		return true;
	}

	
}
