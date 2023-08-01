package net.xsapi.panat.xsevent.listeners;

import com.google.gson.Gson;
import net.xsapi.panat.xsevent.core.core;
import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.utils.RedisPlayerData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class XS_PlayerLeaveEvent implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();

        if(core.getUsingRedis()) {

            HashMap<String,Double> scoreEvent = new HashMap<>();

            for(Map.Entry<String, XSEventTemplate> eventList : XSEventHandler.getListEvent().entrySet()) {
                if(eventList.getValue().isStart()) {
                    if(eventList.getValue().getScoreList().containsKey(p.getUniqueId().toString())) {
                        String key = eventList.getKey();
                        double score = eventList.getValue().getScoreList().get(p.getUniqueId().toString()).getScore();
                        scoreEvent.put(key,score);
                       // Bukkit.getConsoleSender().sendMessage("REMOVE FROM DATA SERVER " + eventList.getKey());
                        eventList.getValue().getScoreList().remove(p.getUniqueId().toString());
                    }
                }
            }

           // Bukkit.getConsoleSender().sendMessage("Player Left : " + p.getName());
            //for(Map.Entry<String,Double> events : scoreEvent.entrySet()) {
            //    Bukkit.getConsoleSender().sendMessage("EVENTS : " + events.getKey() + " SCORE: " + events.getValue());
            //}

            if(!scoreEvent.isEmpty()) {
                RedisPlayerData redisPlayerData = new RedisPlayerData(p.getUniqueId().toString(),p.getName(),scoreEvent);
                Gson gson = new Gson();
                String json = gson.toJson(redisPlayerData);
                core.getPlugin().sendMessageToRedisAsync("XSEventLogout/Channel/"+core.getRedisHost(),json);
            }
        }

        if(core.XSPlayer.containsKey(p.getUniqueId())) {
            core.XSPlayer.remove(p.getUniqueId());
        }
    }

}