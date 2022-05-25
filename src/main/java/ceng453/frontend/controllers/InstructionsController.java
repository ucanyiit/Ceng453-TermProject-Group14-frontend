package ceng453.frontend.controllers;

import ceng453.frontend.utils.RequestHandler;
import ceng453.frontend.utils.StageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class InstructionsController implements Initializable {
    private Stage stage;
    @FXML
    private Label instructionsList;
    @FXML
    private Label errorLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setInstructionsList();
    }

    /** Fetches the instructions and sets the instructions list. */
    private void setInstructionsList() {
        instructionsList.setText("");

        List<NameValuePair> params = List.of();

        try {
            JSONObject obj = RequestHandler.getRequestHandler().getRequest(params, "instructions");
            boolean status = obj.getBoolean("status");

            if (status) {
                JSONArray instructions = obj.getJSONArray("response");
                for (int i = 0; i < instructions.length(); i++) {
                    instructionsList.setText(instructions.getString(i));
                }
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException | URISyntaxException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to fetch instructions.");
        }
    }

    /** This method is called when the "Main Menu" button is clicked. It switches to the home2 page.
     *
     * @param event The event that is triggered when the "Main Menu" button is clicked.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    public void switchToHome1(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/home2.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }
}