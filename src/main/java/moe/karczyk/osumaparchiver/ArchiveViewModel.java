package moe.karczyk.osumaparchiver;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.services.PathValidationService;
import moe.karczyk.osumaparchiver.ui.UiCoordinator;
import moe.karczyk.osumaparchiver.ui.UiCoordinatorAware;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@Service
@CommonsLog
@RequiredArgsConstructor
public class ArchiveViewModel implements UiCoordinatorAware {

    private UiCoordinator uiCoordinator;

    private final PathValidationService pathValidationService;
    private final BeatmapSetService beatmapSetService;

    public final SimpleStringProperty archiveDirPath = new SimpleStringProperty("");
    public final SimpleStringProperty archiveDirErrorMsg = new SimpleStringProperty("");
    public final SimpleStringProperty archiveName = new SimpleStringProperty("");
    public final SimpleStringProperty archiveNameErrorMsg = new SimpleStringProperty("");
    public final SimpleStringProperty archivePathErrorMsg = new SimpleStringProperty("");
    public final SimpleBooleanProperty deleteArchivedFiles = new SimpleBooleanProperty(false);
    public final SimpleBooleanProperty isArchiving = new SimpleBooleanProperty(false);

    @Override
    public void setUiCoordinator(UiCoordinator uiCoordinator) {
        this.uiCoordinator = uiCoordinator;
    }

    public void setArchiveDirectory(File directory) {
        archiveDirPath.set(directory.toPath().toString());
        validateArchiveDirectory();
    }

    public void validateArchiveDirectory() {
        var result = pathValidationService.isDirectoryPath(archiveDirPath.get());
        archiveDirErrorMsg.set(result.msg());
        if (result.valid() && archiveNameErrorMsg.get().isBlank()) {
            validateArchivePath();
        }
    }

    public void validateArchiveName() {
        var result = pathValidationService.isFilename(archiveName.get());
        archiveNameErrorMsg.set(result.msg());
        if (result.valid() && archiveDirErrorMsg.get().isBlank()) {
            validateArchivePath();
        }
    }

    private void validateArchivePath() {
        var result = pathValidationService.isValidNewFilePath(archiveDirPath.get(), archiveName.get());
        archivePathErrorMsg.set(result.msg());
    }

    public void archiveMarkedBeatmapSets() {
        if (isArchiving.get()) return;
        isArchiving.set(true);
        var archivePath = Path.of(archiveDirPath.get(), archiveName.get());
        CompletableFuture<Void> archiveFuture;
        if (deleteArchivedFiles.get()) {
            archiveFuture = beatmapSetService.createArchiveFromAndRemoveMarkedBeatmapSets(archivePath);
        } else {
            archiveFuture = beatmapSetService.createArchiveFromMarkedBeatmapSets(archivePath);
        }

        archiveFuture.thenRun(() -> Platform.runLater(this::onArchiveCompleted));
    }


    private void onArchiveCompleted() {
        uiCoordinator.notifyArchiveCompleted();
        isArchiving.set(false);
    }

    public void refresh() {
        archiveNameErrorMsg.set("");
        archiveName.set("archive_%s.zip".formatted(createTimestamp()));
        validateArchivePath();
    }

    private String createTimestamp() {
        var date = LocalDateTime.now();
        return date.format(DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm-ss"));
    }
}
