package moe.karczyk.osumaparchiver.services;


import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
public class FileAccessService {

    public List<Path> getDirectoriesInDirectory(Path path) {
        var dirs = new ArrayList<Path>();
        try (var dirStream = Files.newDirectoryStream(path)) {
            for (var dir : dirStream) {
                if (Files.isDirectory(dir)) {
                    dirs.add(dir);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return dirs;
    }

    public List<Path> getFilesInDirectory(Path path) {
        var files = new ArrayList<Path>();
        try (var dirStream = Files.newDirectoryStream(path)) {
            for (var file : dirStream) {
                if (!Files.isDirectory(file)) {
                    files.add(file);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    public Stream<String> getLines(Path path) {
        try {
            return Files.lines(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
