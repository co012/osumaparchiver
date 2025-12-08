package moe.karczyk.osumaparchiver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class ConfigViewModel {
    private final BeatmapSetService beatmapSetService;

    public final ObservableList<BeatmapSetPresent> visibleBeatmapSets = FXCollections.observableArrayList();


    public int getPageCount(int pageSize) {
        return Math.toIntExact(Math.ceilDiv(beatmapSetService.getBeatmapSetsCount(), pageSize));

    }

    public void changePage(int pageIdx, int pageSize) {
        visibleBeatmapSets.clear();
        beatmapSetService.getBeatmapSetsPage(pageIdx, pageSize)
                .stream()
                .map(map -> new BeatmapSetPresent(map.getName()))
                .forEach(visibleBeatmapSets::add);

    }


}
