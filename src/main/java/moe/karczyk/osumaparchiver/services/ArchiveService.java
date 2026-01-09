package moe.karczyk.osumaparchiver.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class ArchiveService {

    public void archiveFiles(List<Path> filePaths, Path archivePath) {
        try (var zos = new ZipOutputStream(Files.newOutputStream(archivePath))) {
            filePaths.forEach(p -> archivePath(p, zos));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void archivePath(Path path, ZipOutputStream zos) {
        try (var stream = Files.walk(path)) {
            stream.filter(p -> !Files.isDirectory(p))
                    .forEach(p -> {
                        var entry = new ZipEntry(path.getParent().relativize(p).toString().replace("\\", "/"));
                        try {
                            zos.putNextEntry(entry);
                            Files.copy(p, zos);
                            zos.closeEntry();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
