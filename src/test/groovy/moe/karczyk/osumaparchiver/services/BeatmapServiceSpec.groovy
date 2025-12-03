package moe.karczyk.osumaparchiver.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

import java.nio.file.Path

@SpringBootTest
class BeatmapServiceSpec extends Specification {
    @Autowired
    private BeatmapService beatmapService

    def "should correctly parse beatmap"() {
        when:
        def beatmap = beatmapService.parseBeatmap(Path.of("src/test/resources/beatmapSetsDirExample/beatmapSet1/beatmap1.osu"))
        then:
        with(beatmap) {
            audioFilename == "audio.mp3"
            previewTime == 1234
            title == "title"
            titleOriginal == "titleユニコード"
            artist == "artist"
            artistOriginal == "artistユニコード"
            creator == "creator"
            version == "v1"
            backgroundFilename == "background.jpg"
        }

    }
}
