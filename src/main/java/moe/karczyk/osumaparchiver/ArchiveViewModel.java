package moe.karczyk.osumaparchiver;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.services.PathValidationService;
import moe.karczyk.osumaparchiver.ui.UiCoordinator;
import moe.karczyk.osumaparchiver.ui.UiCoordinatorAware;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class ArchiveViewModel implements UiCoordinatorAware {

    private UiCoordinator uiCoordinator;

    private final PathValidationService pathValidationService;

    public final SimpleStringProperty archiveDirPath = new SimpleStringProperty("");
    public final SimpleStringProperty archiveDirErrorMsg = new SimpleStringProperty("");
    public final SimpleStringProperty archiveName = new SimpleStringProperty("");
    public final SimpleStringProperty archiveNameErrorMsg = new SimpleStringProperty("");
    public final SimpleStringProperty archivePathErrorMsg = new SimpleStringProperty("");
    public final SimpleBooleanProperty deleteArchivedFiles = new SimpleBooleanProperty(false);

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

    public void onArchiveCompleted() {
        System.out.println(Path.of(archiveDirPath.get(), archiveName.get()).toAbsolutePath());
        uiCoordinator.notifyArchiveCompleted();
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
