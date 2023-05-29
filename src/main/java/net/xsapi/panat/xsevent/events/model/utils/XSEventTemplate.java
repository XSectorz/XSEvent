package net.xsapi.panat.xsevent.events.model.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;

public class XSEventTemplate {

    public String IDKey;

    public boolean isStart;

    public Material iconMaterial;
    public int iconmodelData;
    public String iconName;
    public ArrayList<String> iconLore = new ArrayList<>();
    public boolean iconGlowActivate;
    public HashMap<String,XSTimer> timers = new HashMap<>();

    public XSEventType eventType;
    public String eventDate;
    public ArrayList<String> eventDateData = new ArrayList<>();

    public HashMap<UUID,XSScore> scoreList = new HashMap<>();

    public XSRewards rewards;

    /* String */
    public String dateFormat;
    public HashMap<String,String> timerFormat = new HashMap<>();


    public void setRewards(XSRewards rewards) {
        this.rewards = rewards;
    }

    public XSRewards getRewards() {
        return rewards;
    }

    public HashMap<UUID, XSScore> getScoreList() {
        return scoreList;
    }


    public void setStart(boolean start) {
        isStart = start;
    }

    public boolean isStart() {
        return isStart;
    }

    public HashMap<String, String> getTimerFormat() {
        return timerFormat;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public ArrayList<String> getEventDateData() {
        return eventDateData;
    }

    public void setEventDateData(ArrayList<String> eventDateData) {
        this.eventDateData = eventDateData;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getIDKey() {
        return IDKey;
    }

    public void setIDKey(String IDKey) {
        this.IDKey = IDKey;
    }

    public void setEventType(XSEventType eventType) {
        this.eventType = eventType;
    }

    public XSEventType getEventType() {
        return eventType;
    }

    public void setIconGlowActivate(boolean iconGlowActivate) {
        this.iconGlowActivate = iconGlowActivate;
    }

    public void setIconLore(ArrayList<String> iconLore) {
        this.iconLore = iconLore;
    }

    public void setIconMaterial(Material iconMaterial) {
        this.iconMaterial = iconMaterial;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public void setIconModelData(int iconModelData) {
        this.iconmodelData = iconModelData;
    }

    public XSEventTrigger evtTrigger;

    public XSEventTrigger getEvtTrigger() {
        return evtTrigger;
    }

    public void setEvtTrigger(XSEventTrigger evtTrigger) {
        this.evtTrigger = evtTrigger;
    }

    public HashMap<String,XSTimer> getTimers() {

        return this.timers;
    }

    public int getIconModelData() {
        return iconmodelData;
    }

    public Material getIconMaterial() {
        return iconMaterial;
    }

    public String getIconName() {
        return iconName;
    }

    public ArrayList<String> getIconLore() {
        return iconLore;
    }

    public boolean getIsIconGlowActivate() {
        return iconGlowActivate;
    }


    public void onEventEnd() {

        ArrayList<Map.Entry<UUID, XSScore>> entries = new ArrayList<>(scoreList.entrySet());

        Collections.sort(entries, new Comparator<Map.Entry<UUID, XSScore>>() {
            @Override
            public int compare(Map.Entry<UUID, XSScore> entry1, Map.Entry<UUID, XSScore> entry2) {
                double score1 = entry1.getValue().score;
                double score2 = entry2.getValue().score;

                return Double.compare(score2, score1);
            }
        });

        sendRewards(entries);
    }

    public void sendRewards(ArrayList<Map.Entry<UUID,XSScore>> ranking) {

        int rank = 1;

        for(Map.Entry<UUID,XSScore> entry : ranking) {
            Player p = entry.getValue().getPlayer();
            //Bukkit.broadcastMessage("Player: " + p.getName() + ", Score: " + entry.getValue().getScore());

            for(String rewardList : this.getRewards().getRewardsList().get(rank)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),rewardList.replace("%player%", p.getName()
                        ));
            }
            rank += 1;

        }
    }
}