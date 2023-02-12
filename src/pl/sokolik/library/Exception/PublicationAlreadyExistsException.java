package pl.sokolik.library.Exception;

public class PublicationAlreadyExistsException extends RuntimeException{
    public PublicationAlreadyExistsException(String message) {
        super(message);
    }
}
