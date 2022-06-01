package ceng453.frontend.controllers;

import ceng453.frontend.Score;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class LeaderboardController implements Initializable {

    private Stage stage;
    @FXML
    private TableView<Score> scoreTable;
    @FXML
    private TableColumn<Score, String> ranks;
    @FXML
    private TableColumn<Score, String> names;
    @FXML
    private TableColumn<Score, String> times;
    @FXML
    private TableColumn<Score, String> scores;
    @FXML
    private Label timeLabel;

    public LeaderboardController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLeaderboardList(7);
        timeLabel.setText("Week");
    }

    /** Fetches the scores with given day and sets the leaderboard.
     *
     * @param day the last days to fetch scores for
     */
    private void setLeaderboardList(int day) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String endDate = dateFormat.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -day);
        Date tmpDate = cal.getTime();
        String startDate = dateFormat.format(tmpDate);
        List<NameValuePair> params = List.of(
                new BasicNameValuePair("startDate", startDate),
                new BasicNameValuePair("endDate", endDate)
        );

        try {
            JSONObject obj = RequestHandler.getRequestHandler().getRequest(params, "leaderboard");
            boolean status = obj.getBoolean("status");

            if (status) {
                JSONArray scoresArray = obj.getJSONArray("response");

                ranks.setCellValueFactory(new PropertyValueFactory<Score, String>("rank"));
                names.setCellValueFactory(new PropertyValueFactory<Score, String>("name"));
                times.setCellValueFactory(new PropertyValueFactory<Score, String>("time"));
                scores.setCellValueFactory(new PropertyValueFactory<Score, String>("score"));

                scoreTable.getItems().setAll(parseScoreList(scoresArray));
            } else {
                String message = obj.getString("message");
                timeLabel.setText(message);
            }
        } catch (JSONException | IOException | URISyntaxException e) {
            e.printStackTrace();
            timeLabel.setText("Failed to fetch scores.");
        }
    }

    private List<Score> parseScoreList(JSONArray scoresArray) throws JSONException {
        List<Score> scores = new ArrayList<>();
        for (int i = 0; i < scoresArray.length(); i++) {
            JSONObject score = scoresArray.getJSONObject(i);
            scores.add(new Score(i, score.getString("username"), score.getDouble("score"), score.getString("date")));
        }
        return scores;
    }

    /** This method is called when the user clicks the "Main Menu" button. It opens the home2 page.
     *
     * @param event the event that triggers this method
     * @throws IOException if the FXML file cannot be loaded
     */
    public void switchToHome2(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/home2.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    /** This method is called when the user clicks the "Week" button. It loads the leaderboard with scores in the last 7 days.
     *
     * @param event the event that triggers this method
     */
    public void setWeek(ActionEvent event) {
        timeLabel.setText("Week");
        setLeaderboardList(7);
    }

    /** This method is called when the user clicks the "Month" button. It loads the leaderboard with scores in the last 30 days.
     *
     * @param event the event that triggers this method
     */
    public void setMonth(ActionEvent event) {
        timeLabel.setText("Month");
        setLeaderboardList(30);
    }

    /** This method is called when the user clicks the "All Times" button. It loads the leaderboard with scores in the last 100000 days.
     *
     * @param event the event that triggers this method
     */
    public void setAllTimes(ActionEvent event) {
        timeLabel.setText("All Times");
        setLeaderboardList(100000);
    }
}