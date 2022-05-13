module ceng453.frontend.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires json;

    opens ceng453.frontend.frontend to javafx.fxml;
    exports ceng453.frontend.frontend;
    exports ceng453.frontend.frontend.controllers;
    opens ceng453.frontend.frontend.controllers to javafx.fxml;
}