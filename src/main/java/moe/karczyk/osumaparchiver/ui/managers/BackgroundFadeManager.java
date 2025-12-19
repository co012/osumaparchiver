package moe.karczyk.osumaparchiver.ui.managers;

import javafx.animation.FadeTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class BackgroundFadeManager {
    private record Packet(Node node, FadeTransition show) {
    }

    private Packet visible, hidden;

    public BackgroundFadeManager(Node bg1, Node bg2) {
        bg1.setOpacity(1.0);
        bg2.setOpacity(0.0);

        var transition1 = new FadeTransition(Duration.millis(300), bg1);
        transition1.setFromValue(0);
        transition1.setToValue(1);
        var transition2 = new FadeTransition(Duration.millis(300), bg2);
        transition2.setFromValue(0);
        transition2.setToValue(1);
        visible = new Packet(bg1, transition1);
        hidden = new Packet(bg2, transition2);
    }

    public void fadeToBackground(String url) {
        visible.show.stop();
        hidden.node.setStyle("-fx-background-image: url('%s');".formatted(url));

        visible.node.setOpacity(0);
        hidden.show.play();

        var tmp = visible;
        visible = hidden;
        hidden = tmp;
    }

}
