package moe.karczyk.osumaparchiver;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import org.springframework.stereotype.Component;

import java.nio.file.Path;


@Component
@RequiredArgsConstructor
public class ConfigViewModel {
    private final FileSelectionService fileSelectionService;
    private final OsuStuffValidationService osuStuffValidationService;

    @Delegate
    private final ConfigPresentationModel configPresentationModel = new ConfigPresentationModel();


    public void selectMapsDirectoryWithFileChooser(Stage stage) {
        fileSelectionService.selectDirectory(stage)
                .ifPresent(this::setOsuMapsPath);
    }

    private void setOsuMapsPath(Path mapsPath) {
        var result = osuStuffValidationService.isMapsDirectory(mapsPath);
        getIsMapsDirectoryValid().setValue(result.success());
        getMapsDirectoryErrorMsg().setValue(result.message());
        getMapsDirectory().setValue(mapsPath.toAbsolutePath().toString());
    }
}
