package moe.karczyk.osumaparchiver;


import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class SongsPlayer {

    private MediaPlayer mediaPlayer;

    public void play(String url, long startFromMs) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
        }
        var media = new Media(url);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setStartTime(Duration.millis(startFromMs));
        mediaPlayer.play();
    }

    public void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
        }
    }


}
