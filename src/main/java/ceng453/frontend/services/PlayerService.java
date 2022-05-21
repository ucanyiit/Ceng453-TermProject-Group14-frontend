package ceng453.frontend.services;

import ceng453.frontend.enums.PlayerState;

public class PlayerService implements IPlayerService {

    private Integer location = 0;
    private static final Integer TOTAL_TILE_COUNT = 20;
    private PlayerState state = PlayerState.PLAYING;

    @Override
    public Integer getLocation() {
        return location;
    }

    @Override
    public void advanceLocation(Integer tileCount) {
        this.location += tileCount;
        this.location %= TOTAL_TILE_COUNT;
        this.setState(PlayerState.WAITING);
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
