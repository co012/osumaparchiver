package moe.karczyk.osumaparchiver.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import moe.karczyk.osumaparchiver.BigPictureViewModel;
import moe.karczyk.osumaparchiver.ui.managers.BackgroundFadeManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class BigPictureView implements Initializable {

    private BigPictureViewModel viewModel;
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
    private Button nextButton, previusButton;
    @FXML
    private Region bg1, bg2;

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
    }

    public void bind(BigPictureViewModel viewModel) {
        this.viewModel = viewModel;

        viewModel.backgroundUri.addListener((_, _, newUri) -> changeBackground(newUri));
        titleLabel.textProperty().bind(viewModel.title);
        artistLabel.textProperty().bind(new SimpleStringProperty("Artist: ").concat(viewModel.artists));
        creatorLabel.textProperty().bind(new SimpleStringProperty("Creators: ").concat(viewModel.creators));

        root.sceneProperty().addListener((_, _, scene) -> {
            if (scene != null) {
                scene.windowProperty().addListener((_, _, window) -> {
                    if (window != null) {
                        window.setOnHiding((_) -> this.viewModel.songsPlayer.stop());
                    }

                });
            }
        });
    }

    private void changeBackground(String backgroundUri) {
        backgroundFadeManager.fadeToBackground(backgroundUri);
    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT -> viewModel.previousBeatmapSet();
            case RIGHT -> viewModel.nextBeatmapSet();
        }
    }

    @FXML
    private void onNextButtonAction() {
        viewModel.nextBeatmapSet();
    }

    @FXML
    private void onPreviousButtonAction() {
        viewModel.previousBeatmapSet();
    }
}
