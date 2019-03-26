package nl.han.ica.oose.dea.joephoffland.Spotitube.exceptions;

public class InternalServerErrorException extends Exception {
    public InternalServerErrorException() {
        super("Something went wrong on the server.");
    }

    public InternalServerErrorException(String message) {
        super(message);
    }
}
