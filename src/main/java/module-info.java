module ceng453.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires json;

    opens ceng453.frontend to javafx.fxml;
    exports ceng453.frontend;
    exports ceng453.frontend.controllers;
    opens ceng453.frontend.controllers to javafx.fxml;
}