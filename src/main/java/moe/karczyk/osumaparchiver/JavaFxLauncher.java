package moe.karczyk.osumaparchiver;

import javafx.application.Application;
import javafx.stage.Stage;
import moe.karczyk.osumaparchiver.eventpassing.Producer;
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

        var configView = ConfigView.load();
        ConfigViewModel configViewModel = applicationContext.getBean(ConfigViewModel.class);
        configView.bind(configViewModel);

        var stageManager = new StageManager(stage, dirSelectionView, configView);
        var producer = applicationContext.getBean(Producer.class);
        producer.addConsumer(stageManager);
        stageManager.run();

    }

    @Override
    public void stop() {
        applicationContext.close();
    }
}
