package ceng453.frontend.controllers;

import ceng453.frontend.utils.RequestHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Pair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    private static final Integer BOARD_SIZE = 5;

    @FXML
    private Label errorLabel;
    @FXML
    private GridPane gameGrid;
    @FXML
    private AnchorPane pane;

    private List<Circle> playerCircles = new ArrayList<>();

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

    private List<Color> colors = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.PURPLE);

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
                JSONObject respone = obj.getJSONObject("response");
                JSONArray tiles = respone.getJSONArray("tiles");
                JSONArray players = respone.getJSONArray("players");

                for (int i = 0; i < tiles.length(); i++) {
                    JSONObject tile = tiles.getJSONObject(i);
                    Pair<Integer, Integer> location = locationToIndexes.get(tile.getInt("location"));
                    gameGrid.add(new Label(tile.getString("name")), location.getKey(), location.getValue());
                }

                for (int i = 0; i < players.length(); i++) {
                    JSONObject player = players.getJSONObject(i);
                    addPlayerCircle(player.getInt("location"), player.getInt("orderOfPlay"));
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

    private void addPlayerCircle(Integer location, Integer orderOfPlay) {
        Pair<Integer, Integer> tileIndex = locationToIndexes.get(location);
        Circle circle = new Circle(5, colors.get(orderOfPlay));
        double offset = 0.2 + orderOfPlay / 10.;

        circle.centerXProperty().bind(pane.widthProperty().multiply((tileIndex.getKey() + offset) / BOARD_SIZE));
        circle.centerYProperty().bind(pane.heightProperty().multiply((tileIndex.getValue() + 0.2) / BOARD_SIZE));

        playerCircles.add(circle);
        pane.getChildren().add(circle);
    }

}