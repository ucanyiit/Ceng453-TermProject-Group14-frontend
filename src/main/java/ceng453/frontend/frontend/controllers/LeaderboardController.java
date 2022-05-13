package ceng453.frontend.frontend.controllers;

import ceng453.frontend.frontend.utils.RequestHandler;
import ceng453.frontend.frontend.utils.StageUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class LeaderboardController {

    private Stage stage;
    @FXML
    private ListView<String> leaderboardList;
    @FXML
    private Label timeLabel;

    private void setLeaderboardList(int day) {
        leaderboardList.getItems().clear();

        JSONObject jsonObject = new JSONObject();
        DateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy");
        String startDate = dateFormat.format(new Date());
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -day);
        Date tmpDate = cal.getTime();
        String endDate = dateFormat.format(tmpDate);

        try {
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
            timeLabel.setText("Error: " + e.getMessage());
            return;
        }

        try {
            JSONObject obj = RequestHandler.getRequestHandler().postRequest(jsonObject, "leaderboard");
            boolean status = obj.getBoolean("status");

            if (status) {
                JSONArray scores = obj.getJSONArray("response");
                for (int i = 0; i < scores.length(); i++) {
                    leaderboardList.getItems().add(scores.getJSONObject(i).getString("username") + ": " + scores.getJSONObject(i).getString("score"));
                }
            } else {
                String message = obj.getString("message");
                timeLabel.setText(message);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            timeLabel.setText("Failed to fetch scores.");
        }
    }

    public void switchToHome2(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ceng453/frontend/frontend/home2.fxml")));
        stage = StageUtils.modifyStage(stage, new Scene(root));
        stage.show();
    }

    public void setWeek(ActionEvent event) throws IOException, JSONException {
        timeLabel.setText("Week");
        setLeaderboardList(7);
    }

    public void setMonth(ActionEvent event) throws IOException {
        timeLabel.setText("Month");
        setLeaderboardList(30);
    }

    public void setAllTimes(ActionEvent event) throws IOException {
        timeLabel.setText("All Times");
        setLeaderboardList(1000000);
    }
}