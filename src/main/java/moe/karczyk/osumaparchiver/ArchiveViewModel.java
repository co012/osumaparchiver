package moe.karczyk.osumaparchiver;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.ui.UiCoordinator;
import moe.karczyk.osumaparchiver.ui.UiCoordinatorAware;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ArchiveViewModel implements UiCoordinatorAware {

    private UiCoordinator uiCoordinator;

    // TODO: check if file already exists
    public final SimpleStringProperty archivePath = new SimpleStringProperty("./archive.zip");
    public final SimpleStringProperty errorMsg = new SimpleStringProperty("");
    public final SimpleBooleanProperty deleteArchivedFiles = new SimpleBooleanProperty(false);

    @Override
    public void setUiCoordinator(UiCoordinator uiCoordinator) {
        this.uiCoordinator = uiCoordinator;
    }

    public void setArchiveDirectory(File directory) {
        errorMsg.set("");
        if (!directory.isDirectory()) return;
        var archiveName = Path.of(archivePath.get()).getFileName();
        if (archiveName == null)
            archiveName = Path.of("archive.zip");

        Path path = directory.toPath().resolve(archiveName);
        archivePath.set(path.toString());


        if (Files.exists(path)) {
            errorMsg.set("Invalid path: File already exist");
        }
    }

    public void validateArchivePath() {
        errorMsg.set("");
        Path path;
        try {
            path = Path.of(archivePath.get());
        } catch (InvalidPathException e) {
            errorMsg.set("Invalid path: " + e.getReason());
            return;
        }


        if (Files.isDirectory(path)) {
            errorMsg.set("Selected path points to a directory");
        } else if (Files.exists(path)) {
            errorMsg.set("Selected path points to an existing file");
        }
    }

    public void onArchiveCompleted() {
        uiCoordinator.notifyArchiveCompleted();
    }
}
