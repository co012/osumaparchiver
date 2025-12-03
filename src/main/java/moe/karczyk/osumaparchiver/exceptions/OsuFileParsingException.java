package moe.karczyk.osumaparchiver.exceptions;


public class OsuFileParsingException extends RuntimeException {
    public OsuFileParsingException(String message) {
        super(message);
    }

    public OsuFileParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
