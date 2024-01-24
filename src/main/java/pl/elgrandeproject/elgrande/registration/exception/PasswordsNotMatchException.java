package pl.elgrandeproject.elgrande.registration.exception;

public class PasswordsNotMatchException extends RuntimeException{
    public PasswordsNotMatchException(String message) {
        super(message);
    }
}
