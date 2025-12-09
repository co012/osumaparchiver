package moe.karczyk.osumaparchiver.ui.models;

import lombok.Builder;

@Builder
public record BeatmapSetPresent(long id, String name, boolean archive) {
}
