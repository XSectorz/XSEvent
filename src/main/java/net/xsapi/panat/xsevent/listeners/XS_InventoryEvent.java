package net.xsapi.panat.xsevent.listeners;

import net.xsapi.panat.xsevent.configuration.config;
import net.xsapi.panat.xsevent.core.core;
import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.gui.XSEventUI;
import net.xsapi.panat.xsevent.player.xsPlayer;
import net.xsapi.panat.xsevent.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class XS_InventoryEvent implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals(Utils.replaceColor(config.customConfig.getString("gui.title")))) {
            Player p = (Player) e.getWhoClicked();
            e.setCancelled(true);

            xsPlayer xPlayer = core.XSPlayer.get(p.getUniqueId());
            List<Map.Entry<String, XSEventTemplate>> Event = new ArrayList<>(XSEventHandler.getListEvent().entrySet());

            if(XSEventUI.evtSlot.contains(e.getSlot())) {

                int index = XSEventUI.evtSlot.indexOf(e.getSlot());

                int realIndex = (xPlayer.getEvtPage()-1)*8 + index;

                if (realIndex < XSEventHandler.getListEvent().size()) {

                    String key = Event.get(realIndex).getValue().getIDKey();

                    if(!xPlayer.getClickInfo().containsKey(key)) {
                        xPlayer.getClickInfo().put(key,true);
                    } else {
                        xPlayer.getClickInfo().replace(key,!xPlayer.getClickInfo().get(key));
                    }
                    XSEventUI.updateInventory(p);
                }
            }

            if(e.getSlot() == 48) {
                if(xPlayer.getEvtPage() != 1) {
                    xPlayer.removeEvtPage(1);
                    XSEventUI.updateInventory(p);
                }
            } else if(e.getSlot() == 49) {
                p.closeInventory();
            } else if(e.getSlot() == 50) {
                int index = (8  * (xPlayer.getEvtPage()-1));
                if(XSEventHandler.getListEvent().size() > (index+8)) {
                    xPlayer.addEvtPage(1);
                    XSEventUI.updateInventory(p);
                }
            }
        }
    }

}
