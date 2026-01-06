package moe.karczyk.osumaparchiver.services;

import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Service
public class PathValidationService {
    public record Result(boolean valid, String msg) {
        private static Result ok() {
            return new Result(true, "");
        }

        private static Result err(String msg) {
            return new Result(false, msg);
        }
    }

    public Result isDirectoryPath(String text) {
        Path path;
        try {
            path = Path.of(text);
        } catch (InvalidPathException e) {
            return Result.err("Invalid path: " + e.getReason());
        }
        if (!Files.exists(path)) {
            return Result.err("Directory does not exists");
        } else if (!Files.isDirectory(path)) {
            return Result.err("Not a directory");
        }
        return Result.ok();
    }

    public Result isFilename(String text) {
        if (text.isBlank()) {
            return Result.err("Filename can't be blank");
        }
        Path path;
        try {
            path = Path.of(text);
        } catch (InvalidPathException e) {
            return Result.err("Invalid name: " + e.getReason());
        }
        if (path.getNameCount() > 1) {
            return Result.err("Not a file name");
        }
        return Result.ok();
    }

    public Result isValidNewFilePath(String dirText, String fileText) {
        Path path;
        try {
            path = Path.of(dirText, fileText);
        } catch (InvalidPathException e) {
            return Result.err("Invalid path: " + e.getReason());
        }
        if (Files.exists(path)) {
            return Result.err("File already exists");
        }
        return Result.ok();
    }

}
