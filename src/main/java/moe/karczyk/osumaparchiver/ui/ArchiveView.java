package moe.karczyk.osumaparchiver.ui;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import moe.karczyk.osumaparchiver.ArchiveViewModel;

import java.io.IOException;

public class ArchiveView {

    @FXML
    public Parent root;

    private ArchiveViewModel archiveViewModel;

    @FXML
    private TextField pathTextField;
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

    }

    @FXML
    private void onBrowseAction() {
    }

    @FXML
    private void onArchiveAction() {
        // TODO
        archiveViewModel.onArchiveCompleted();
    }

}
