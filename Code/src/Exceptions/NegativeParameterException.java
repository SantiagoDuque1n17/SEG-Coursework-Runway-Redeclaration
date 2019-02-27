package RunwayRedeclaration.Exceptions;

public class NegativeParameterException extends Exception {
    public NegativeParameterException("Invalid parameters; runway can't be redeclared.") {
        super();
    }
    public NegativeParameterException(String msg) {
        super(msg);
    }
}
