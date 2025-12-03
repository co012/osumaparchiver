package moe.karczyk.osumaparchiver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import moe.karczyk.osumaparchiver.BeatmapSetRepository;
import moe.karczyk.osumaparchiver.models.BeatmapSet;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@CommonsLog
public class BeatmapSetService {

    private final FileAccessService fileAccessService;
    private final BeatmapService beatmapService;
    private final BeatmapSetRepository beatmapSetRepository;


    public void loadMapsFrom(Path mapsPath) {
        var mapDirs = fileAccessService.getDirectoriesInDirectory(mapsPath);
        for (Path mapDir : mapDirs) {
            var beatmaps = fileAccessService.getFilesInDirectory(mapDir).stream()
                    .filter(p -> p.toString().endsWith(".osu"))
                    .map(beatmapService::parseBeatmap)
                    .toList();
            beatmapService.saveBeatmaps(beatmaps);

            var beatmapSet =  BeatmapSet.builder()
                    .beatmaps(beatmaps)
                    .name(beatmaps.getFirst().getTitle())
                    .fullDirectoryPath(mapDir.toAbsolutePath().toString())
                    .directoryName(mapDir.getFileName().toString())
                    .build();
            beatmapSetRepository.save(beatmapSet);
        }

    }
}
