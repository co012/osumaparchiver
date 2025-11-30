package moe.karczyk.osumaparchiver;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;


public class JavaFxLauncher extends Application {

    private Stage stage;
    private ConfigurableApplicationContext applicationContext;


    @Override
    public void init() {
        applicationContext = new SpringApplicationBuilder(SpringLauncher.class).run();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        StageRepository stageRepository = applicationContext.getBean(StageRepository.class);
        stageRepository.setMainStage(stage);

        var configView = ConfigView.load();
        ConfigViewModel configViewModel = applicationContext.getBean(ConfigViewModel.class);
        configView.bind(configViewModel);
        stage.setScene(new Scene(configView.root));
        stage.show();

    }

    @Override
    public void stop() {
        applicationContext.close();
        stage.close();
    }
}
