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
public class Home2Tests {
    Pane root;
    Stage stage;

    @Start
    public void start(Stage stage) throws Exception {
        root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource("home2.fxml")));
        this.stage = stage;
        stage.setScene(new Scene(root));
        stage.show();
        stage.toFront();
    }

    /** Checks buttons in the main menu. */
    @Test
    public void checkButtons() {
        Button registerButton = (Button) root.getChildrenUnmodifiable().get(0);
        Button loginButton = (Button) root.getChildrenUnmodifiable().get(1);
        Button leaderboardButton = (Button) root.getChildrenUnmodifiable().get(2);
        Button accountButton = (Button) root.getChildrenUnmodifiable().get(3);
        Button logoutButton = (Button) root.getChildrenUnmodifiable().get(4);
        assertEquals("Play", registerButton.getText());
        assertEquals("Instructions", loginButton.getText());
        assertEquals("Leaderboard", leaderboardButton.getText());
        assertEquals("Account Details", accountButton.getText());
        assertEquals("Logout", logoutButton.getText());
    }
}
