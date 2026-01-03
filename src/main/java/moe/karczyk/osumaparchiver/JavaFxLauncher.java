package moe.karczyk.osumaparchiver;

import javafx.application.Application;
import javafx.stage.Stage;
import moe.karczyk.osumaparchiver.ui.*;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


public class JavaFxLauncher extends Application {

    private ConfigurableApplicationContext applicationContext;


    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(SpringLauncher.class).run();
    }

    @Override
    public void start(Stage stage) {
        var dirSelectionView = DirectorySelectionView.load();
        DirectorySelectionViewModel directorySelectionViewModel = applicationContext.getBean(DirectorySelectionViewModel.class);
        dirSelectionView.bind(directorySelectionViewModel);

        var beatmapSetViewModel = applicationContext.getBean(BeatmapSetViewModel.class);

        var configView = ConfigView.load();
        ConfigViewModel configViewModel = applicationContext.getBean(ConfigViewModel.class);
        configView.bind(configViewModel);
        configView.bind(beatmapSetViewModel);

        var bigPictureView = BigPictureView.load();
        BigPictureViewModel bigPictureViewModel = applicationContext.getBean(BigPictureViewModel.class);
        bigPictureView.bind(bigPictureViewModel);
        bigPictureView.bind(beatmapSetViewModel);

        var archiveView = ArchiveView.load();
        ArchiveViewModel archiveViewModel = applicationContext.getBean(ArchiveViewModel.class);
        archiveView.bind(archiveViewModel);

        var uiCoordinator = new UiCoordinator(stage, dirSelectionView, configView, bigPictureView, archiveView);
        applicationContext.getBeansOfType(UiCoordinatorAware.class)
                .values()
                .forEach(bean -> bean.setUiCoordinator(uiCoordinator));
        uiCoordinator.run();

    }

    @Override
    public void stop() {
        applicationContext.close();
    }
}
