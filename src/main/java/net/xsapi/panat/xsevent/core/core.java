package net.xsapi.panat.xsevent.core;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.momirealms.customfishing.CustomFishing;
import net.xsapi.panat.xsevent.command.commandsLoader;
import net.xsapi.panat.xsevent.configuration.config;
import net.xsapi.panat.xsevent.configuration.configLoader;
import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.events.model.utils.XSScore;
import net.xsapi.panat.xsevent.events.model.utils.XSTimer;
import net.xsapi.panat.xsevent.listeners.XS_EventLoader;
import net.xsapi.panat.xsevent.player.xsPlayer;
import net.xsapi.panat.xsevent.utils.RedisPlayerData;
import net.xsapi.panat.xsevent.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

import java.lang.reflect.Type;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.xsapi.panat.xsevent.gui.XSEventUI.updateInventory;


public final class core extends JavaPlugin {

    public static core plugin;

    public static core getPlugin() {
        return plugin;
    }

    public static HashMap<UUID,xsPlayer> XSPlayer = new HashMap<UUID, xsPlayer>();

    public static CustomFishing cfAPI = null;

    public static HashMap<UUID, Inventory> pOpenGUI = new HashMap<>();
    private static boolean usingRedis = false;
    private static String hostRedis;
    private static String localRedis;

    @Override
    public void onEnable() {
        plugin = this;
        Bukkit.getConsoleSender().sendMessage("§x§f§f§a§c§2§f******************************");
        Bukkit.getConsoleSender().sendMessage("§x§f§f§a§c§2§f   XSAPI Event v1.0     ");
        Bukkit.getConsoleSender().sendMessage("§r");
        Bukkit.getConsoleSender().sendMessage("§x§f§f§a§c§2§f  Make the better task!");
        Bukkit.getConsoleSender().sendMessage("§r");
        Bukkit.getConsoleSender().sendMessage("§x§f§f§a§c§2§f******************************");

        APILoader();

        new commandsLoader();
        new configLoader();
        new XS_EventLoader();

        SetupDefault();
        if(usingRedis) {
            if(redisConnection()) {
                localRedis = config.customConfig.getString("cross-server.server-name");
                hostRedis = config.customConfig.getString("cross-server.parent-name");

                //Bukkit.getConsoleSender().sendMessage("CH Name: " + core.getLocalRedis());
                //Bukkit.getConsoleSender().sendMessage("Parent CH Name: " + core.getRedisHost());
                //subscribeToChannelAsync(core.getLocalRedis());
                subscribeToChannelAsync("LoginEvent/"+core.getLocalRedis());
                subscribeToChannelAsync("XSEventRedisData/"+core.getRedisHost());
            }
        }

        XSEventHandler.loadEvent();
        loadPlayerData();
        updateInventoryTask();
        checkEventTask();
    }

    public static String getRedisHost() {
        return hostRedis;
    }

    public static boolean getUsingRedis() {
        return usingRedis;
    }

    public static String getLocalRedis() {
        return localRedis;
    }

