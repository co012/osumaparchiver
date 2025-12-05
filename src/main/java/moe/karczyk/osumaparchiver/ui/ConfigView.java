package moe.karczyk.osumaparchiver.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import moe.karczyk.osumaparchiver.ConfigViewModel;

import java.io.IOException;


public class ConfigView {
    private ConfigViewModel configViewModel;

    @FXML
    public Parent root;


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
    }

}
