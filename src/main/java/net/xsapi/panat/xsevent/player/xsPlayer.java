package net.xsapi.panat.xsevent.player;

import org.bukkit.entity.Player;

public class xsPlayer {

    public int evtPage = 1;
    public Player p;

    public xsPlayer(Player p) {
        this.p = p;
    }

    public Player getPlayer() {
        return p;
    }

    public int getEvtPage() {
        return evtPage;
    }

    public void setEvtPage(int evtPage) {
        this.evtPage = evtPage;
    }

    public void addEvtPage(int add) {
        this.evtPage = this.evtPage + add;
    }

    public void removeEvtPage(int remove) {
        this.evtPage = this.evtPage - remove;
    }
}