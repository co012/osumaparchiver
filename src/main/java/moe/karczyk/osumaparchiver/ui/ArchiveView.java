package moe.karczyk.osumaparchiver.ui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import moe.karczyk.osumaparchiver.ArchiveViewModel;

import java.io.File;
import java.io.IOException;

public class ArchiveView {

    @FXML
    public Parent root;

    private ArchiveViewModel archiveViewModel;
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    private TextField pathTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private CheckBox deleteAfterCheckBox;
    @FXML
    private Button archiveButton;

    public static ArchiveView load() {
        FXMLLoader loader = new FXMLLoader(ConfigView.class.getResource("/fxml/archive_view.fxml"));
        ArchiveView view;
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view = loader.getController();
        return view;
    }

    public void bind(ArchiveViewModel viewModel) {
        archiveViewModel = viewModel;
        deleteAfterCheckBox.selectedProperty().bindBidirectional(viewModel.deleteArchivedFiles);
        pathTextField.textProperty().bindBidirectional(viewModel.archivePath);
        errorLabel.textProperty().bind(viewModel.errorMsg);
        viewModel.errorMsg.subscribe(msg -> archiveButton.setDisable(msg != null && !msg.isBlank()));

    }

    @FXML
    private void onBrowseAction() {
        var currentStage = (Stage) root.getScene().getWindow();
        File selected = directoryChooser.showDialog(currentStage);
        if (selected == null) {
            return;
        }
        archiveViewModel.setArchiveDirectory(selected);
    }

    @FXML
    private void onArchiveAction() {
        // TODO
        archiveViewModel.onArchiveCompleted();
    }

    @FXML
    private void onPathFieldInput() {
        archiveViewModel.validateArchivePath();
    }

}
