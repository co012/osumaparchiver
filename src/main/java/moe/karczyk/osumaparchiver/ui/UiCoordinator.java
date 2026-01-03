package moe.karczyk.osumaparchiver.ui;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.extern.apachecommons.CommonsLog;

@CommonsLog
public class UiCoordinator {

    private final Stage mainStage;
    private final Stage bigPictureStage = new Stage(StageStyle.UNDECORATED);
    private final Stage archiveStage = new Stage();
    private final Stage loadStage = new Stage();

    public UiCoordinator(Stage mainStage, DirectorySelectionView directorySelectionView, ConfigView configView, BigPictureView bigPictureView, ArchiveView archiveView) {
        this.mainStage = mainStage;
        mainStage.setScene(new Scene(configView.root));

        setupSubStageWithView(archiveStage, archiveView.root);
        setupSubStageWithView(loadStage, directorySelectionView.getRoot());
        bigPictureStage.setScene(new Scene(bigPictureView.root));
        bigPictureStage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), bigPictureStage::hide);

    }

    private void setupSubStageWithView(Stage stage, Parent view) {
        stage.initOwner(mainStage);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setScene(new Scene(view));
    }


    public void run() {
        mainStage.show();
        loadStage.show();
    }

    public void openBigPicture() {
        bigPictureStage.setFullScreen(true);
        bigPictureStage.show();
    }

    public void requestArchiveAction() {
        archiveStage.show();
    }

    public void notifyLoadCompleted(boolean success) {
        if (!success) {
            Platform.exit();
        }
        loadStage.close();
    }

    public void notifyArchiveCompleted() {
        archiveStage.close();
    }

}
