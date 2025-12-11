package moe.karczyk.osumaparchiver.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import moe.karczyk.osumaparchiver.BigPictureViewModel;

import java.io.IOException;


public class BigPictureView {

    @FXML
    public Parent root;
    private BigPictureViewModel viewModel;

    @FXML
    private Label titleLabel;
    @FXML
    private Label artistLabel;
    @FXML
    private Label creatorLabel;

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


        viewModel.backgroundUri.addListener((_, _, newUri) -> changeBackground(newUri));
        titleLabel.textProperty().bind(viewModel.title);
        artistLabel.textProperty().bind(new SimpleStringProperty("Artist: ").concat(viewModel.artists));
        creatorLabel.textProperty().bind(new SimpleStringProperty("Creators: ").concat(viewModel.creators));

    }

    private void changeBackground(String backgroundUri) {
        System.out.println(backgroundUri);
        System.out.println();
        root.setStyle("-fx-background-image: url('%s');".formatted(backgroundUri));
    }
}
