package net.xsapi.panat.xsevent.listeners;

import net.xsapi.panat.xsevent.core.core;
import net.xsapi.panat.xsevent.events.model.customfishing.XSCustomFishing;
import net.xsapi.panat.xsevent.events.model.customfishing.XSEvent;
import org.bukkit.Bukkit;

public class XS_EventLoader {

    public XS_EventLoader() {
        if(core.cfAPI != null) {
            Bukkit.getPluginManager().registerEvents(new XSEvent(), core.getPlugin());
        }
        Bukkit.getPluginManager().registerEvents(new XS_PlayerJoinEvent(), core.getPlugin());
        Bukkit.getPluginManager().registerEvents(new XS_PlayerLeaveEvent(), core.getPlugin());
        Bukkit.getPluginManager().registerEvents(new XS_PlayerCloseInventoryEvent(), core.getPlugin());
    }

}