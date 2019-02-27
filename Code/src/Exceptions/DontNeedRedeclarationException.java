public class DontNeedRedeclarationException extends Exception {
    public DontNeedRedeclarationException() {
        super("Obstacle out of the way; redeclaration unnecessary.");
    }
    public DontNeedRedeclarationException(String msg) {
        super(msg);
    }
}
