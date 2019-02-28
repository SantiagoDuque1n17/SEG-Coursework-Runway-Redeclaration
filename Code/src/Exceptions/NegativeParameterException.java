package Exceptions;

public class NegativeParameterException extends Exception {
    public NegativeParameterException() {
        super("Invalid parameters; runway can't be redeclared.");
    }
    public NegativeParameterException(String msg) {
        super(msg);
    }
}
