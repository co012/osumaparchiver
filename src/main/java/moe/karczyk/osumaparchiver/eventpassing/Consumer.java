package moe.karczyk.osumaparchiver.eventpassing;

public interface Consumer {
    void consume(final Event event);
}
