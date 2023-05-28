package net.xsapi.panat.xsevent.listeners;

import net.xsapi.panat.xsevent.core.core;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class XS_PlayerCloseInventoryEvent implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if(core.pOpenGUI.containsKey(e.getPlayer().getUniqueId())) {
            core.pOpenGUI.remove(e.getPlayer().getUniqueId());
        }
    }
}
