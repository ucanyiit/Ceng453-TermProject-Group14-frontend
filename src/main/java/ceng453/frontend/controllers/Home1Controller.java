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

    /** This method is called when the user clicks the "Login" button. It switches the scene to the login scene.
     *
     * @param event The event that triggers the method.
     * @throws IOException Throws an exception if the fxml file cannot be found.
     */
    public void switchToLogin(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/login.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the user clicks the "Register" button. It switches the scene to the register scene.
     *
     * @param event The event that triggers the method.
     * @throws IOException Throws an exception if the fxml file cannot be found.
     */
    public void switchToRegister(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/register.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }
}