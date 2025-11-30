package moe.karczyk.osumaparchiver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@CommonsLog
public class MapsService {

    private final FileAccessService fileAccessService;


    public void loadMapsFrom(Path mapsPath) {
        var maps = new ArrayList<>();
        var mapDirs = fileAccessService.getDirectoriesInDirectory(mapsPath);
        for (Path mapDir : mapDirs) {
            var mapFiles = fileAccessService.getFilesInDirectory(mapDir).stream()
                    .filter(p -> p.toString().endsWith(".osu"))
                    .map(this::parseMapFile)
                    .toList();
            maps.add(mapFiles);
        }

        log.info(maps);
    }

    private String parseMapFile(Path mapFile) {
        try (var mapFileLines = fileAccessService.getLines(mapFile)) {
            var iterator = mapFileLines.iterator();

            while (iterator.hasNext()) {
                var line = iterator.next().trim();
                if (line.startsWith("[Metadata]")) {
                    break;
                }

            }

            while (iterator.hasNext() ) {
                var line = iterator.next().trim();
                if (line.trim().startsWith("[")) {
                    break;
                }
                var keyValuePair = line.split(":", 2);
                if (keyValuePair.length != 2) {
                    continue;
                }

                if (keyValuePair[0].equals("Title")) {
                    return keyValuePair[1].trim();
                }
            }

        }

        return null;
    }
}
