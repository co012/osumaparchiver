package moe.karczyk.osumaparchiver.services


import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path

@SpringBootTest
class FileAccessServiceSpec extends Specification {

    @Autowired
    private FileAccessService fileAccessService

    @TempDir
    private Path tempPath

    def setup() {
        Files.createDirectory(tempPath.resolve("dir1"))
        Files.createDirectory(tempPath.resolve("dir2"))
        Files.createDirectory(tempPath.resolve("dir3"))

        Files.writeString(tempPath.resolve("file.png"), "png")
        Files.writeString(tempPath.resolve("file.osu"), "osu\nosu1")
    }

    def "service returns correct directory paths"() {
        when:
        def paths = fileAccessService.getDirectoriesInDirectory(tempPath)

        then:
        paths ==~ [tempPath.resolve("dir1"), tempPath.resolve("dir2"), tempPath.resolve("dir3")]
    }

    def "service returns correct file paths"() {
        when:
        def paths = fileAccessService.getFilesInDirectory(tempPath)

        then:
        paths ==~ [tempPath.resolve("file.png"),
                   tempPath.resolve("file.osu"),]
    }

    def "service returns correct file content"() {
        when:
        def contentStream = fileAccessService.getLines(tempPath.resolve("file.osu")).toList()

        then:
        contentStream == ["osu", "osu1"]
    }

}
