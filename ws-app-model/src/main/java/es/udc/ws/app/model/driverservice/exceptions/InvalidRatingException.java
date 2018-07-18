package es.udc.ws.app.model.driverservice.exceptions;

@SuppressWarnings("serial")
public class InvalidRatingException extends Exception {

    private String userId;
    private int valoracion = -1;

    public InvalidRatingException(String userId) {
        super("User with id=\"" + userId + 
              "\" is not rating his trip");
        this.userId = userId;
    }
    
    public InvalidRatingException(String userId, int valoracion) {
        super("User with id=\"" + userId + 
                "\" has already rated this trip");
        this.userId = userId;
        this.valoracion = valoracion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }
}