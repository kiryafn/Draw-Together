package domain;

public class BannedWordExeption extends Exception{
    public BannedWordExeption(String message) {
        super(message);
    }
}
