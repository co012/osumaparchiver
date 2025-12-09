package moe.karczyk.osumaparchiver;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.RequiredArgsConstructor;
import moe.karczyk.osumaparchiver.services.BeatmapSetService;
import moe.karczyk.osumaparchiver.ui.models.BeatmapSetPresent;
import org.springframework.stereotype.Component;

import java.util.List;


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
                .map(map -> BeatmapSetPresent.builder()
                        .id(map.getId())
                        .name(map.getName())
                        .archive(map.isSelectedToArchive())
                        .build())
                .forEach(visibleBeatmapSets::add);

    }

    public void changeBeatmapSetsArchiveStatus(List<BeatmapSetPresent> beatmapSets, boolean archive) {
        var selectedIds = beatmapSets.stream()
                .map(BeatmapSetPresent::id)
                .toList();
        beatmapSetService.changeArchiveSelectionStatus(selectedIds, archive);
    }


}
