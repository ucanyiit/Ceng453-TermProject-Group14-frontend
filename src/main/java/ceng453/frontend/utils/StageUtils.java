package ceng453.frontend.utils;

import javafx.scene.Scene;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class StageUtils {

        /** Modifies the scene to have bootstrap css. */
        public static Scene modifyScene(Scene scene) {
                scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
                return scene;
        }

        /** Sets scene of the stage. */
        public static Stage modifyStage(Stage stage, Scene scene) {
                Scene modifiedScene = modifyScene(scene);
                stage.setScene(modifiedScene);
                stage.setTitle("Monopoly!");
                return stage;
        }
}
