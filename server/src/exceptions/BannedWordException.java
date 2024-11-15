package exceptions;

public class BannedWordException extends Exception{
    public BannedWordException(String message) {
        super(message);
    }
}
