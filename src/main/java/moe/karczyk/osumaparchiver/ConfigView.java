package moe.karczyk.osumaparchiver;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class ConfigView {
    private ConfigViewModel configViewModel;

    @FXML
    public Parent root;
    @FXML
    private TextField mapDirectoryTextField;
    @FXML
    private Label mapDirectoryErrorLabel;


    public static ConfigView load() {
        FXMLLoader loader = new FXMLLoader(ConfigView.class.getResource("/fxml/config_view.fxml"));
        ConfigView view;
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view = loader.getController();
        return view;
    }

    public void bind(ConfigViewModel configViewModel) {
        this.configViewModel = configViewModel;
        mapDirectoryTextField.textProperty().bind(configViewModel.getMapsDirectory());
        mapDirectoryErrorLabel.textProperty().bind(configViewModel.getMapsDirectoryErrorMsg());
    }


    public void onBrowseButtonAction(ActionEvent actionEvent) {
        var currentStage = (Stage) root.getScene().getWindow();
        configViewModel.selectMapsDirectoryWithFileChooser(currentStage);
    }

}
