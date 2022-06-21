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

    /** This method is called when the login succeeds. It switches to the home2 page.
     *
     * @param event The event that is triggered when the login button is clicked.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void switchToHome2(ActionEvent event, String username) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ceng453/frontend/home2.fxml"));
        Parent root = loader.load();
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
        ((Home2Controller) loader.getController()).initData(username);
    }

    /** This method is called when the user clicks the "remind password" button. It switches to the "remind password" page.
     *
     * @param event The event that is triggered when the login button is clicked.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void remindPassword(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/forget_password.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the user clicks the "reset password" button. It switches to the "reset password" page.
     *
     * @param event The event that is triggered when the login button is clicked.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void switchResetPassword(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/reset_password.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the user clicks the "reset password" button. It switches to the "reset password" page.
     *
     * @param event The event that is triggered when the login button is clicked.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void resetPassword(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        JSONObject jsonObject = new JSONObject();

        if (username.isEmpty()) {
            errorLabel.setText("Please enter your username.");
            return;
        }

        try {
            jsonObject.put("username", username);
        } catch (JSONException e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
            return;
        }

        try {
            JSONObject obj = RequestHandler.getRequestHandler().postRequest(jsonObject, "auth/request-password-reset");
            boolean status = obj.getBoolean("status");

            if (status) {
                switchResetPassword(event);
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to send e-mail to your account.");
        }
    }

    /** This method is called when the login button is clicked. It send a request to the server to check if the
     * username and password are correct. If they are, it switches to the home page. If not, it displays an error message.
     *
     * @param event The event that is triggered when the login button is clicked.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
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
                switchToHome2(event, username);
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