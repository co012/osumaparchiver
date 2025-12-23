package moe.karczyk.osumaparchiver.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import moe.karczyk.osumaparchiver.BeatmapSetViewModel;
import moe.karczyk.osumaparchiver.BigPictureViewModel;
import moe.karczyk.osumaparchiver.ui.managers.BackgroundFadeManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class BigPictureView implements Initializable {

    private BigPictureViewModel bigPictureViewModel;
    private BeatmapSetViewModel beatmapSetViewModel;
    private BackgroundFadeManager backgroundFadeManager;

    @FXML
    public Parent root;
    @FXML
    private Label titleLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private Label creatorLabel;
    @FXML
    private Button nextButton, previousButton;
    @FXML
    private Region bg1, bg2;
    @FXML
    private Node archiveIndicator;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        root.sceneProperty().addListener((_, _, scene) -> {
            if (scene != null) {
                scene.addEventHandler(KeyEvent.KEY_RELEASED, this::onKeyReleased);
            }
        });
        backgroundFadeManager = new BackgroundFadeManager(bg1, bg2);
        archiveIndicator.setOpacity(0.0);
    }

    public void bind(BigPictureViewModel viewModel) {
        this.bigPictureViewModel = viewModel;

        root.sceneProperty().addListener((_, _, scene) -> {
            if (scene != null) {
                scene.windowProperty().addListener((_, _, window) -> {
                    if (window != null) {
                        window.setOnHiding((_) -> this.bigPictureViewModel.songsPlayer.stop());
                        window.setOnShowing((_) -> this.bigPictureViewModel.playBeatmap(beatmapSetViewModel.beatmapSet.get().id()));
                    }
                });
            }
        });
    }

    public void bind(BeatmapSetViewModel viewModel) {
        beatmapSetViewModel = viewModel;
        viewModel.beatmapSet.subscribe(
                newBeatmapSet -> {
                    titleLabel.setText(newBeatmapSet.name());
                    artistLabel.setText("Artist: " + newBeatmapSet.artists());
                    creatorLabel.setText("Creators: " + newBeatmapSet.creators());
                    archiveIndicator.setOpacity(newBeatmapSet.archive() ? 1.0 : 0.0);
                    nextButton.setDisable(!newBeatmapSet.hasNext());
                    previousButton.setDisable(!newBeatmapSet.hasPrevious());
                }
        );
        viewModel.activeBeatmap.addListener((_, oldBeatMap, newBeatMap) -> {
            if (oldBeatMap == null || !oldBeatMap.beatmapImgUrl().equals(newBeatMap.beatmapImgUrl())) {
                changeBackground(newBeatMap.beatmapImgUrl());
            }
        });

    }

    private void changeBackground(String backgroundUri) {
        backgroundFadeManager.fadeToBackground(backgroundUri);
    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> onPreviousButtonAction();
            case RIGHT -> onNextButtonAction();
            case A -> beatmapSetViewModel.toggleArchiveSelectionForBeatmapSet();
        }
    }

    @FXML
    private void onNextButtonAction() {
        if (beatmapSetViewModel.nextBeatmapSet()) {
            this.bigPictureViewModel.playBeatmap(beatmapSetViewModel.beatmapSet.get().id());
        }
    }

    @FXML
    private void onPreviousButtonAction() {
        if (beatmapSetViewModel.previousBeatmapSet()) {
            this.bigPictureViewModel.playBeatmap(beatmapSetViewModel.beatmapSet.get().id());
        }
    }
}
