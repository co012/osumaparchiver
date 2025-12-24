package moe.karczyk.osumaparchiver.ui;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import moe.karczyk.osumaparchiver.BeatmapSetViewModel;
import moe.karczyk.osumaparchiver.ConfigViewModel;
import moe.karczyk.osumaparchiver.ui.models.BeatmapSetPresent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class ConfigView implements Initializable {
    private ConfigViewModel configViewModel;
    private BeatmapSetViewModel beatmapSetViewModel;

    @FXML
    public Parent root;
    @FXML
    private Pagination pagination;
    @FXML
    private ChoiceBox<Integer> rowsCountBox;
    @FXML
    private TableView<BeatmapSetPresent> table;
    @FXML
    private TableColumn<BeatmapSetPresent, String> nameCol;
    @FXML
    private TableColumn<BeatmapSetPresent, String> artistsCol;
    @FXML
    private TableColumn<BeatmapSetPresent, String> creatorsCol;
    @FXML
    private TableColumn<BeatmapSetPresent, Number> beatmapCountCol;
    @FXML
    private TableColumn<BeatmapSetPresent, String> archiveCol;
    @FXML
    private TabPane beatmapTabPane;

    @FXML
    private ImageView background;
    @FXML
    private Label title, titleOriginal, artist, artistOriginal, creator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.sceneProperty().subscribe(scene -> {
            if (scene == null) return;
            scene.windowProperty().subscribe(window -> {
                if (window == null) return;
                window.focusedProperty().subscribe(isFocus -> {
                    if (isFocus) {
                        refresh();
                    }
                });
            });
        });

        rowsCountBox.getItems().addAll(10, 20, 30, 40, 50, 60);
        rowsCountBox.setValue(40);
        rowsCountBox.valueProperty().addListener(
                (_, oldVal, newVal) -> changeRowsPerPageCount(oldVal, newVal)
        );
        pagination.currentPageIndexProperty().addListener(
                (_, _, newValue) -> onPageChange(newValue.intValue())
        );
        table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        nameCol.setCellValueFactory(
                it -> new SimpleStringProperty(it.getValue().name())
        );
        artistsCol.setCellValueFactory(
                it -> new SimpleStringProperty(it.getValue().artists())
        );
        creatorsCol.setCellValueFactory(
                it -> new SimpleStringProperty(it.getValue().creators())
        );
        beatmapCountCol.setCellValueFactory(
                it -> new SimpleIntegerProperty(it.getValue().beatmapCount())
        );
        archiveCol.setCellValueFactory(
                it -> new SimpleStringProperty(it.getValue().archive() ? "YES" : "NO")
        );

        beatmapTabPane.getSelectionModel()
                .selectedIndexProperty()
                .addListener(
                        (_, _, idx) -> beatmapSetViewModel.setActiveBeatmap(idx.intValue())
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

    public void bind(BeatmapSetViewModel beatmapSetViewModel) {
        this.beatmapSetViewModel = beatmapSetViewModel;
        table.getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (_, _, item) -> {
                            if (item != null) this.beatmapSetViewModel.changeBeatmapSet(item.id());
                        }
                );

        beatmapSetViewModel.availableBeatmaps.subscribe(beatmaps -> {
            var newTabs = beatmaps
                    .stream()
                    .map(b -> new Tab(b.version()))
                    .toList();
            beatmapTabPane.getTabs().setAll(newTabs);
        });
        beatmapSetViewModel.activeBeatmap.subscribe(beatmap -> {
            title.setText("Title: " + beatmap.title());
            titleOriginal.setText("Title original: " + beatmap.titleOriginal());
            artist.setText("Artist: " + beatmap.artist());
            artistOriginal.setText("Artist Original: " + beatmap.artistOriginal());
            creator.setText("Creator: " + beatmap.creator());

            background.setImage(new Image(beatmap.beatmapImgUrl()));
        });

    }

    private void onPageChange(int pageIdx) {
        configViewModel.changePage(pageIdx, rowsCountBox.getValue());
    }

    private void refresh() {
        pagination.setPageCount(configViewModel.getPageCount(rowsCountBox.getValue()));
        onPageChange(pagination.getCurrentPageIndex());
    }

    private void changeRowsPerPageCount(int oldVal, int newVal) {
        var oldPage = pagination.getCurrentPageIndex();
        pagination.setPageCount(configViewModel.getPageCount(newVal));
        var newPage = oldPage * oldVal / newVal;
        pagination.setCurrentPageIndex(newPage);

        if (table.getItems().size() == oldVal) {
            onPageChange(newPage);
        }

    }

    @FXML
    private void onKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.U) {
            var selected = table.getSelectionModel().getSelectedItems();
            var selectedRows = new ArrayList<>(table.getSelectionModel().getSelectedIndices());
            configViewModel.changeBeatmapSetsArchiveStatus(selected, event.getCode() == KeyCode.A);
            refresh();
            selectedRows.forEach(rowIdx -> table.getSelectionModel().select(rowIdx));
        } else if (event.getCode() == KeyCode.B) {
            configViewModel.openBigPictureOn();
        }

    }


}
