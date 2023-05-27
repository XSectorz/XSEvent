package net.xsapi.panat.xsevent.gui;

import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.player.xsPlayer;
import net.xsapi.panat.xsevent.configuration.config;
import net.xsapi.panat.xsevent.core.core;
import net.xsapi.panat.xsevent.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class XSEventUI {

    public static ArrayList<Integer> usedSlot = new ArrayList<>(Arrays.asList(10,12,14,16,28,30,32,34,48,49,50,53));

    public static ArrayList<Integer> evtSlot = new ArrayList<>(Arrays.asList(10,12,14,16,28,30,32,34));

    public static void openUI(Player p) {

        Inventory inventory = Bukkit.createInventory(null,54, Utils.replaceColor(config.customConfig.getString("gui.title")));

        if(config.customConfig.getBoolean("gui.sound.enable")) {
            p.playSound(p.getLocation(), Sound.valueOf(config.customConfig.getString("gui.sound.type")),1.0f,1.0f);
        }

        inventory.setItem(48,Utils.createDecoration("previous"));
        inventory.setItem(49,Utils.createDecoration("exit"));
        inventory.setItem(50,Utils.createDecoration("next"));
        inventory.setItem(53,Utils.createDecoration("info"));

        for(int i = 0 ; i < 53 ; i++) {
            if(usedSlot.contains(i)) {
                continue;
            } else {
                inventory.setItem(i,Utils.createDecoration("blocked"));
            }
        }

        xsPlayer xPlayer = core.XSPlayer.get(p.getUniqueId());


        int index = 0 + (8  * (xPlayer.getEvtPage()-1));

        for(int i = 0 ; i < 8 ; i++) {

            if(index + i >= XSEventHandler.getListEvent().size()) {
                break;
            }

            XSEventTemplate xsEvt = XSEventHandler.getListEvent().get(index+i);


            ArrayList<String> lores = new ArrayList<>();

            for(String lore : xsEvt.getIconLore()) {
                if(lore.contains("%date%")) {
                    lore = lore.replace("%date%",xsEvt.getDateFormat());
                }
                lores.add(lore);
            }

           // lores.add(xsEvt.getEventDateData().toString());

            inventory.setItem(evtSlot.get(i),Utils.createItem(xsEvt.getIconMaterial(),
                    1,xsEvt.getIconModelData(),xsEvt.getIconName(),lores
            ,xsEvt.getIsIconGlowActivate(),new HashMap<>()));

        }


        p.openInventory(inventory);

    }

}