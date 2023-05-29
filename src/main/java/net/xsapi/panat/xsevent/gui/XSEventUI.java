package net.xsapi.panat.xsevent.gui;

import net.xsapi.panat.xsevent.configuration.messages;
import net.xsapi.panat.xsevent.configuration.xsevent;
import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.events.model.utils.XSScore;
import net.xsapi.panat.xsevent.player.xsPlayer;
import net.xsapi.panat.xsevent.configuration.config;
import net.xsapi.panat.xsevent.core.core;
import net.xsapi.panat.xsevent.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

public class XSEventUI {

    public static ArrayList<Integer> usedSlot = new ArrayList<>(Arrays.asList(10,12,14,16,28,30,32,34,48,49,50,53));

    public static ArrayList<Integer> evtSlot = new ArrayList<>(Arrays.asList(10,12,14,16,28,30,32,34));

    public static void openUI(Player p) {

        Inventory inventory = Bukkit.createInventory(null,54, Utils.replaceColor(config.customConfig.getString("gui.title")));

        if(config.customConfig.getBoolean("gui.sound.enable")) {
            p.playSound(p.getLocation(), Sound.valueOf(config.customConfig.getString("gui.sound.type")),1.0f,1.0f);
        }

        for(int i = 0 ; i < 53 ; i++) {
            if(usedSlot.contains(i)) {
                continue;
            }

            inventory.setItem(i,Utils.createDecoration("blocked"));
        }

        core.XSPlayer.get(p.getUniqueId()).setEvtPage(1);

        if(!core.pOpenGUI.containsKey(p.getUniqueId())) {
            core.pOpenGUI.put(p.getUniqueId(),inventory);
        }

        inventory.setItem(49,Utils.createDecoration("exit"));
        inventory.setItem(53,Utils.createDecoration("info"));

        updateInventory(p);
        p.openInventory(inventory);

    }

    public static void updateInventory(Player p) {
        Inventory inv = core.pOpenGUI.get(p.getUniqueId());
        updateInventoryContents(inv,p);
        p.updateInventory();
    }

