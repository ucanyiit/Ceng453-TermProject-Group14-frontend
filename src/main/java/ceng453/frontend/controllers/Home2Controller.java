package ceng453.frontend.controllers;

import ceng453.frontend.utils.RequestHandler;
import ceng453.frontend.utils.StageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Home2Controller {

    private Stage stage;

    /** This method is called when the user clicks the "Logout" button. It logs the user out and opens the home1 page.
     *
     * @param event The event that triggers the method.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void logout(ActionEvent event) throws IOException {
        RequestHandler.getRequestHandler().setToken("");
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/home1.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the user clicks the "Leaderboard" button. It opens the leaderboard page.
     *
     * @param event The event that triggers the method.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void switchToLeaderboard(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/leaderboard.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the user clicks the "Instructions" button. It switches the scene to the instructions' scene.
     *
     * @param event The event that triggers the method.
     * @throws IOException Throws an exception if the fxml file cannot be found.
     */
    public void switchToInstructions(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/instructions.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the user clicks the "Play" button. It switches the scene to the board scene.
     *
     * @param event The event that triggers the method.
     * @throws IOException Throws an exception if the fxml file cannot be found.
     */
    public void switchToBoard(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/board.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }
}