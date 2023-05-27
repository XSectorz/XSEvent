package net.xsapi.panat.xsevent.listeners;

import net.xsapi.panat.xsevent.core.core;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class XS_PlayerLeaveEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(core.XSPlayer.containsKey(p.getUniqueId())) {
            core.XSPlayer.remove(p.getUniqueId());
        }
    }

}