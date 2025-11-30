package moe.karczyk.osumaparchiver;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Getter
public class ConfigPresentationModel {
    StringProperty mapsDirectory = new SimpleStringProperty("");
    BooleanProperty isMapsDirectoryValid = new SimpleBooleanProperty(false);
    StringProperty mapsDirectoryErrorMsg = new SimpleStringProperty("");
}
