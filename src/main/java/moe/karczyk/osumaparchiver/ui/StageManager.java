package moe.karczyk.osumaparchiver.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import moe.karczyk.osumaparchiver.eventpassing.Consumer;
import moe.karczyk.osumaparchiver.eventpassing.Event;

import java.util.concurrent.ArrayBlockingQueue;

@RequiredArgsConstructor
@CommonsLog
public class StageManager implements Consumer {

    private final ArrayBlockingQueue<Event> eventQueue = new ArrayBlockingQueue<>(5);

    private final Stage mainStage;
    private final DirectorySelectionView directorySelectionView;
    private final ConfigView configView;

    @Override
    public void consume(Event event) {
        try {
            eventQueue.add(event);
        } catch (IllegalStateException e) {
            log.error("Event queue is full");
        }
    }

    public void run() {
        showSelectDirectoryScene();
        var eventReceiverThread = new Thread(this::handleEvent, "eventReceiverThread");
        eventReceiverThread.start();
    }

    private void handleEvent() {
        while (true) {
            Event event;
            try {
                event = eventQueue.take();
            } catch (InterruptedException e) {
                log.warn("Event queue has bean interrupted");
                break;
            }

            switch (event) {
                case SONGS_LOADED -> Platform.runLater(this::showConfigScene);
                case SHUTDOWN -> {
                    Platform.runLater(mainStage::close);
                    return;
                }
            }
        }
    }

    private void showSelectDirectoryScene() {
        var scene = new Scene(directorySelectionView.getRoot());
        mainStage.setScene(scene);
        mainStage.show();
    }

    private void showConfigScene() {
        mainStage.hide();
        var scene = new Scene(configView.root);
        mainStage.setScene(scene);
        mainStage.show();
    }

}
