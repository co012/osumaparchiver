package moe.karczyk.osumaparchiver.ui;


import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.util.ResourceBundle;

public class ArchiveView implements Initializable {

    @FXML
    public Parent root;

    private ArchiveViewModel archiveViewModel;
    private final DirectoryChooser directoryChooser = new DirectoryChooser();

    @FXML
    private TextField archiveDirPathField, archiveNameField;
    @FXML
    private Label dirErrorLabel, archiveNameError;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.sceneProperty().subscribe(scene -> {
            if (scene != null) {
                scene.windowProperty().subscribe(window -> {
                    if (window != null) {
                        window.setOnShowing(_ -> archiveViewModel.refresh());
                    }
                });
            }
        });
    }

    public void bind(ArchiveViewModel viewModel) {
        archiveViewModel = viewModel;
        deleteAfterCheckBox.selectedProperty().bindBidirectional(viewModel.deleteArchivedFiles);
        archiveDirPathField.textProperty().bindBidirectional(viewModel.archiveDirPath);
        dirErrorLabel.textProperty().bind(viewModel.archiveDirErrorMsg);
        archiveNameField.textProperty().bindBidirectional(viewModel.archiveName);
        archiveNameError.textProperty().bind(Bindings.when(viewModel.archiveNameErrorMsg.isEmpty())
                .then(viewModel.archivePathErrorMsg)
                .otherwise(viewModel.archiveNameErrorMsg)
        );
        archiveButton.disableProperty().bind(viewModel.archiveDirErrorMsg
                .isEmpty()
                .and(viewModel.archiveNameErrorMsg.isEmpty())
                .and(viewModel.archiveDirPath.isEmpty())
                .not()
        );
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
    private void onArchiveNameInput() {
        archiveViewModel.validateArchiveName();
    }

    @FXML
    private void onDirPathInput() {
        archiveViewModel.validateArchiveDirectory();
    }

}
