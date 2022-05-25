package ceng453.frontend.controllers;

import ceng453.frontend.enums.PlayerState;
import ceng453.frontend.services.PlayerService;
import ceng453.frontend.utils.RequestHandler;
import ceng453.frontend.utils.StageUtils;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;


public class BoardController implements Initializable {
    private static final Integer BOARD_SIZE = 5;

    private Stage stage;

    @FXML
    private Label errorLabel;
    @FXML
    private GridPane gameGrid;
    @FXML
    private AnchorPane pane;
    @FXML
    private Label turnAdvanceLabel;
    @FXML
    private Button turnAdvanceButton;
    @FXML
    private ListView<String> actionsList;
    @FXML
    private ListView<String> playersList;
    @FXML
    private Label botActionLabel;

    private Queue<String> botActionQueue = new PriorityQueue<>();
    PlayerService playerService = new PlayerService();
    private String gameId;
    private List<Circle> playerCircles = new ArrayList<>();
    private List<VBox> tileBoxes = new ArrayList<>();

    private static final List<Pair<Integer, Integer>> LOCATION_TO_INDEXES = List.of(
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
    private List<String> colorNames = List.of("RED", "BLUE", "GREEN", "YELLOW", "PURPLE");

    private String getPlayerDesc(int i, JSONObject player) throws JSONException {
        return colorNames.get(i)
                + ": " + player.getString("username")
                + ", money:" + player.getInt("money")
                + ", jail:" + player.getInt("jailDuration");
    }

    private void setPlayerCircles(JSONArray players) throws JSONException {
        playersList.getItems().clear();
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            addPlayerCircle(player.getInt("location"), player.getInt("orderOfPlay"));
            playersList.getItems().add(getPlayerDesc(i, player));
        }
    }

    private void updatePlayerCircles(JSONArray players) throws JSONException {
        playersList.getItems().clear();
        for (int i = 0; i < players.length(); i++) {
            JSONObject player = players.getJSONObject(i);
            updatePlayerCircle(player.getInt("location"), player.getInt("orderOfPlay"));
            playersList.getItems().add(getPlayerDesc(i, player));
        }
    }

    private void setBoardWithGameDTO(JSONObject response) throws JSONException {
        JSONArray tiles = response.getJSONArray("tiles");
        this.gameId = response.getString("gameId");

        for (int i = 0; i < tiles.length(); i++) {
            JSONObject tile = tiles.getJSONObject(i);
            Pair<Integer, Integer> location = LOCATION_TO_INDEXES.get(tile.getInt("location"));
            VBox vbox = new VBox();

            if (tileBoxes.size() != i) {
                vbox = tileBoxes.get(i);
                vbox.getChildren().clear();
            }

            Text textTileName = new Text(tile.getString("name"));
            textTileName.setStyle("-fx-font-weight: bold");
            vbox.getChildren().add(textTileName);
            if (tile.get("propertyType").equals("PUBLIC_PROPERTY") || tile.get("propertyType").equals("PRIVATE_PROPERTY")) {
                vbox.getChildren().add(new Label("Price: " + tile.get("price").toString()));
            }

            if (!tile.get("owner").equals("")) {
                vbox.getChildren().add(new Label("Owner: " + tile.get("owner").toString()));
            }

            vbox.setAlignment(Pos.CENTER);

            if (tileBoxes.size() == i) {
                gameGrid.add(vbox, location.getKey(), location.getValue());
                tileBoxes.add(vbox);
            }
        }
    }

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
            JSONObject obj = RequestHandler.getRequestHandler().postRequest(jsonObject, "game/create-game");
            boolean status = obj.getBoolean("status");

