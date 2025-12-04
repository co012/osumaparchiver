package moe.karczyk.osumaparchiver;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;


public class ConfigView {
    private ConfigViewModel configViewModel;

    @FXML
    public Parent root;
    @FXML
    private TextField mapDirectoryTextField;


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
    }

}
