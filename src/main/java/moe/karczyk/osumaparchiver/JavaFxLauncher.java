package moe.karczyk.osumaparchiver;

import javafx.application.Application;
import javafx.stage.Stage;
import moe.karczyk.osumaparchiver.eventpassing.Producer;
import moe.karczyk.osumaparchiver.ui.BigPictureView;
import moe.karczyk.osumaparchiver.ui.ConfigView;
import moe.karczyk.osumaparchiver.ui.DirectorySelectionView;
import moe.karczyk.osumaparchiver.ui.StageManager;
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

        var stageManager = new StageManager(stage, dirSelectionView, configView, bigPictureView);
        var producer = applicationContext.getBean(Producer.class);
        producer.addConsumer(stageManager);
        stageManager.run();

    }

    @Override
    public void stop() {
        applicationContext.close();
    }
}
