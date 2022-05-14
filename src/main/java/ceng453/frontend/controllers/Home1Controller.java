package ceng453.frontend.controllers;

import ceng453.frontend.utils.StageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Home1Controller {

    private Stage stage;

    public void switchToLogin(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/login.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    public void switchToRegister(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/register.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }
}