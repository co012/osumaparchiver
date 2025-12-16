package moe.karczyk.osumaparchiver.ui.models;

import lombok.Builder;

@Builder
public record BeatmapPresent(String title, String titleOriginal, String artist, String artistOriginal, String creator,
                             String version, String beatmapImgUrl) {
}
