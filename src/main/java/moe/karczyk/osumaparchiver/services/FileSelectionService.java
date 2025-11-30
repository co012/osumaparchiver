package moe.karczyk.osumaparchiver.services;

import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.File;
import java.nio.file.Path;
import java.util.Optional;


@RequiredArgsConstructor
@Component
public class FileSelectionService {
    public Optional<Path> selectDirectory(Stage stage) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Directory");
        File selected =  directoryChooser.showDialog(stage);
        return selected == null ? Optional.empty() : Optional.of(Path.of(selected.getAbsolutePath()));
    }
}