    public static void updateInventoryContents(Inventory inv,Player p) {
        xsPlayer xPlayer = core.XSPlayer.get(p.getUniqueId());

        if(xPlayer.getEvtPage() > 1) {
            inv.setItem(48,Utils.createDecoration("previous"));
        } else {
            inv.setItem(48,Utils.createDecoration("previous-blocked"));
        }

        int index = (8  * (xPlayer.getEvtPage()-1));

        if(XSEventHandler.getListEvent().size() > (index+8)) {
            inv.setItem(50,Utils.createDecoration("next"));
        } else {
            inv.setItem(50,Utils.createDecoration("next-blocked"));
        }

        //clear slot
        for(int i = 0 ; i < 8 ; i++) {
            inv.setItem(evtSlot.get(i),new ItemStack(Material.AIR));
        }
        for(int i = 0 ; i < 8 ; i++) {

            //Bukkit.broadcastMessage("INDEX: " + index);
            //Bukkit.broadcastMessage("SIZE: " + XSEventHandler.getListEvent().size());

            if(index + i >= XSEventHandler.getListEvent().size()) {
                break;
            }

            XSEventTemplate xsEvt = XSEventHandler.getListEvent().get(index+i);

            ArrayList<String> lores = new ArrayList<>();

            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            int hours = now.getHour();
            int minutes = now.getMinute();
            int seconds = now.getSecond();

            Date currentDate = null;
            try {
                currentDate = format.parse(hours+":"+ minutes + ":" + seconds);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            HashMap<String,String> placeholderReplace = new HashMap<>();

            for(Map.Entry<String, String> placeholder : xsEvt.getTimerFormat().entrySet()) {
                String timeStart = xsevent.customConfig.getString("xsevent.events." + xsEvt.getIDKey() + ".eventTimer." + placeholder.getKey() +".time_to_start");

                try {
                    Date targetDate = format.parse(timeStart);
                    //Bukkit.broadcastMessage("targetDate: " + targetDate.getTime());
                    //Bukkit.broadcastMessage("currentDate: " + currentDate.getTime());

                    if(!xsEvt.getEventDateData().contains(XSEventHandler.dateInRealLife.get(new Date().getDay()))) {
                        placeholderReplace.put("%count_" + placeholder.getKey() + "%",Utils.replaceColor(Objects.requireNonNull(messages.customConfig.getString("placeholders.TIME_PASSED"))));
                        if(!xsEvt.getEventDateData().contains("EVERY_DAY")) {
                            continue;
                        }
                    }

                    long remainingTimeMillis = targetDate.getTime() - currentDate.getTime();

                    if(remainingTimeMillis <= 0) {

                        remainingTimeMillis = Math.abs(remainingTimeMillis);

                        long diffSeconds = (remainingTimeMillis / 1000);

                        //Bukkit.broadcastMessage("DIFF SECS: " + diffSeconds);
                        //Bukkit.broadcastMessage("TIME TO ALIVE: " + xsEvt.getTimers().get(placeholder.getKey()).getTimeToAlive());
                        //Bukkit.broadcastMessage("------------");

                        if(diffSeconds >= xsEvt.getTimers().get(placeholder.getKey()).getTimeToAlive()) {
                            placeholderReplace.put("%count_" + placeholder.getKey() + "%",Utils.replaceColor(messages.customConfig.getString("placeholders.TIME_PASSED")));
                        } else {
                            placeholderReplace.put("%count_" + placeholder.getKey() + "%",Utils.replaceColor(Objects.requireNonNull(messages.customConfig.getString("placeholders.TIME_START"))));
                        }
                    } else {
                        long remainingSeconds = remainingTimeMillis / 1000;
                        long remainingMinutes = remainingSeconds / 60;
                        long remainingHours = remainingMinutes / 60;

                        remainingSeconds %= 60;
                        remainingMinutes %= 60;

                        String timer = "";

                        timer += String.valueOf(remainingHours);
                        timer += " ";
                        if(remainingHours >= 1) {
                            timer += messages.customConfig.getString("placeholders.hours");
                        } else {
                            timer += messages.customConfig.getString("placeholders.hour");
                        }
                        timer += String.valueOf(remainingMinutes);
                        timer += " ";
                        if(remainingMinutes >= 1) {
                            timer += messages.customConfig.getString("placeholders.minutes");
                        } else {
                            timer += messages.customConfig.getString("placeholders.minute");
                        }
                        timer += String.valueOf(remainingSeconds);
                        timer += " ";
                        if(remainingSeconds >= 1) {
                            timer += messages.customConfig.getString("placeholders.seconds");
                        } else {
                            timer += messages.customConfig.getString("placeholders.second");
                        }

                        placeholderReplace.put("%count_" + placeholder.getKey() + "%",
                                Utils.replaceColor(messages.customConfig.getString("placeholders.TIME_START_SOON")
                                        .replace("%time%",timer)));
                    }

                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }

            DecimalFormat df = new DecimalFormat(".#");

            for(String lore : xsEvt.getIconLore()) {

                lore = lore.replace("%date%",xsEvt.getDateFormat());

                if(lore.contains("%timer_")) {
                    for(Map.Entry<String, String> placeholder : xsEvt.getTimerFormat().entrySet()) {
                        lore = lore.replace("%timer_" + placeholder.getKey() + "%",
                                xsEvt.getTimerFormat().get(placeholder.getKey()));
                    }
                }
                if(lore.contains("%count_")) {
                    for(Map.Entry<String,String> placeholders : placeholderReplace.entrySet()) {
                        lore = lore.replace(placeholders.getKey(),placeholders.getValue());
                    }
                }

                if(lore.contains("%xsevent_")) {

                    if(!xsEvt.isStart()) {
                        continue;
                    } else {

                        String score = "";

                        if(!xsEvt.getScoreList().containsKey(p.getUniqueId())) {
                            score = "-";
                        } else {
                            score += df.format(xsEvt.getScoreList().get(p.getUniqueId()).getScore());
                        }

                        lore = lore.replace("%xsevent_" + xsEvt.getIDKey() + "%",
                                Utils.replaceColor(messages.customConfig.getString("placeholders.score_replace")
                                        .replace("%xs_point%",score)));
                    }
                }

                lores.add(lore);
            }

            // lores.add(xsEvt.getEventDateData().toString());

            inv.setItem(evtSlot.get(i),Utils.createItem(xsEvt.getIconMaterial(),
                    1,xsEvt.getIconModelData(),xsEvt.getIconName(),lores
                    ,xsEvt.getIsIconGlowActivate(),new HashMap<>()));

        }
    }

}