package moe.karczyk.osumaparchiver;

import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class StageRepository {
    @Getter
    @Setter
    private Stage mainStage;
}
