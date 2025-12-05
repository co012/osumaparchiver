package moe.karczyk.osumaparchiver.eventpassing;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class Producer {
    private final List<Consumer> consumers = new ArrayList<>();

    public synchronized void addConsumer(Consumer consumer) {
        consumers.add(consumer);
    }

    public void publish(Event event) {
        for (Consumer consumer : consumers) {
            consumer.consume(event);
        }
    }

}
