package ceng453.frontend.controllers;

import ceng453.frontend.utils.RequestHandler;
import ceng453.frontend.utils.StageUtils;
import org.json.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LoginController {

    private Stage stage;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Label errorLabel;

    public void switchToHome1(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/home1.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    public void switchToHome2(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/home2.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    public void login(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
            return;
        }

        try {
            JSONObject obj = RequestHandler.getRequestHandler().postRequest(jsonObject, "auth/login");
            boolean status = obj.getBoolean("status");

            if (status) {
                String token = obj.getString("message");
                RequestHandler.getRequestHandler().setToken(token);
                switchToHome2(event);
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to log in.");
        }
    }
}