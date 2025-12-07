package moe.karczyk.osumaparchiver;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.services.OsuStuffValidationService;
import moe.karczyk.osumaparchiver.ui.ConfigPresentationModel;
import org.springframework.stereotype.Component;

import java.nio.file.Path;


@Component
@RequiredArgsConstructor
public class ConfigViewModel {
    private final OsuStuffValidationService osuStuffValidationService;
    private final BeatmapSetService beatmapSetService;

    @Delegate
    private final ConfigPresentationModel configPresentationModel = new ConfigPresentationModel();


    private void setOsuMapsPath(Path mapsPath) {

    }
}
