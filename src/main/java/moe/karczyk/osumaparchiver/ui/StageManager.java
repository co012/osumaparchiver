package moe.karczyk.osumaparchiver.ui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import moe.karczyk.osumaparchiver.eventpassing.Consumer;
import moe.karczyk.osumaparchiver.eventpassing.Event;

@RequiredArgsConstructor
@CommonsLog
public class StageManager implements Consumer {

    private final Stage bigPictureStage = new Stage(StageStyle.UNDECORATED);

    private final Stage mainStage;
    private final DirectorySelectionView directorySelectionView;
    private final ConfigView configView;
    private final BigPictureView bigPictureView;

    @Override
    public void consume(Event event) {
        if (Platform.isFxApplicationThread()) {
            handleEvent(event);
        } else {
            Platform.runLater(() -> handleEvent(event));
        }
    }

    private void setup() {
        bigPictureStage.setScene(new Scene(bigPictureView.root));
        bigPictureStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), bigPictureStage::hide);
    }

    public void run() {
        setup();
        showSelectDirectoryScene();
    }

    private void handleEvent(Event event) {
            switch (event) {
                case SONGS_LOADED -> showConfigScene();
                case BIG_PICTURE_TARGET_SELECTED -> openBigPicture();
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

    private void openBigPicture() {
        bigPictureStage.setFullScreen(true);
        bigPictureStage.show();
    }

}
