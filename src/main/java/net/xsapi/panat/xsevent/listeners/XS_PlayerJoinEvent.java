package net.xsapi.panat.xsevent.listeners;

import net.xsapi.panat.xsevent.core.core;
import net.xsapi.panat.xsevent.player.xsPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class XS_PlayerJoinEvent implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();

        xsPlayer xPlayer = new xsPlayer(p);

        if(core.getUsingRedis()) {
            core.getPlugin().sendMessageToRedisAsync("XSEventLogin/Channel/"
                    +core.getRedisHost(),core.getLocalRedis()+":" + p.getUniqueId());
        }
        core.XSPlayer.put(p.getUniqueId(),xPlayer);
    }

}