            if (status) {
                JSONObject response = obj.getJSONObject("response");
                setBoardWithGameDTO(response);
                JSONArray players = response.getJSONArray("players");
                setPlayerCircles(players);
                playerService.setState(PlayerState.PLAYING);
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to create game.");
        }
    }


    /** This method is called when the user advances their turn.
     *
     * @param event The event that triggers the method.
     */
    public void turnAdvance(ActionEvent event) {
        errorLabel.setText("");
        setBotActionList(null);
        PlayerState state = playerService.getState();
        if (state == PlayerState.PLAYING) {
            this.rollDice(event);
        } else if (state == PlayerState.WAITING) {
            this.takeAction(event);
        } else if (state == PlayerState.DONE) {
            this.nextTurn(event);
        } else {
            errorLabel.setText("Game is not in a valid state.");
        }
    }

    /** This method is called when the user clicks the turn advance button. It sends a request to the server.
     *
     * @param event The event that triggers the method.
     */
    private void rollDice(ActionEvent event) {
        List<NameValuePair> params = List.of(
                new BasicNameValuePair("gameId", this.gameId)
        );

        try {
            JSONObject obj = RequestHandler.getRequestHandler().getRequest(params, "game/roll-dice");
            boolean status = obj.getBoolean("status");

            if (status) {
                JSONObject response = obj.getJSONObject("response");
                Integer die1 = response.getInt("dice1");
                Integer die2 = response.getInt("dice2");
                JSONArray jsonActions = response.getJSONArray("actions");
                List<String> actions = new ArrayList<>();

                updatePlayerCircles(response.getJSONObject("game").getJSONArray("players"));

                for (int i = 0; i < jsonActions.length(); i++) {
                    actions.add(jsonActions.get(i).toString());
                }

                setTakeActionsList(actions);
                turnAdvanceLabel.setText(die1 + " + " + die2 + " = " + (die1 + die2));

                turnAdvanceButton.setText("Take Action");
                playerService.setState(PlayerState.WAITING);
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException | URISyntaxException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to roll dice.");
        }
    }

    /** This method is called when the user clicks the "Roll Dice" button. It sends a request to the server.
     *
     * @param event The event that triggers the method.
     */
    private void nextTurn(ActionEvent event) {
        List<NameValuePair> params = List.of(
                new BasicNameValuePair("gameId", this.gameId)
        );

        try {
            JSONObject obj = RequestHandler.getRequestHandler().getRequest(params, "game/next-turn");
            boolean status = obj.getBoolean("status");

            if (status) {
                JSONObject response = obj.getJSONObject("response");
                JSONObject game = response.getJSONObject("game");
                setBoardWithGameDTO(game);
                JSONArray players = game.getJSONArray("players");
                updatePlayerCircles(players);

                turnAdvanceLabel.setText("You have finished the turn.");
                if (game.getInt("turn") == 0) {
                    turnAdvanceButton.setText("Roll Dice");
                    playerService.setState(PlayerState.PLAYING);
                }
                Object isNull = response.get("botAction");
                if (isNull.toString().equals("null"))
                    return; // bot could not play
                JSONObject action = response.getJSONObject("botAction");
                String botAction = action.getString("action") + "\n" +
                                "DICE(" + action.get("dice1").toString() + "," + action.get("dice2").toString() + ")";
                setBotActionList(botAction);
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException | URISyntaxException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to advance the turn.");
        }
    }

    /** This method is called when the user clicks the "Roll Dice" button. It sends a request to the server.
     *
     * @param event The event that triggers the method.
     */
    public void resign(ActionEvent event) {
        List<NameValuePair> params = List.of(
                new BasicNameValuePair("gameId", this.gameId)
        );

        try {
            JSONObject obj = RequestHandler.getRequestHandler().getRequest(params, "game/resign");
            boolean status = obj.getBoolean("status");

            if (status) {
                switchToHome2(event);
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException | URISyntaxException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to resign.");
        }
    }

    private void addPlayerCircle(Integer location, Integer orderOfPlay) {
        Pair<Integer, Integer> tileIndex = LOCATION_TO_INDEXES.get(location);
        Circle circle = new Circle(10, colors.get(orderOfPlay));
        double offset = 0.4 + orderOfPlay / 5.;

        circle.centerXProperty().bind(pane.widthProperty().multiply((tileIndex.getKey() + offset) / BOARD_SIZE));
        circle.centerYProperty().bind(pane.heightProperty().multiply((tileIndex.getValue() + 0.2) / BOARD_SIZE));

        playerCircles.add(circle);
        pane.getChildren().add(circle);
    }

    private void updatePlayerCircle(Integer location, Integer orderOfPlay) {
        Pair<Integer, Integer> tileIndex = LOCATION_TO_INDEXES.get(location);
        Circle circle = playerCircles.get(orderOfPlay);
        double offset = 0.2 + orderOfPlay / 10.;

        circle.centerXProperty().unbind();
        circle.centerYProperty().unbind();

        circle.centerXProperty().bind(pane.widthProperty().multiply((tileIndex.getKey() + offset) / BOARD_SIZE));
        circle.centerYProperty().bind(pane.heightProperty().multiply((tileIndex.getValue() + 0.2) / BOARD_SIZE));
    }

    /** This method is called when game is finished. It switches to the home2 page.
     *
     * @param event The event that is triggered when the game is finished.
     * @throws IOException Throws an IOException if the FXML file cannot be found.
     */
    private void switchToHome2(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/home2.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called user pressed take action button.
     *
     * @param event The event that is triggered when the game is finished.
     */
    public void takeAction(ActionEvent event) {
        String selectedAction = actionsList.getSelectionModel().getSelectedItem();
        if (selectedAction == null) {
            errorLabel.setText("Please select an action.");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("gameId", gameId);
            jsonObject.put("action", selectedAction);
        } catch (JSONException e) {
            e.printStackTrace();
            errorLabel.setText("Error: " + e.getMessage());
            return;
        }

        try {
            JSONObject obj = RequestHandler.getRequestHandler().postRequest(jsonObject, "game/take-action");
            boolean status = obj.getBoolean("status");

            if (status) {
                JSONObject response = obj.getJSONObject("response");
                setBoardWithGameDTO(response);
                JSONArray players = response.getJSONArray("players");
                updatePlayerCircles(players);
                setTakeActionsList(null);

                turnAdvanceButton.setText("Next Turn");
                playerService.setState(PlayerState.DONE);
            } else {
                String message = obj.getString("message");
                errorLabel.setText(message);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            errorLabel.setText("Failed to create game.");
        }
    }

    private void setTakeActionsList(List<String> actions) {
        if (actions == null) {
            actionsList.setItems(FXCollections.observableArrayList());
            actionsList.setVisible(false);
        } else {
            actionsList.setItems(FXCollections.observableArrayList(actions));
            actionsList.setVisible(true);
        }
    }

    private void setBotActionList(String botAction) {
        if (botAction == null) {
            botActionLabel.setText("");
            botActionLabel.setVisible(false);
        } else {
            botActionLabel.setText(botAction);
            botActionLabel.setVisible(true);
        }
    }
}