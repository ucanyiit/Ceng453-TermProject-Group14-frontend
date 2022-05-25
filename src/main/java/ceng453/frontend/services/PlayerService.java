package ceng453.frontend.services;

import ceng453.frontend.enums.PlayerState;

public class PlayerService implements IPlayerService {
    private PlayerState state = PlayerState.PLAYING;

    @Override
    public PlayerState getState() {
        return state;
    }

    @Override
    public void setState(PlayerState state) {
        this.state = state;
    }
}