    private void subscribeToChannelAsync(String channelName) {
        String redisHost = config.customConfig.getString("redis.host");
        int redisPort = config.customConfig.getInt("redis.port");
        String password = config.customConfig.getString("redis.password");
        new Thread(() -> {
            try (Jedis jedis = new Jedis(redisHost, redisPort)) {
                if(!password.isEmpty()) {
                    jedis.auth(password);
                }
                JedisPubSub jedisPubSub = new JedisPubSub() {
                    @Override
                    public void onMessage(String channel, String message) {
                        if(channel.equalsIgnoreCase("LoginEvent/"+core.getLocalRedis())) {
                         //   Bukkit.getConsoleSender().sendMessage("Received Data Login " + message);
                            Gson gson = new Gson();
                            RedisPlayerData playerData = gson.fromJson(message, RedisPlayerData.class);
                          //  Bukkit.getConsoleSender().sendMessage("UUID + " + playerData.getUuid() );
                          //  Bukkit.getConsoleSender().sendMessage("NAME + " + playerData.getName() );
                          //  Bukkit.getConsoleSender().sendMessage("SCORE + " + playerData.getScoreList().toString() );

                            for(Map.Entry<String,Double> score : playerData.getScoreList().entrySet()) {
                                if(XSEventHandler.getListEvent().get(score.getKey()).isStart()) {
                                  //  Bukkit.getConsoleSender().sendMessage("ADD SCORE TO " + score.getKey() + " WITH " + score.getValue());
                                    XSScore xsScore = new XSScore(playerData.getName());
                                    xsScore.setScore(score.getValue());
                                    XSEventHandler.getListEvent().get(score.getKey()).getScoreList().put(playerData.getUuid(),xsScore);
                                }
                            }

                            //Bukkit.getConsoleSender().sendMessage("--------------------------------");
                        } else if(channel.equalsIgnoreCase("XSEventRedisData/" + core.getRedisHost())) {
                            //Bukkit.getConsoleSender().sendMessage("Recieved Event Data From " + "XSEventRedisData/" + core.getRedisHost());
                            Gson gson = new Gson();
                            Type scoreMapType = new TypeToken<HashMap<String, HashMap<String, XSScore>>>() {}.getType();
                            HashMap<String, HashMap<String, XSScore>> scoreRedis = gson.fromJson(message, scoreMapType);

                            for (String key : scoreRedis.keySet()) {
                                if(XSEventHandler.getListEvent().get(key).isStart()) {
                                 //   Bukkit.getConsoleSender().sendMessage("Change score : " + key);

                                    for (Map.Entry<String,XSScore> scoreList : scoreRedis.get(key).entrySet()) {
                                        if(!XSEventHandler.getListEvent().get(key).getScoreList().containsKey(scoreList.getKey())) {
                                        //    Bukkit.getConsoleSender().sendMessage("Not Contain : " + key);
                                            XSEventHandler.getListEvent().get(key).getScoreList().put(scoreList.getKey(),scoreList.getValue());
                                        } else {
                                            //ข้อมูล
                                            if(XSEventHandler.getListEvent().get(key).getScoreList().get(scoreList.getKey()).getScore() < scoreList.getValue().getScore()) {
                                         //       Bukkit.getConsoleSender().sendMessage("Updated data to : " + scoreList.getValue().getPlayerName());
                                                XSEventHandler.getListEvent().get(key).getScoreList().put(scoreList.getKey(),scoreList.getValue());
                                            } else {
                                         //       Bukkit.getConsoleSender().sendMessage("Data up-to-date : " + scoreList.getValue().getPlayerName());
                                         //       Bukkit.getConsoleSender().sendMessage("Value in Server : " + XSEventHandler.getListEvent().get(key).getScoreList().get(scoreList.getKey()).getScore());
                                         //       Bukkit.getConsoleSender().sendMessage("Value in Redis : " + scoreList.getValue().getScore());
                                         //       Bukkit.getConsoleSender().sendMessage("--------------------------------");
                                            }
                                        }
                                    }

                                    //XSEventHandler.getListEvent().get(key).setScoreList(scoreRedis.get(key));
                                }
                            }

                           // for(Map.Entry<String, XSEventTemplate> event : XSEventHandler.getListEvent().entrySet()) {
                            //    if(event.getValue().isStart()) {
                                   // Bukkit.getConsoleSender().sendMessage("Event: " + event.getValue().getIDKey());
                                   // Bukkit.getConsoleSender().sendMessage("Score: ");
                              //      for(Map.Entry<String,XSScore> eventScore : event.getValue().getScoreList().entrySet()) {
                                 //       Bukkit.broadcastMessage("P: " + eventScore.getValue().getPlayerName() + " Score: " + eventScore.getValue().getScore());
                                //   }
                                //}
                            //}

                          //  Bukkit.getConsoleSender().sendMessage("--------------------------------");

                        }
                     //   Bukkit.getConsoleSender().sendMessage("Received message from channel '" + channel + "': " + message);
                    }
                };
                jedis.subscribe(jedisPubSub, channelName);
            } catch (Exception e) {
                // จัดการข้อผิดพลาดที่เกิดขึ้น
                e.printStackTrace();
            }
        }).start();
    }

    public void sendMessageToRedisAsync(String CHName,String message) {
        String redisHost = config.customConfig.getString("redis.host");
        int redisPort = config.customConfig.getInt("redis.port");
        String password = config.customConfig.getString("redis.password");

        new Thread(() -> {
            try (Jedis jedis = new Jedis(redisHost, redisPort)) {
                if(!password.isEmpty()) {
                    jedis.auth(password);
                }
                jedis.publish(CHName, message);
            } catch (Exception e) {
                // จัดการข้อผิดพลาดที่เกิดขึ้น
                e.printStackTrace();
            }
        }).start();
    }

