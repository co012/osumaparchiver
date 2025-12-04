package moe.karczyk.osumaparchiver.services;

import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.BeatmapRepository;
import moe.karczyk.osumaparchiver.models.Beatmap;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class BeatmapService {

    private final FileAccessService fileAccessService;
    private final BeatmapRepository beatmapRepository;



    public Beatmap parseBeatmap(Path beatmapPath) {
        var lines = fileAccessService.getLines(beatmapPath).iterator();
        var beatmapBuilder = Beatmap.builder();

        var section = "";
        var isKeyValue = false;
        while (lines.hasNext()) {
            var line = lines.next().trim();

            if (line.isBlank() || line.startsWith("//")) {
                continue;
            }

            if (line.startsWith("[")) {
                section = line.toLowerCase();
                isKeyValue = Set.of("[general]", "[metadata]").contains(section);
                continue;
            }


           if (isKeyValue) {
               parseKeyValueLine(line, beatmapBuilder);
               continue;
           }

           if (section.equals("[events]")) {
               var splits = line.split(",");
               if (splits.length < 5 || !splits[0].trim().equals("0") || !splits[1].trim().equals("0")) {
                   continue;
               }
               var background = splits[2].trim();
               if (background.startsWith("\"")) {
                   background = background.replaceAll("\"", "");
               }
               beatmapBuilder.backgroundFilename(background);
           }

        }

        return beatmapBuilder.build();
    }

    public void saveBeatmaps(List<Beatmap> beatmaps) {
        beatmapRepository.saveAll(beatmaps);
    }

    private void parseKeyValueLine(String line, Beatmap.BeatmapBuilder beatmapBuilder) {
        var keyValue = line.split(":", 2);
        if (keyValue.length != 2) {
            return;
        }

        switch (keyValue[0].toLowerCase()) {
            // [General]
            case "audiofilename" -> beatmapBuilder.audioFilename(keyValue[1].trim());
            case "previewtime" -> beatmapBuilder.previewTime(Long.parseLong(keyValue[1].trim()));
            // [Metadata]
            case "title" -> beatmapBuilder.title(keyValue[1].trim());
            case "titleunicode" -> beatmapBuilder.titleOriginal(keyValue[1].trim());
            case "artist" -> beatmapBuilder.artist(keyValue[1].trim());
            case "artistunicode" -> beatmapBuilder.artistOriginal(keyValue[1].trim());
            case "creator" -> beatmapBuilder.creator(keyValue[1].trim());
            case "version" -> beatmapBuilder.version(keyValue[1].trim());

        }
    }

}
