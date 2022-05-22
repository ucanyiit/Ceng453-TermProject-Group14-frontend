package ceng453.frontend.services;

import ceng453.frontend.enums.PlayerState;

public class PlayerService implements IPlayerService {

    private Integer location = 0;
    private static final Integer TOTAL_TILE_COUNT = 16;
    private PlayerState state = PlayerState.PLAYING;

    @Override
    public Integer getLocation() {
        return location;
    }

    @Override
    public void advanceLocation(Integer die1, Integer die2) {
        this.location += die1 + die2;
        this.location %= TOTAL_TILE_COUNT;
        if (!die1.equals(die2)) {
            this.setState(PlayerState.WAITING);
        }
    }

    @Override
    public PlayerState getState() {
        return state;
    }

    @Override
    public void setState(PlayerState state) {
        this.state = state;
    }
}