    public static void updateInventoryTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Map.Entry<UUID,Inventory> playerOpen : pOpenGUI.entrySet()) {
                    updateInventory(Bukkit.getPlayer(playerOpen.getKey()));
                }
            }
        }.runTaskTimer(core.getPlugin(), 0L, 20L);
    }

    public static void checkEventTask() {

        new BukkitRunnable() {
            @Override
            public void run() {
                LocalTime current = LocalTime.now();

                for(Map.Entry<String,XSEventTemplate> eventList : XSEventHandler.getListEvent().entrySet()) {
                    XSEventTemplate xsEvt = eventList.getValue();
                    if(!xsEvt.getEventDateData().contains(XSEventHandler.dateInRealLife.get(new Date().getDay()))) {
                        if(!xsEvt.getEventDateData().contains("EVERY_DAY")) {
                            continue;
                        }
                    }

                    for(Map.Entry<String, XSTimer> xsTimer : xsEvt.getTimers().entrySet()) {

                        LocalTime targetStartTime = LocalTime.parse(xsTimer.getValue().getStartTimer());
                        LocalTime targetEndTime = LocalTime.parse(xsTimer.getValue().getEndTimer());

                        //Event Start Normal
                        if(current.getHour() == targetStartTime.getHour() &&
                        current.getMinute() == targetStartTime.getMinute() &&
                        current.getSecond() == targetStartTime.getSecond()) {
                            xsEvt.setStart(true);
                            xsEvt.setEventNotifyCurrentTimer(0);
                            xsEvt.setRoundKey(xsTimer.getKey());
                            for (String text : xsEvt.getEvtTrigger().getStartBoardcast()) {
                                Bukkit.broadcastMessage(Utils.replaceColor(text));
                            }

                            for(String cmd : xsEvt.getEvtTrigger().getStartTrigger()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
                            }
                        }

                        //Event End Normal
                        if(current.getHour() == targetEndTime.getHour() &&
                                current.getMinute() == targetEndTime.getMinute() &&
                                current.getSecond() == targetEndTime.getSecond()) {
                            xsEvt.setStart(false);
                            xsEvt.setEventNotifyCurrentTimer(0);
                            xsEvt.onEventEnd();
                            for(String cmd : xsEvt.getEvtTrigger().getEndTrigger()) {
                                Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
                            }
                        }

                        //Event Start With Delayed
                        if(current.isAfter(targetStartTime) && current.isBefore(targetEndTime)) {
                            if(!xsEvt.isStart()) {
                                xsEvt.setStart(true);
                                xsEvt.setEventNotifyCurrentTimer(0);
                                xsEvt.setRoundKey(xsTimer.getKey());
                                for (String text : xsEvt.getEvtTrigger().getStartBoardcast()) {
                                    Bukkit.broadcastMessage(Utils.replaceColor(text));
                                }

                                for(String cmd : xsEvt.getEvtTrigger().getStartTrigger()) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
                                }
                            }
                        }

                        //Event End With Delayed
                        if(current.isAfter(targetEndTime)) {
                            if(xsEvt.getRoundKey().equalsIgnoreCase(xsTimer.getKey()) && xsEvt.isStart()) {
                                xsEvt.setStart(false);
                                xsEvt.setEventNotifyCurrentTimer(0);
                                xsEvt.onEventEnd();
                                for(String cmd : xsEvt.getEvtTrigger().getEndTrigger()) {
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),cmd);
                                }
                            }
                        }
                    }

                    //Event Notify
                    if(xsEvt.isStart()) {
                        xsEvt.setEventNotifyCurrentTimer(xsEvt.getEventNotifyCurrentTimer()+1);
                        if(xsEvt.getEventNotifyCurrentTimer() >= xsEvt.getEventNotifyTimer()) {
                            if(usingRedis) {
                                xsEvt.sendDataRedis();
                            }
                            xsEvt.sendNotify();
                        }
                    }


                }
            }
        }.runTaskTimer(core.getPlugin(), 0L, 20L);
    }

    public void loadPlayerData() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.closeInventory();

            xsPlayer xPlayer = new xsPlayer(p);
            core.XSPlayer.put(p.getUniqueId(),xPlayer);
        }
    }


    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§c[XSEVENT] Plugin Disabled " + Bukkit.getServer().getBukkitVersion());
    }



    public boolean setUPCustomFishing() {
        if (Bukkit.getPluginManager().getPlugin("CustomFishing") != null) {
            this.cfAPI = CustomFishing.getInstance();
        }
        if (this.cfAPI != null) {
            return true;
        } else {
            return false;
        }
    }
    public void APILoader() {
        if(!setUPCustomFishing()) {
            Bukkit.getConsoleSender().sendMessage("§x§f§f§c§e§2§2[XSEVENT] CustomFishing: §x§f§f§5§8§5§8Not Hook");
        } else {
            Bukkit.getConsoleSender().sendMessage("§x§f§f§c§e§2§2[XSEVENT] CustomFishing: §x§5§d§f§f§6§3Hook");
        }
    }

    private void SetupDefault() {
        usingRedis = config.customConfig.getBoolean("redis.enable");
        hostRedis = config.customConfig.getString("cross-server.parent-name");

    }

    private boolean redisConnection() {
        String redisHost = config.customConfig.getString("redis.host");
        int redisPort = config.customConfig.getInt("redis.port");
        String password = config.customConfig.getString("redis.password");

        try {
            Jedis jedis = new Jedis(redisHost, redisPort);
            if(!password.isEmpty()) {
                jedis.auth(password);
            }
            jedis.close();
            Bukkit.getConsoleSender().sendMessage("§x§E§7§F§F§0§0[XSEVENT] Redis Server : §x§6§0§F§F§0§0Connected");
            return true;
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§x§E§7§F§F§0§0[XSEVENT] Redis Server : §x§C§3§0§C§2§ANot Connected");
            e.printStackTrace();
        }
        return false;
    }


}
