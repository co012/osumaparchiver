package moe.karczyk.osumaparchiver.ui;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import moe.karczyk.osumaparchiver.ConfigViewModel;
import moe.karczyk.osumaparchiver.ui.models.BeatmapSetPresent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
    @FXML
    private TableColumn<BeatmapSetPresent, String> archiveCol;

    private final int ITEMS_PER_PAGE = 10;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pagination.currentPageIndexProperty().addListener(
                (_, _, newValue) -> onPageChange(newValue.intValue())
        );
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameCol.setCellValueFactory(
                it -> new SimpleStringProperty(it.getValue().name())
        );
        archiveCol.setCellValueFactory(
                it -> new SimpleStringProperty(it.getValue().archive() ? "YES" : "NO")
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

    }

    private void onPageChange(int pageIdx) {
        configViewModel.changePage(pageIdx, ITEMS_PER_PAGE);
    }

    private void refresh() {
        pagination.setPageCount(configViewModel.getPageCount(ITEMS_PER_PAGE));
        onPageChange(pagination.getCurrentPageIndex());
    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.U) {
            var selected = table.getSelectionModel().getSelectedItems();
            var selectedRows = new ArrayList<>(table.getSelectionModel().getSelectedIndices());
            configViewModel.changeBeatmapSetsArchiveStatus(selected, event.getCode() == KeyCode.A);
            refresh();
            selectedRows.forEach(rowIdx -> table.getSelectionModel().select(rowIdx));
        }
    }




}
