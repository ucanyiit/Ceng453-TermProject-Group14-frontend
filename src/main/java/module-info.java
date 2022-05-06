module ceng453.frontend.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens ceng453.frontend.frontend to javafx.fxml;
    exports ceng453.frontend.frontend;
}