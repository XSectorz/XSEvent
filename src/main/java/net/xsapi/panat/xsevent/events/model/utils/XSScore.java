package net.xsapi.panat.xsevent.events.model.utils;

import org.bukkit.entity.Player;

public class XSScore {
    public double score = 0;
    public String player;

    public XSScore(String player) {
        this.player = player;
    }

    public String getPlayerName() {
        return this.player;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }
}
