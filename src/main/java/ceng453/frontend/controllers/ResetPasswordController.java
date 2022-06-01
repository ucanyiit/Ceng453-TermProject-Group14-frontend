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
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;

public class ResetPasswordController {

    private Stage stage;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField codeField;
    @FXML
    private TextField newPasswordField;
    @FXML
    private Label errorLabel;

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

    /** This method is called when the reset password button is clicked. It sends a request to the server to reset the password.
     *
     * @param event The event that is triggered when the login button is clicked.
     */
    public void resetPassword(ActionEvent event) {
        String username = usernameField.getText();
        String code = codeField.getText();
        String password = newPasswordField.getText();

        List<NameValuePair> params = List.of(
                new BasicNameValuePair("username", username),
                new BasicNameValuePair("token", code),
                new BasicNameValuePair("password", password)
        );

        try {
            JSONObject obj = RequestHandler.getRequestHandler().getRequest(params, "auth/reset-password");
            boolean status = obj.getBoolean("status");
            String message = obj.getString("message");

            if (status) {
                errorLabel.setText("Password changed successfully!");
                errorLabel.setStyle("-fx-text-fill: green");
            } else {
                errorLabel.setText(message);
                errorLabel.setStyle("-fx-text-fill: red");
            }
        } catch (JSONException | IOException | URISyntaxException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to log in.");
        }
    }
}