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

public class RemindPasswordController {

    private Stage stage;
    @FXML
    private TextField usernameField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label passwordReminderLabel;

    /** This method is called when the "Main Menu" button is clicked. It switches to the home1 page.
     *
     * @param event The event that is triggered when the "Main Menu" button is clicked.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void switchToHome1(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/home1.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the remind password button is clicked. It sends a request to the server to remind the password.
     *
     * @param event The event that is triggered when the login button is clicked.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void remindPassword(ActionEvent event) throws IOException {
        String username = usernameField.getText();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
            return;
        }

        try {
            JSONObject obj = RequestHandler.getRequestHandler().postRequest(jsonObject, "auth/remind-password");
            boolean status = obj.getBoolean("status");
            String message = obj.getString("message");

            if (status) {
                passwordReminderLabel.setText("Your password reminder is: " + message);
            } else {
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to log in.");
        }
    }
}