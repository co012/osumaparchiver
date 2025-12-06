package moe.karczyk.osumaparchiver.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import moe.karczyk.osumaparchiver.eventpassing.Consumer;
import moe.karczyk.osumaparchiver.eventpassing.Event;

@RequiredArgsConstructor
@CommonsLog
public class StageManager implements Consumer {

    private final Stage mainStage;
    private final DirectorySelectionView directorySelectionView;
    private final ConfigView configView;

    @Override
    public void consume(Event event) {
        if (Platform.isFxApplicationThread()) {
            handleEvent(event);
        } else {
            Platform.runLater(() -> handleEvent(event));
        }
    }

    public void run() {
        showSelectDirectoryScene();
    }

    private void handleEvent(Event event) {
            switch (event) {
                case SONGS_LOADED -> Platform.runLater(this::showConfigScene);
                case SHUTDOWN -> {
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
