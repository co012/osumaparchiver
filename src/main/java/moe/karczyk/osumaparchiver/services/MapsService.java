package moe.karczyk.osumaparchiver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import moe.karczyk.osumaparchiver.BeatmapRepository;
import moe.karczyk.osumaparchiver.BeatmapSetRepository;
import moe.karczyk.osumaparchiver.models.Beatmap;
import moe.karczyk.osumaparchiver.models.BeatmapSet;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@CommonsLog
public class MapsService {

    private final FileAccessService fileAccessService;
    private final BeatmapSetRepository beatmapSetRepository;
    private final BeatmapRepository beatmapRepository;


    public void loadMapsFrom(Path mapsPath) {
        var mapDirs = fileAccessService.getDirectoriesInDirectory(mapsPath);
        for (Path mapDir : mapDirs) {
            var mapFiles = fileAccessService.getFilesInDirectory(mapDir).stream()
                    .filter(p -> p.toString().endsWith(".osu"))
                    .map(this::parseMapFile)
                    .map(name -> Beatmap.builder().title(name).build())
                    .toList();
            beatmapRepository.saveAll(mapFiles);

            var beatmapSet =  BeatmapSet.builder()
                    .beatmaps(mapFiles)
                    .fullDirectoryPath(mapDir.toAbsolutePath().toString())
                    .directoryName(mapDir.getFileName().toString())
                    .build();
            beatmapSetRepository.save(beatmapSet);
        }




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
