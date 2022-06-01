package ceng453.frontend;

public class Score {
    private int rank;
    private String name;
    private Double score;
    private String time;

    public Score(int rank, String name, Double score, String time) {
        this.rank = rank;
        this.name = name;
        this.score = score;
        this.time = time;
    }

    public int getRank() {
        return rank;
    }

    public String getName() {
        return name;
    }

    public Double getScore() {
        return score;
    }

    public String getTime() {
        return time;
    }
}
