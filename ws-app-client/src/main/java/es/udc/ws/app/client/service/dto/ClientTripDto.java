package es.udc.ws.app.client.service.dto;


public class ClientTripDto {

    private Long tripId;
    private Long driverId;
    private String origen;
    private String destino;
    private String user;
    private String creditCardNumber;
    private int valoracion = -1;


    public ClientTripDto(Long tripId, Long driverId, String origen, String destino, String user,
                         String creditCardNumber, int valoracion) {
        this.tripId = tripId;
        this.driverId = driverId;
        this.origen = origen;
        this.destino = destino;
        this.user = user;
        this.creditCardNumber = creditCardNumber;
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


    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
}
