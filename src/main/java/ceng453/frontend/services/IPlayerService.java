package ceng453.frontend.services;

import ceng453.frontend.enums.PlayerState;

public interface IPlayerService {
    Integer getLocation();
    void advanceLocation(Integer tileCount);
    PlayerState getState();
    void setState(PlayerState state);
}