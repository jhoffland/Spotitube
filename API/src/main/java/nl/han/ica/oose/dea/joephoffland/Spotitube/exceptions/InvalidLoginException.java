package nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions;

public class InvalidLoginException extends Exception {

    public InvalidLoginException() {
        super("The authentication detail(s) are not right.");
    }

    public InvalidLoginException(String message) {
        super(message);
    }
}
