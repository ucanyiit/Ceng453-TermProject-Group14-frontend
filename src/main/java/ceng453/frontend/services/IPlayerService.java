package ceng453.frontend.services;

import ceng453.frontend.enums.PlayerState;

public interface IPlayerService {
    PlayerState getState();
    void setState(PlayerState state);
}
