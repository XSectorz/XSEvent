package net.xsapi.panat.xsevent.events.model.utils;

import org.bukkit.entity.Player;

public class XSScore {

    public Player p;
    public double score = 0;

    public XSScore(Player p) {
        this.p = p;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getScore() {
        return score;
    }

    public Player getPlayer() {
        return p;
    }
}
