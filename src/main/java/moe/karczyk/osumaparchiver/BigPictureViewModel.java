package moe.karczyk.osumaparchiver;

import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.services.UrlEncodingService;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class BigPictureViewModel {
    private final BeatmapSetService beatmapSetService;
    private final UrlEncodingService urlEncodingService;

    public final SongsPlayer songsPlayer = new SongsPlayer();

    // TODO: Search by beatmap id
    public void playBeatmap(long beatmapSetId) {
        var beatmapSet = beatmapSetService.findBeatmapSetWithId(beatmapSetId).orElseThrow();
        var songUrl = urlEncodingService.encodePath(Path.of(beatmapSet.getFullDirectoryPath(), beatmapSet.getBeatmaps().getFirst().getAudioFilename()));
        var startTime = beatmapSet.getBeatmaps().getFirst().getPreviewTime();
        songsPlayer.play(songUrl, startTime);
    }
}
