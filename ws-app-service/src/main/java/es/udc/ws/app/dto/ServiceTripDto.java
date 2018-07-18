package es.udc.ws.app.dto;

public class ServiceTripDto {
    
    private Long tripId;
    private Long driverId;
    private String origen;
    private String destino;
    private String user;
//  private Calendar reservationDate;
    private String creditCardNumber;
    private int valoracion = -1;


    public ServiceTripDto(Long tripId, Long driverId, String origen, String destino, String user, String creditCardNumber) {
        this.tripId = tripId;
        this.driverId = driverId;
        this.origen = origen;
        this.destino = destino;
        this.user = user;
        this.creditCardNumber = creditCardNumber;
    }

    public ServiceTripDto(Long tripId, Long driverId, String origen, String destino, String user, 
                          String creditCardNumber, int valoracion){
        this(tripId, driverId, origen, destino, user, creditCardNumber);
        this.valoracion = valoracion;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
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
    
    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
    
}