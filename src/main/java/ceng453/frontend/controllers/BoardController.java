package ceng453.frontend.controllers;

import ceng453.frontend.utils.RequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    @FXML
    private Label errorLabel;
    @FXML
    private GridPane gameGrid;

    private List<Pair<Integer, Integer>> locationToIndexes = List.of(
            new Pair<>(0, 4),
            new Pair<>(0, 3),
            new Pair<>(0, 2),
            new Pair<>(0, 1),
            new Pair<>(0, 0),
            new Pair<>(1, 0),
            new Pair<>(2, 0),
            new Pair<>(3, 0),
            new Pair<>(4, 0),
            new Pair<>(4, 1),
            new Pair<>(4, 2),
            new Pair<>(4, 3),
            new Pair<>(4, 4),
            new Pair<>(3, 4),
            new Pair<>(2, 4),
            new Pair<>(1, 4)
    );

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "SINGLEPLAYER");
        } catch (JSONException e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
            return;
        }


        try {
            JSONObject obj = RequestHandler.getRequestHandler().postRequest(jsonObject, "game");
            boolean status = obj.getBoolean("status");

            if (status) {
                JSONObject respones = obj.getJSONObject("response");
                JSONArray tiles = respones.getJSONArray("tiles");
                for (int i = 0; i < tiles.length(); i++) {
                    JSONObject tile = tiles.getJSONObject(i);
                    Pair<Integer, Integer> location = locationToIndexes.get(tile.getInt("location"));
                    gameGrid.add(new Label(tile.getString("name")), location.getKey(), location.getValue());
                }
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to fetch scores.");
        }
    }

}