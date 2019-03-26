package nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions;

public class InvalidLoginException extends Exception {

    public InvalidLoginException() {
        super("The login details are not right.");
    }

    public InvalidLoginException(String message) {
        super(message);
    }
}
