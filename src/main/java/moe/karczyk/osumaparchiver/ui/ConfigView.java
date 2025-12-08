package moe.karczyk.osumaparchiver.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import moe.karczyk.osumaparchiver.BeatmapSetPresent;
import moe.karczyk.osumaparchiver.ConfigViewModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class ConfigView implements Initializable {
    private ConfigViewModel configViewModel;

    @FXML
    public Parent root;
    @FXML
    private Pagination pagination;
    @FXML
    private TableView<BeatmapSetPresent> table;
    @FXML
    private TableColumn<BeatmapSetPresent, String> nameCol;

    private final int ITEMS_PER_PAGE = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pagination.currentPageIndexProperty().addListener(
                (_, _, newValue) -> onPageChange(newValue.intValue())
        );
    }


    public static ConfigView load() {
        FXMLLoader loader = new FXMLLoader(ConfigView.class.getResource("/fxml/config_view.fxml"));
        ConfigView view;
        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        view = loader.getController();
        view.root.sceneProperty().addListener((_, oldValue, newValue) -> {
            if (oldValue != newValue) {
                view.refresh();
            }
        });
        return view;
    }

    public void bind(ConfigViewModel configViewModel) {
        this.configViewModel = configViewModel;
        table.setItems(configViewModel.visibleBeatmapSets);
        nameCol.setCellValueFactory(
                it -> new SimpleStringProperty(it.getValue().name())
        );
    }

    private void onPageChange(int pageIdx) {
        configViewModel.changePage(pageIdx, ITEMS_PER_PAGE);
    }

    private void refresh() {
        pagination.setPageCount(configViewModel.getPageCount(ITEMS_PER_PAGE));
        onPageChange(pagination.getCurrentPageIndex());
    }


}
