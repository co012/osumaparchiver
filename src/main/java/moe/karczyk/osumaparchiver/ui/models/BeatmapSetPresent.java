package moe.karczyk.osumaparchiver.ui.models;

import lombok.Builder;

@Builder
public record BeatmapSetPresent(long id, String name, String artists, String creators, int beatmapCount,
                                boolean archive, Long nextId, Long previousId) {
    public static final BeatmapSetPresent EMPTY = new BeatmapSetPresent(-1, "N/A", "N/A", "N/A", 0, false, null, null);

    public boolean hasNext() {
        return nextId != null;
    }

    public boolean hasPrevious() {
        return previousId != null;
    }
}
