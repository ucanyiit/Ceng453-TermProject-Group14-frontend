package ceng453.frontend.controllers;

import ceng453.frontend.utils.RequestHandler;
import ceng453.frontend.utils.StageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

public class RegisterController {

    private Stage stage;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField passwordReminderField;
    @FXML
    private Label errorLabel;

    /** This method is called when the "Main Menu" button is clicked. It switches to the home1 page.
     *
     * @param event The event that triggers this method.
     * @throws IOException Throws an IOException if the FXML file cannot be loaded.
     */
    public void switchToHome1(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/home1.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the "Register" button is clicked. It registers the user and switches to the home1 page.
     *
     * @param event The event that triggers this method.
     * @throws IOException Throws an IOException if the FXML file cannot be loaded.
     */
    public void register(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String passwordReminder = passwordReminderField.getText();

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("username", username);
            jsonObject.put("email", email);
            jsonObject.put("password", password);
            jsonObject.put("passwordReminder", passwordReminder);
        } catch (JSONException e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
            return;
        }

        try {
            JSONObject obj = RequestHandler.getRequestHandler().postRequest(jsonObject, "auth/register");
            boolean status = obj.getBoolean("status");

            if (status) {
                switchToHome1(event);
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to register.");
        }
    }
}