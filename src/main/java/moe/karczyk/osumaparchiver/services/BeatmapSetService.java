package moe.karczyk.osumaparchiver.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import moe.karczyk.osumaparchiver.models.BeatmapSet;
import moe.karczyk.osumaparchiver.repositories.BeatmapSetRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@CommonsLog
public class BeatmapSetService {

    private final FileAccessService fileAccessService;
    private final BeatmapService beatmapService;
    private final BeatmapSetRepository beatmapSetRepository;
    private final ArchiveService archiveService;

    public interface ProgressCallback {
        void updateProgress(long processedDirs, long total);
    }

    public void loadMapsFrom(Path mapsPath, ProgressCallback callback) {
        var mapDirs = fileAccessService.getDirectoriesInDirectory(mapsPath);
        long totalDirs = mapDirs.size();
        callback.updateProgress(0, totalDirs);

        var it = mapDirs.listIterator();
        while (it.hasNext()) {
            var mapDir = it.next();
            var beatmaps = fileAccessService.getFilesInDirectory(mapDir).stream()
                    .filter(p -> p.toString().endsWith(".osu"))
                    .map(beatmapService::parseBeatmap)
                    .toList();
            beatmapService.saveBeatmaps(beatmaps);

            if (beatmaps.isEmpty()) {
                log.warn("Empty BeatmapSet in : %s, skipping".formatted(mapDir.toAbsolutePath()));
                continue;
            }

            var beatmapSet =  BeatmapSet.builder()
                    .beatmaps(beatmaps)
                    .name(beatmaps.getFirst().getTitle())
                    .fullDirectoryPath(mapDir.toAbsolutePath().toString())
                    .directoryName(mapDir.getFileName().toString())
                    .build();
            beatmapSetRepository.save(beatmapSet);
            callback.updateProgress(it.nextIndex(), totalDirs);
        }
        callback.updateProgress(totalDirs, totalDirs);
    }

    public List<BeatmapSet> getBeatmapSetsPage(int pageIdx, int pageSize) {
        var pageInfo = PageRequest.of(pageIdx, pageSize);
        return beatmapSetRepository.findAll(pageInfo).toList();
    }

    public long getBeatmapSetsCount() {
        return beatmapSetRepository.count();
    }

    public Optional<BeatmapSet> findBeatmapSetWithId(long id) {
        return beatmapSetRepository.findById(id);
    }

    @Transactional
    public void changeArchiveSelectionStatus(List<Long> beatmapSetIds, boolean archive) {
        beatmapSetRepository.updateArchiveSelectionStatus(beatmapSetIds, archive);
    }

    @Transactional
    public void toggleArchiveSelectionForBeatmapSet(long beatmapSetId) {
        var beatmapSet = findBeatmapSetWithId(beatmapSetId).orElseThrow();
        changeArchiveSelectionStatus(List.of(beatmapSetId), !beatmapSet.isSelectedToArchive());
    }

    public Optional<BeatmapSet> findNext(long beatmapSet) {
        return beatmapSetRepository.findFirstByIdAfterOrderByIdAsc(beatmapSet);
    }

    public Optional<BeatmapSet> findPrevious(long beatmapSet) {
        return beatmapSetRepository.findFirstByIdBeforeOrderByIdDesc(beatmapSet);
    }

    @Async
    public CompletableFuture<Void> createArchiveFromMarkedBeatmapSets(Path archivePath) {
        var paths = beatmapSetRepository.findAllBySelectedToArchive(true)
                .stream()
                .map(BeatmapSet::getFullDirectoryPath)
                .map(Path::of)
                .toList();
        archiveService.archiveFiles(paths, archivePath);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @Transactional
    public CompletableFuture<Void> createArchiveFromAndRemoveMarkedBeatmapSets(Path archivePath) {
        var paths = beatmapSetRepository.findAllBySelectedToArchive(true)
                .stream()
                .map(BeatmapSet::getFullDirectoryPath)
                .map(Path::of)
                .toList();
        archiveService.archiveFiles(paths, archivePath);
        fileAccessService.deleteDirectories(paths);
        beatmapSetRepository.deleteBeatmapSetsBySelectedToArchive(true);
        return CompletableFuture.completedFuture(null);
    }

    public long getBeatmapSetCount() {
        return beatmapSetRepository.count();
    }

    public long getToArchiveBeatmapSetCount() {
        return beatmapSetRepository.countBySelectedToArchive(true);
    }
}
