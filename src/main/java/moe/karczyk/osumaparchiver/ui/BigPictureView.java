package moe.karczyk.osumaparchiver.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import moe.karczyk.osumaparchiver.BigPictureViewModel;

import java.io.IOException;

public class BigPictureView {

    @FXML
    public Parent root;
    private BigPictureViewModel viewModel;

    public static BigPictureView load() {
        FXMLLoader loader = new FXMLLoader(ConfigView.class.getResource("/fxml/big_picture_view.fxml"));
        BigPictureView view;
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view = loader.getController();
        return view;
    }

    public void bind(BigPictureViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.beatmapSetImg.addListener(
                (_, _, img) -> ((Pane) root).setBackground(new Background(new BackgroundImage(img, null, null, null, null)))
        );

    }
}
