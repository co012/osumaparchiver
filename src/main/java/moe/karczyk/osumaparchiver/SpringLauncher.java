package moe.karczyk.osumaparchiver;

import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringLauncher {
    static void main(String[] args) {
        Application.launch(JavaFxLauncher.class, args);
    }
}
