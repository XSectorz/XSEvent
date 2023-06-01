package net.xsapi.panat.xsevent.events.handler;

import net.xsapi.panat.xsevent.configuration.messages;
import net.xsapi.panat.xsevent.core.core;
import net.xsapi.panat.xsevent.events.model.customfishing.XSCustomFishing;
import net.xsapi.panat.xsevent.events.model.blockbreak.XSBlockbreak;
import net.xsapi.panat.xsevent.events.model.mobhunting.XSMobHunting;
import net.xsapi.panat.xsevent.events.model.utils.*;
import net.xsapi.panat.xsevent.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class XSEventHandler {

    public static ArrayList<XSEventTemplate> listEvent = new ArrayList<>();

    public static ArrayList<String> dateData = new ArrayList<>(Arrays.asList("MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY","SUNDAY","EVERY_DAY"));

    public static ArrayList<String> dateInRealLife = new ArrayList<>(Arrays.asList("SUNDAY","MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY","SATURDAY"));

    public static void loadEvent() {

        //Bukkit.broadcastMessage("START LOAD");

        listEvent.clear();

        checkDirectoryAndCreateFile();
        getAllEvents();

        //Bukkit.broadcastMessage("Load Quest: " + listEvent.size());
        Bukkit.getLogger().info("§x§f§f§c§e§2§2[XSEVENT] Quests Loaded : §x§4§D§D§5§5§1" + listEvent.size() + " §x§f§f§c§e§2§2!");

    }

    public static void checkDirectoryAndCreateFile() {
        String directoryName = "xsevents";

        File directory = new File(core.getPlugin().getDataFolder(), directoryName);
        File fileFish = new File(core.getPlugin().getDataFolder(), "xsevents/xsevent_siam_fishing.yml");
        File fileMob = new File(core.getPlugin().getDataFolder(), "xsevents/xsevent_siam_mob_killing.yml");
        File fileFarm = new File(core.getPlugin().getDataFolder(), "xsevents/xsevent_siam_farming.yml");

        if (!directory.exists()) {
            directory.mkdirs();
            if (!fileFish.exists()) {
                fileFish.getParentFile().mkdirs();
                core.getPlugin().saveResource("xsevents/xsevent_siam_fishing.yml", false);
            }
            if (!fileMob.exists()) {
                fileMob.getParentFile().mkdirs();
                core.getPlugin().saveResource("xsevents/xsevent_siam_mob_killing.yml", false);
            }
            if (!fileFarm.exists()) {
                fileMob.getParentFile().mkdirs();
                core.getPlugin().saveResource("xsevents/xsevent_siam_farming.yml", false);
            }

        }
    }

    public static void getAllEvents() {
        String directoryName = "xsevents";
        File directory = new File(core.getPlugin().getDataFolder(), directoryName);

        if (directory.exists() && directory.isDirectory()) {
            File[] allEvent = directory.listFiles();

            if (allEvent != null) {
                for (File eventFile : allEvent) {
                    if (eventFile.isFile()) {
                        //Bukkit.broadcastMessage(eventFile.getName());
                        String fileName = eventFile.getName();

                        if(fileName.endsWith(".yml")) {
                           // Bukkit.broadcastMessage("END WITH YML LOADING...");
                            File customConfigFile = new File(core.getPlugin().getDataFolder(), "xsevents/"+fileName);
                            FileConfiguration customConfig;
                            customConfig = (FileConfiguration) new YamlConfiguration();

                            try {
                                customConfig.load(customConfigFile);
                            } catch (IOException | org.bukkit.configuration.InvalidConfigurationException e) {
                                e.printStackTrace();
                            }

                            //Bukkit.broadcastMessage("TYPE: " + customConfig.getString("xsevent.events.eventType"));
                            XSEventType evtType = XSEventType.valueOf(customConfig.getString("xsevent.events.eventType"));
                            if(evtType.equals(XSEventType.CUSTOM_FISHING)) {
                                XSCustomFishing xsCustomFishingEvt = new XSCustomFishing(fileName.replace(".yml","")
                                        ,customConfigFile,customConfig);
                                listEvent.add(xsCustomFishingEvt);
                            } else if(evtType.equals(XSEventType.MOB_HUNTING)) {
                                XSMobHunting xsMobHunting = new XSMobHunting(fileName.replace(".yml","")
                                        ,customConfigFile,customConfig);
                                listEvent.add(xsMobHunting);
                            } else if(evtType.equals(XSEventType.BLOCK_BREAK)) {
                                XSBlockbreak xsFarmHarvest = new XSBlockbreak(fileName.replace(".yml","")
                                        ,customConfigFile,customConfig);
                                listEvent.add(xsFarmHarvest);
                            }
                        }
                    }
                }
            }
        }
    }

    public static ArrayList<XSEventTemplate> getListEvent() {
        return listEvent;
    }

    public static void setGlowActive(XSEventTemplate XSETemplate) {
        XSETemplate.setOnActivateGlowActivate(XSETemplate.customConfig.getBoolean("xsevent.events.activateOption.isGlowWhenActivate"));
    }

    public static void setMaterialActivate(XSEventTemplate XSETemplate) {
        XSETemplate.setOnActivateMaterial(Material.getMaterial(XSETemplate.customConfig.getString("xsevent.events.activateOption.material")));
    }

    public static void setModelDataActivate(XSEventTemplate XSETemplate) {
        XSETemplate.setOnActivateModelData(XSETemplate.customConfig.getInt("xsevent.events.activateOption.modelData"));
    }

    public static void setReward(XSEventTemplate XSETemplate) {

        XSRewards xsRewards = new XSRewards();

        for(String section : XSETemplate.customConfig.getConfigurationSection("xsevent.events.event_rewards.prize").getKeys(false)) {
            String ranking = section.split("_")[0];
            ArrayList<String> cmd = new ArrayList<>(
                    XSETemplate.customConfig.getStringList("xsevent.events.event_rewards.prize." + section + ".commands")
            );

            if(ranking.split("-").length == 2) {

                int start = Integer.parseInt(ranking.split("-")[0]);
                int end = Integer.parseInt(ranking.split("-")[1]);

                for(int i = start ; i <= end ; i++) {
                    xsRewards.getRewardsList().put(i,cmd);
                }

            } else {

                xsRewards.getRewardsList().put(Integer.parseInt(ranking),cmd);
            }
        }

        ArrayList<String> cmd = new ArrayList<>(
                XSETemplate.customConfig.getStringList("xsevent.events.event_rewards.participants.prize")
        );

        xsRewards.setParticipantsRewards(cmd);

        XSETemplate.setRewards(xsRewards);

    }

    public static void setEventType(XSEventTemplate XSETemplate) {
        XSETemplate.setEventType(XSEventType.valueOf(XSETemplate.customConfig.getString("xsevent.events.eventType")));
    }

    public static void setEventTrigger(XSEventTemplate XSETemplate) {

        ArrayList<String> start = new ArrayList<>();
        ArrayList<String> end = new ArrayList<>();

        for(String section : XSETemplate.customConfig.getConfigurationSection("xsevent.events.eventTrigger").getKeys(false)) {
            for(String cmd : XSETemplate.customConfig.getStringList("xsevent.events.eventTrigger." + section + ".commands")) {
                if(section.equals("start")) {
                    start.add(cmd);
                } else {
                    end.add(cmd);
                }
            }
        }

        XSEventTrigger xsEventTrigger = new XSEventTrigger(start,end
        ,new ArrayList<>(XSETemplate.customConfig.getStringList("xsevent.events.eventTrigger.start.broadcast")),
                new ArrayList<>(XSETemplate.customConfig.getStringList("xsevent.events.eventTrigger.end.broadcast")));
        XSETemplate.setEvtTrigger(xsEventTrigger);
    }

    public static void setTimerFormat(XSEventTemplate XSETemplate) {
        DecimalFormat df = new DecimalFormat("00");
        for(String section : XSETemplate.customConfig.getConfigurationSection("xsevent.events.eventTimer").getKeys(false)) {

            String time_start = XSETemplate.customConfig.getString("xsevent.events.eventTimer." + section +".time_to_start");
            int time_alive = XSETemplate.customConfig.getInt("xsevent.events.eventTimer." + section +".time_to_alive");


            //Bukkit.broadcastMessage("SECTION: " + section + " time_start: " + time_start + " time_alive: " + time_alive);

            int hours = time_alive / 3600;
            int minutes = (time_alive % 3600) / 60;
            int seconds = (time_alive % 3600) % 60;

            int timeH = Integer.parseInt(time_start.split(":")[0]) + hours;
            int timeM = Integer.parseInt(time_start.split(":")[1]) + minutes;
            int timeS = Integer.parseInt(time_start.split(":")[2]) + seconds;

            if(timeS >= 60) {
                timeS -= 60;
                timeM +=1;
            }
            if(timeM >= 60) {
                timeM -= 60;
                timeH += 1;
            }
            if(timeH >= 24) {
                timeH -= 24;
            }

            XSETemplate.getTimerFormat().put(
                    section,
                    time_start + "-" +
                    df.format(timeH)+":"+df.format(timeM)+":"+df.format(timeS));

            XSTimer xsTimer = new XSTimer(time_start,time_alive,df.format(timeH)+":"+df.format(timeM)+":"+df.format(timeS));
            XSETemplate.getTimers().put(section,xsTimer);
        }
    }

    public static void setDateData(XSEventTemplate XSETemplate) {
        ArrayList<String> date_split = new ArrayList<>(Arrays.asList(XSETemplate.getEventDate().split(",")));
        for(int i = 0 ; i < date_split.size() ; i++) {
            String day = date_split.get(i);

            try {
                XSDate xsDate = XSDate.valueOf(day);
                XSETemplate.getEventDateData().add(day);
            } catch (IllegalArgumentException e) {

                String[] date_split_v2 = day.split("-");

                int startIndex = dateData.indexOf(date_split_v2[0]);
                int endIndex = dateData.indexOf(date_split_v2[1]);

                for(int j = startIndex ; j <= endIndex ; j++) {
                    XSETemplate.getEventDateData().add(dateData.get(j));
                }

            }
        }
    }

    public static String getDateString(String date) {

        ArrayList<String> date_split = new ArrayList<>(Arrays.asList(date.split(",")));

        String str = "";

        for(int i = 0 ; i < date_split.size() ; i++) {

            String day = date_split.get(i);

            try {
                XSDate xsDate = XSDate.valueOf(day);
                str += messages.customConfig.getString("dates." + day);
            } catch (IllegalArgumentException e) {

                String[] date_split_v2 = day.split("-");
                str += messages.customConfig.getString("dates." + date_split_v2[0]);
                str += "-";
                str += messages.customConfig.getString("dates." + date_split_v2[1]);;

            }

            if(i+1 != date_split.size()) {
                str += ", ";
            }
        }

        return Utils.replaceColor(str);
    }

}