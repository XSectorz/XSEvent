package net.xsapi.panat.xsevent.listeners;

import net.momirealms.customfishing.api.event.FishResultEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class XS_CustomFishingEvent implements Listener {

    @EventHandler
    public void onFish(FishResultEvent e) {
        if(e.getLoot() == null) {
            Bukkit.broadcastMessage("NULL");
        } else {
            Bukkit.broadcastMessage(e.getResult().toString());
        }
    }

}