package moe.karczyk.osumaparchiver;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Service
@RequiredArgsConstructor
@CommonsLog
public class OsuStuffValidationService {

    public static final String OSU_MAP_FILE_EXTENSION = ".osu";

    public record ValidationResult(boolean success, String message) {}

    public ValidationResult isMapsDirectory(Path path) {
        if (path == null || Files.notExists(path)) {
            return new ValidationResult(false, "Selected directory does not exist");
        }

        if (!Files.isDirectory(path)) {
            return new ValidationResult(false, "Selected path is not a directory");
        }

        try (var mapsDirs = Files.newDirectoryStream(path)) {
            for  (var mapDir : mapsDirs) {
                if (isSingleMapDirectory(mapDir)) {
                    return new ValidationResult(true, "");
                }
            }

        } catch (IOException e) {
            log.warn("Error while reading maps directory", e);
        }

        return new ValidationResult(false, "Selected directory is not a osu maps directory");

    }

    private boolean isSingleMapDirectory(Path path) {
        if (!Files.isDirectory(path)) {
            return false;
        }

        try (var mapFiles = Files.newDirectoryStream(path)) {
            for (var mapFile : mapFiles) {
                if (mapFile.toString().endsWith(OSU_MAP_FILE_EXTENSION)) {
                    return true;
                }
            }

        } catch (IOException e) {
            log.warn("Error while reading maps directory", e);
        }

        return false;
    }
}
