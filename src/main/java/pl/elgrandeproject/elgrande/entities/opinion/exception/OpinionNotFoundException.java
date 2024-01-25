package pl.elgrandeproject.elgrande.entities.opinion.exception;

public class OpinionNotFoundException extends  RuntimeException{
    public OpinionNotFoundException(String message) {
        super(message);
    }
}
