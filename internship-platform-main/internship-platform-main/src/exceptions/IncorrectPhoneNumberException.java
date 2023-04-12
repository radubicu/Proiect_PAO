package exceptions;

public class IncorrectPhoneNumberException extends Exception {
    public IncorrectPhoneNumberException(String errorMessage) {
        super(errorMessage);
    }
}
