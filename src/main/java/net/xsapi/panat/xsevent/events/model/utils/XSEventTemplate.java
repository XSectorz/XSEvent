package net.xsapi.panat.xsevent.events.model.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.xsapi.panat.xsevent.configuration.messages;
import net.xsapi.panat.xsevent.core.core;
import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.utils.RedisDataObject;
import net.xsapi.panat.xsevent.utils.Utils;
import net.xsapi.panat.xsevent.utils.XSScoreTypeAdapter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static net.xsapi.panat.xsevent.events.handler.XSEventHandler.getDateString;

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

    //OnActivate
    public Material onActivateMaterial;
    public int onActivateModelData;
    public boolean onActivateGlowActivate;
    public HashMap<String,XSTimer> timers = new HashMap<>();

    //File
    public File customConfigFile;
    public FileConfiguration customConfig;
    public FileConfiguration getConfig() {
        return customConfig;
    }

    public int eventNotifyCurrentTimer = 0;
    public int eventNotifyTimer;
    public ArrayList<String> eventNotifyBroadcast;
    public int eventPriority;
    public XSEventType eventType;
    public String eventDate;
    public ArrayList<String> eventDateData = new ArrayList<>();

    public HashMap<String, XSScore> scoreList = new HashMap<String, XSScore>();

    public XSRewards rewards;

    /* String */
    public String dateFormat;
    public HashMap<String,String> timerFormat = new HashMap<>();

    public ArrayList<String> listExcept = new ArrayList<>();

    public String roundKey = "";

    public void setListExcept(ArrayList<String> listExcept) {
        this.listExcept = listExcept;
    }

    public ArrayList<String> getListExcept() {
        return listExcept;
    }

    public void setRoundKey(String roundKey) {
        this.roundKey = roundKey;
    }

    public String getRoundKey() {
        return roundKey;
    }

    public void setEventNotifyCurrentTimer(int eventNotifyCurrentTimer) {
        this.eventNotifyCurrentTimer = eventNotifyCurrentTimer;
    }

    public int getEventNotifyCurrentTimer() {
        return eventNotifyCurrentTimer;
    }

    public void setEventNotifyBroadcast(ArrayList<String> eventNotifyBroadcast) {
        this.eventNotifyBroadcast = eventNotifyBroadcast;
    }

    public ArrayList<String> getEventNotifyBroadcast() {
        return eventNotifyBroadcast;
    }

    public void setEventNotifyTimer(int eventNotifyTimer) {
        this.eventNotifyTimer = eventNotifyTimer;
    }

    public int getEventNotifyTimer() {
        return eventNotifyTimer;
    }

    public int getEventPriority() {
        return eventPriority;
    }

    public void setEventPriority(int eventPriority) {
        this.eventPriority = eventPriority;
    }

    public void setCustomConfig(FileConfiguration customConfig) {
        this.customConfig = customConfig;
    }

    public void setCustomConfigFile(File customConfigFile) {
        this.customConfigFile = customConfigFile;
    }

    public void setOnActivateMaterial(Material onActivateMaterial) {
        this.onActivateMaterial = onActivateMaterial;
    }

    public Material getOnActivateMaterial() {
        return onActivateMaterial;
    }

    public void setOnActivateModelData(int onActivateModelData) {
        this.onActivateModelData = onActivateModelData;
    }

    public int getOnActivateModelData() {
        return onActivateModelData;
    }

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

    public HashMap<String, XSScore> getScoreList() {
        return scoreList;
    }

    public void setScoreList(HashMap<String, XSScore> scoreList) {
        this.scoreList = scoreList;
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

    public void setOnActivateGlowActivate(boolean onActivateGlowActivate) {
        this.onActivateGlowActivate = onActivateGlowActivate;
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

    public boolean getIsOnActivateGlowActivate() {
        return onActivateGlowActivate;
    }

    public XSEventTemplate(String name,File file,FileConfiguration config) {

        this.setCustomConfig(config);
        this.setCustomConfigFile(file);
        this.setIDKey(name);

        this.setIconMaterial(Material.getMaterial(
                Objects.requireNonNull(customConfig.getString("xsevent.events.icon.material"))));
        this.setIconName(customConfig.getString("xsevent.events.icon.name"));
        this.setIconModelData(customConfig.getInt("xsevent.events.icon.modelData"));
        this.setIconLore(new ArrayList<>(customConfig.getStringList("xsevent.events.icon.lore")));
        this.setEventDate(customConfig.getString("xsevent.events.eventRepeat"));

        this.setOnClickMaterial(Material.getMaterial(
                Objects.requireNonNull(customConfig.getString("xsevent.events.onClick.material"))));
        this.setOnClickName(customConfig.getString("xsevent.events.onClick.name"));
        this.setOnClickmodelData(customConfig.getInt("xsevent.events.onClick.modelData"));
        this.setOnClickLore(new ArrayList<>(customConfig.getStringList("xsevent.events.onClick.lore")));
        this.setEventPriority(customConfig.getInt("xsevent.events.priority"));
        this.setEventNotifyTimer(customConfig.getInt("xsevent.events.eventNotify.repeat"));
        this.setEventNotifyBroadcast(new ArrayList<>(customConfig.getStringList("xsevent.events.eventNotify.broadcast")));

        if(customConfig.get("xsevent.events.eventExcept") != null) {
            this.setListExcept(new ArrayList<>(customConfig.getStringList("xsevent.events.eventExcept")));
        }

        setDateFormat(getDateString(this.getEventDate()));
        XSEventHandler.setDateData(this);
        XSEventHandler.setEventType(this);
        XSEventHandler.setTimerFormat(this);
        XSEventHandler.setEventTrigger(this);
        XSEventHandler.setGlowActive(this);
        XSEventHandler.setReward(this);
        XSEventHandler.setModelDataActivate(this);
        XSEventHandler.setMaterialActivate(this);

    }
    public void onEventEnd() {
       // Bukkit.broadcastMessage("Event end waiting for last data from redis server...");
        Bukkit.getScheduler().scheduleSyncDelayedTask(core.getPlugin(), new Runnable() {

            @Override
            public void run() {
                //sendRewards(entries);
                if(core.getUsingRedis()) {
                    sendRedisEndSignal();
                } else {
                    ArrayList<Map.Entry<String, XSScore>> entries = new ArrayList<>(scoreList.entrySet());

                    entries.sort(new Comparator<Map.Entry<String, XSScore>>() {
                        @Override
                        public int compare(Map.Entry<String, XSScore> entry1, Map.Entry<String, XSScore> entry2) {
                            double score1 = entry1.getValue().score;
                            double score2 = entry2.getValue().score;

                            return Double.compare(score2, score1);
                        }
                    });

                    endBoardcast(entries);
                }
                scoreList.clear();
            }
        }, 200L);

    }

    public void sendRedisEndSignal() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(XSScore.class, new XSScoreTypeAdapter());
        Gson gson = gsonBuilder.create();

        RedisDataObject redisDataObject = new RedisDataObject(getIDKey(), scoreList);

        String json = gson.toJson(redisDataObject);
        Bukkit.getLogger().info("Send Data to Redis Server... End Signal");
        core.getPlugin().sendMessageToRedisAsync("XSEventReset/" + core.getRedisHost(), json+"<SPLIT>"+this.getIDKey());
    }

    public void sendRewards(ArrayList<Map.Entry<String, XSScore>> ranking) {

        int rank = 1;

        for(Map.Entry<String,XSScore> entry : ranking) {
            String p = entry.getValue().getPlayerName();
            //Bukkit.broadcastMessage("Player: " + p.getName() + ", Score: " + entry.getValue().getScore());

            if(rank > this.getRewards().getRewardsList().size()) {
                for (String rewardList : this.getRewards().getParticipantsRewards()) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),rewardList.replace("%player%", p
                    ));
                }
            } else {
                for(String rewardList : this.getRewards().getRewardsList().get(rank)) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),rewardList.replace("%player%", p
                    ));
                }
            }
            rank += 1;

        }

    }

    public void endBoardcast(ArrayList<Map.Entry<String, XSScore>> ranking) {

        DecimalFormat df = new DecimalFormat("#.##");

        ArrayList<Map.Entry<String, XSScore>> tempRanking = (ArrayList<Map.Entry<String, XSScore>>) ranking.clone();

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
                                Objects.requireNonNull(messages.customConfig.getString("placeholders.no_players")));
                    } else {

                        String playerName = ranking.get(rank-1).getValue().getPlayerName();

                        text = text.replace("%xsevent_top_" + match + "%",
                                playerName);
                        tempRanking.removeIf(entry -> entry.getValue().getPlayerName().equals(playerName));

                        for(String rewardList : this.getRewards().getRewardsList().get(Integer.parseInt(match))) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),rewardList.replace("%player%", playerName
                                    ));
                                }
                            }.runTask(core.getPlugin());
                            //Bukkit.getLogger().info("Player: " + playerName + " | Rank: " + rank + " | Reward: " + rewardList);
                        }
                        //Bukkit.getLogger().info("----------------------");


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
                                Objects.requireNonNull(messages.customConfig.getString("placeholders.no_score")));
                    } else {
                        text = text.replace("%xsevent_top_score_" + match + "%",
                                df.format(ranking.get(rank - 1).getValue().getScore()));
                    }
                }
            }

            Bukkit.broadcastMessage(Utils.replaceColor(text));
        }

        for(Map.Entry<String,XSScore> entry : tempRanking) {
            for(String rewardList : this.getRewards().getParticipantsRewards()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),rewardList.replace("%player%", entry.getValue().getPlayerName()
                        ));
                    }
                }.runTask(core.getPlugin());
            }
        }
    }

    public void sendDataRedis() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(XSScore.class, new XSScoreTypeAdapter());
        Gson gson = gsonBuilder.create();

        RedisDataObject redisDataObject = new RedisDataObject(this.getIDKey(), scoreList);

        String json = gson.toJson(redisDataObject); // แปลง redisDataObject เป็น JSON

       // Bukkit.getConsoleSender().sendMessage(core.getLocalRedis() + " send Data to " + core.getRedisHost()+"/"+core.getLocalRedis());
        core.getPlugin().sendMessageToRedisAsync(core.getRedisHost() + "/" + core.getLocalRedis(), json);
        //Bukkit.getConsoleSender().sendMessage("Send complete!");
        this.setEventNotifyCurrentTimer(0);
    }

    public void sendNotify() {

        ArrayList<Map.Entry<String, XSScore>> ranking = new ArrayList<>(scoreList.entrySet());
        DecimalFormat df = new DecimalFormat("#.##");
        ranking.sort(new Comparator<Map.Entry<String, XSScore>>() {
            @Override
            public int compare(Map.Entry<String, XSScore> entry1, Map.Entry<String, XSScore> entry2) {
                double score1 = entry1.getValue().score;
                double score2 = entry2.getValue().score;

                return Double.compare(score2, score1);
            }
        });

        int rankNum = 1;
        HashMap<String,Integer> pRank = new HashMap<>();
        HashMap<String,Double> pScore = new HashMap<>();
        for(Map.Entry<String,XSScore> entry : ranking) {
            String p = entry.getValue().getPlayerName();

            pRank.put(p,rankNum);
            pScore.put(p,entry.getValue().getScore());
         //   Bukkit.getConsoleSender().sendMessage("RANK: " + rankNum + " ---> " + p + " | SCORE -> " + entry.getValue().getScore());
            rankNum += 1;
        }

        for(Player p : Bukkit.getOnlinePlayers()) {
            for(String text : this.getEventNotifyBroadcast()) {
                if(text.contains("%xsevent_top")) {
                    String regex = "%xsevent_top_(\\d+)%";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(text);

                    if(matcher.find()) {
                        String match = matcher.group(1);
                        int rank = Integer.parseInt(match);

                        if(ranking.size() < rank) {
                            text = text.replace("%xsevent_top_" + match + "%",
                                    Objects.requireNonNull(messages.customConfig.getString("placeholders.no_players")));
                        } else {
                            text = text.replace("%xsevent_top_" + match + "%",
                                    ranking.get(rank-1).getValue().getPlayerName());
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
                                    Objects.requireNonNull(messages.customConfig.getString("placeholders.no_score")));
                        } else {
                            text = text.replace("%xsevent_top_score_" + match + "%",
                                    df.format((ranking.get(rank - 1).getValue().getScore())));
                        }
                    }
                }

                text = text.replace("%xsevent_player%",p.getName().toString());

                if(!pRank.containsKey(p.getName())) {
                    text = text.replace("%xsevent_myrank%",Objects.requireNonNull(messages.customConfig.getString("placeholders.no_players")));
                    text = text.replace("%xsevent_score%",Objects.requireNonNull(messages.customConfig.getString("placeholders.no_score")));
                } else {
                    text = text.replace("%xsevent_myrank%",pRank.get(p.getName()).toString());
                    text = text.replace("%xsevent_score%",df.format(pScore.get(p.getName())));
                }

                p.sendMessage(Utils.replaceColor(text));
            }
        }
        this.setEventNotifyCurrentTimer(0);
    }
}