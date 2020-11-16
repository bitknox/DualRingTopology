package dk.johanf.dualring;

public class UnknownRequestException extends RuntimeException {
    private static final long serialVersionUID = 4523936909224367038L;
    private String message;

    public UnknownRequestException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
