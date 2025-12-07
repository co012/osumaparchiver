package moe.karczyk.osumaparchiver.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.Getter;
import moe.karczyk.osumaparchiver.DirectorySelectionViewModel;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class DirectorySelectionView implements Initializable {

    @FXML
    @Getter
    private Parent root;
    @FXML
    private Label infoLabel;
    @FXML
    private TextField pathTextField;
    @FXML
    private Button browseButton;
    @FXML
    private Button loadButton;
    @FXML
    private ProgressBar progressBar;

    private DirectorySelectionViewModel viewModel;
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    public static DirectorySelectionView load() {
        FXMLLoader loader = new FXMLLoader(DirectorySelectionView.class.getResource("/fxml/directory_selection_view.fxml"));
        DirectorySelectionView view;
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view = loader.getController();
        return view;
    }

    public void bind(DirectorySelectionViewModel viewModel) {
        this.viewModel = viewModel;
        viewModel.songsDirPath.bindBidirectional(pathTextField.textProperty());

        pathTextField.disableProperty().bind(viewModel.isLoadingMaps);
        browseButton.disableProperty().bind(viewModel.isLoadingMaps);
        loadButton.disableProperty().bind(viewModel.isPathValid.not().or(viewModel.isLoadingMaps));
        progressBar.disableProperty().bind(viewModel.isLoadingMaps.not());

        progressBar.progressProperty().bind(viewModel.loadingProgress);
        infoLabel.textProperty().bind(viewModel.infoText);
    }

    @FXML
    private void onBrowseButtonAction() {
        var currentStage = (Stage) root.getScene().getWindow();
        File selected = directoryChooser.showDialog(currentStage);
        if (selected == null) {
            return;
        }
        viewModel.processSelectedPath(selected);
    }

    @FXML
    private void onPathChanged() {
        viewModel.validateSelectedPath(pathTextField.getText());
    }

    @FXML
    private void onLoadButtonAction() {
        viewModel.loadMapsFromSelectedPath();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        directoryChooser.setTitle("Select Directory");
    }
}
