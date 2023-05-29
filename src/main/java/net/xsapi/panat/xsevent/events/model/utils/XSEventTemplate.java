package net.xsapi.panat.xsevent.events.model.utils;

import net.xsapi.panat.xsevent.configuration.messages;
import net.xsapi.panat.xsevent.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class XSEventTemplate {

    public String IDKey;

    public boolean isStart;

    public Material iconMaterial;
    public int iconmodelData;
    public String iconName;
    public ArrayList<String> iconLore = new ArrayList<>();

    //onClick
    public Material onClickMaterial;
    public int onClickmodelData;
    public String onClickName;
    public ArrayList<String> onClickLore = new ArrayList<>();

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


    public int getOnClickmodelData() {
        return onClickmodelData;
    }

    public Material getOnClickMaterial() {
        return onClickMaterial;
    }

    public String getOnClickName() {
        return onClickName;
    }

    public ArrayList<String> getOnClickLore() {
        return onClickLore;
    }

    public void setOnClickMaterial(Material onClickMaterial) {
        this.onClickMaterial = onClickMaterial;
    }

    public void setOnClickLore(ArrayList<String> onClickLore) {
        this.onClickLore = onClickLore;
    }

    public void setOnClickmodelData(int onClickmodelData) {
        this.onClickmodelData = onClickmodelData;
    }

    public void setOnClickName(String onClickName) {
        this.onClickName = onClickName;
    }

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

        endBoardcast(entries);
        sendRewards(entries);
    }

    public void endBoardcast(ArrayList<Map.Entry<UUID,XSScore>> ranking) {

        for (String text : this.getEvtTrigger().getEndBoardcast()) {

            if(text.contains("%xsevent_top")) {
                String regex = "%xsevent_top_(\\d+)%";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(text);

                if(matcher.find()) {
                    String match = matcher.group(1);
                    int rank = Integer.parseInt(match);

                    if(ranking.size() < rank) {
                        text = text.replace("%xsevent_top_" + match + "%",
                                messages.customConfig.getString("placeholders.no_players"));
                    } else {
                        text = text.replace("%xsevent_top_" + match + "%",
                                ranking.get(rank-1).getValue().getPlayer().getName());
                    }
                }
            }
            if(text.contains("%xsevent_top_score_")) {
                String regex = "%xsevent_top_score_(\\d+)%";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(text);

                if(matcher.find()) {
                    String match = matcher.group(1);
                    int rank = Integer.parseInt(match);

                    if(ranking.size() < rank) {
                        text = text.replace("%xsevent_top_score_" + match + "%",
                                messages.customConfig.getString("placeholders.no_score"));
                    } else {
                        text = text.replace("%xsevent_top_score_" + match + "%",
                                ranking.get(rank-1).getValue().getScore()+"");
                    }
                }
            }

            Bukkit.broadcastMessage(Utils.replaceColor(text));
        }
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