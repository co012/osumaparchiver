package moe.karczyk.osumaparchiver;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.eventpassing.Event;
import moe.karczyk.osumaparchiver.eventpassing.Producer;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.services.OsuStuffValidationService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class DirectorySelectionViewModel {

    private final OsuStuffValidationService osuStuffValidationService;
    private final BeatmapSetService beatmapSetService;
    private final Producer producer;

    public final SimpleStringProperty songsDirPath = new  SimpleStringProperty();
    public final SimpleStringProperty infoText  = new  SimpleStringProperty();
    public final SimpleBooleanProperty isPathValid = new  SimpleBooleanProperty(false);
    public final SimpleBooleanProperty isLoadingMaps = new SimpleBooleanProperty(false);
    public final SimpleDoubleProperty loadingProgress = new SimpleDoubleProperty(0);


    public void validateSelectedPath(String path) {
        if (path == null || path.isBlank()) {
            isPathValid.set(false);
            return;
        }

        Path osuSongsDirPath;
        try {
            osuSongsDirPath = Path.of(path);
        } catch (InvalidPathException e) {
            isPathValid.set(false);
            infoText.setValue(e.getMessage());
            return;
        }

        var validationResult = osuStuffValidationService.isMapsDirectory(osuSongsDirPath);

        if (!validationResult.success()) {
            isPathValid.set(false);
            infoText.set(validationResult.message());
            return;
        }

        isPathValid.set(true);
        infoText.set("");
    }

    public void processSelectedPath(@NonNull File path) {
        Path osuSongsDirPath;
        try {
            osuSongsDirPath = path.toPath();
        } catch (InvalidPathException e) {
            isPathValid.set(false);
            infoText.setValue(e.getMessage());
            return;
        }
        songsDirPath.set(osuSongsDirPath.toAbsolutePath().toString());

        var validationResult = osuStuffValidationService.isMapsDirectory(osuSongsDirPath);

        if (!validationResult.success()) {
            isPathValid.set(false);
            infoText.set(validationResult.message());
            return;
        }

        isPathValid.set(true);
        infoText.set("");
    }

    public void loadMapsFromSelectedPath() {
        var path = Paths.get(songsDirPath.getValue());
        var loadingTask = createLoadingMapsTask(path);
        loadingProgress.bind(loadingTask.progressProperty());
        isLoadingMaps.bind(loadingTask.runningProperty());
        loadingTask.setOnSucceeded(_ -> producer.publish(Event.SONGS_LOADED));
        var loadingThread = new Thread(loadingTask);
        loadingThread.setDaemon(true);
        loadingThread.start();
    }

    private Task<Void> createLoadingMapsTask(Path path) {
        return new Task<>() {
            @Override
            protected Void call() {
                beatmapSetService.loadMapsFrom(path, this::updateProgress);
                return null;
            }
        };
    }





}
