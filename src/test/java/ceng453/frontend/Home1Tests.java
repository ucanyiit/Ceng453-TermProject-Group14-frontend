package ceng453.frontend;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
public class Home1Tests {
    Pane root;
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("home1.fxml")));
        this.stage = stage;
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    @Test
    public void hasHelloWorldButton() {
        Button registerButton = (Button) root.getChildrenUnmodifiable().get(0);
        Button loginButton = (Button) root.getChildrenUnmodifiable().get(1);
        assertEquals("Register", registerButton.getText());
        assertEquals("Login", loginButton.getText());
    }
}
