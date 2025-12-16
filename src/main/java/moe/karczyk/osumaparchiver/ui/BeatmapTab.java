package moe.karczyk.osumaparchiver.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import moe.karczyk.osumaparchiver.ui.models.BeatmapPresent;

import java.io.IOException;

public class BeatmapTab extends Tab {
    @FXML
    private ImageView background;
    @FXML
    private Label title, titleOriginal, artist, artistOriginal, creator;


    public BeatmapTab(BeatmapPresent beatmap) {
        super(beatmap.version());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/components/beatmap_view.fxml"));
        loader.setController(this);
        try {
            VBox root = loader.load();
            this.setContent(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        title.setText("Title: " + beatmap.title());
        titleOriginal.setText("Title original: " + beatmap.titleOriginal());
        artist.setText("Artist: " + beatmap.artist());
        artistOriginal.setText("Artist Original: " + beatmap.artistOriginal());
        creator.setText("Creator: " + beatmap.creator());

        background.setImage(new Image(beatmap.beatmapImgUrl()));
    }

    public boolean versionEquals(BeatmapPresent other) {
        return this.getText().equals(other.version());
    }
}
