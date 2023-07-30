package net.xsapi.panat.xsevent.core;

import net.momirealms.customfishing.CustomFishing;
import net.xsapi.panat.xsevent.command.commandsLoader;
import net.xsapi.panat.xsevent.configuration.config;
import net.xsapi.panat.xsevent.configuration.configLoader;
import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import net.xsapi.panat.xsevent.events.model.utils.XSTimer;
import net.xsapi.panat.xsevent.listeners.XS_EventLoader;
import net.xsapi.panat.xsevent.player.xsPlayer;
import net.xsapi.panat.xsevent.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import redis.clients.jedis.Jedis;

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
    public static Jedis jedis;
    private static boolean usingRedis = false;

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
            redisConnection();
        }

        XSEventHandler.loadEvent();
        loadPlayerData();
        updateInventoryTask();
        checkEventTask();
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

                for(XSEventTemplate xsEvt : XSEventHandler.getListEvent()) {
                    if(!xsEvt.getEventDateData().contains(XSEventHandler.dateInRealLife.get(new Date().getDay()))) {
                        if(!xsEvt.getEventDateData().contains("EVERY_DAY")) {
                            continue;
                        }
                    }

                    for(Map.Entry<String, XSTimer> xsTimer : xsEvt.getTimers().entrySet()) {

                        LocalTime targetStartTime = LocalTime.parse(xsTimer.getValue().getStartTimer());
                        LocalTime targetEndTime = LocalTime.parse(xsTimer.getValue().getEndTimer());

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


                    if(xsEvt.isStart()) {

                        xsEvt.setEventNotifyCurrentTimer(xsEvt.getEventNotifyCurrentTimer()+1);
                        if(xsEvt.getEventNotifyCurrentTimer() >= xsEvt.getEventNotifyTimer()) {
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
    }

    private void redisConnection() {
        String redisHost = config.customConfig.getString("redis.host");
        int redisPort = config.customConfig.getInt("redis.port");
        String password = config.customConfig.getString("redis.password");

        try {
            jedis = new Jedis(redisHost, redisPort);
            if(!password.isEmpty()) {
                jedis.auth(password);
            }
            jedis.close();
            Bukkit.getConsoleSender().sendMessage("§x§E§7§F§F§0§0[XSEVENT] Redis Server : §x§6§0§F§F§0§0Connected");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§x§E§7§F§F§0§0[XSEVENT] Redis Server : §x§C§3§0§C§2§ANot Connected");
            e.printStackTrace();
        }
    }


}
