package exceptions;

public class EmployerNotFoundException extends Exception {
    public EmployerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}
