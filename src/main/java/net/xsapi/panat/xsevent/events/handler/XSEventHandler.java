package net.xsapi.panat.xsevent.events.handler;

import net.xsapi.panat.xsevent.configuration.messages;
import net.xsapi.panat.xsevent.configuration.xsevent;
import net.xsapi.panat.xsevent.events.model.customfishing.XSCustomFishing;
import net.xsapi.panat.xsevent.events.model.utils.*;
import net.xsapi.panat.xsevent.utils.Utils;

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

        for(String section : xsevent.customConfig.getConfigurationSection("xsevent.events").getKeys(false)) {

            XSEventType evtType = XSEventType.valueOf(xsevent.customConfig.getString("xsevent.events." + section + ".eventType"));
            //Bukkit.broadcastMessage( section + " : " + evtType.toString());

            if(evtType.equals(XSEventType.CUSTOM_FISHING)) {
                XSCustomFishing xsCustomFishingEvt = new XSCustomFishing(section);

                xsCustomFishingEvt.setDateFormat(getDateString(xsCustomFishingEvt.getEventDate()));
                setDateData(xsCustomFishingEvt);
                setEventType(xsCustomFishingEvt);
                setTimerFormat(xsCustomFishingEvt);
                setEventTrigger(xsCustomFishingEvt);
                setGlowActive(xsCustomFishingEvt);
                setReward(xsCustomFishingEvt);
                listEvent.add(xsCustomFishingEvt);
            }

        }

    }

    public static ArrayList<XSEventTemplate> getListEvent() {
        return listEvent;
    }

    public static void setGlowActive(XSEventTemplate XSETemplate) {
        XSETemplate.setIconGlowActivate(xsevent.customConfig.getBoolean("xsevent.events."
        +XSETemplate.getIDKey()+".icon.isGlowWhenActivate"));
    }

    public static void setReward(XSEventTemplate XSETemplate) {

        XSRewards xsRewards = new XSRewards();

        for(String section : xsevent.customConfig.getConfigurationSection("xsevent.events." + XSETemplate.getIDKey() + ".event_rewards.prize").getKeys(false)) {
            String ranking = section.split("_")[0];
            ArrayList<String> cmd = new ArrayList<>(
                    xsevent.customConfig.getStringList("xsevent.events." + XSETemplate.getIDKey() + ".event_rewards.prize." + section + ".commands")
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
                xsevent.customConfig.getStringList("xsevent.events." + XSETemplate.getIDKey() + ".event_rewards.participants.prize")
        );

        xsRewards.setParticipantsRewards(cmd);

        XSETemplate.setRewards(xsRewards);

    }

    public static void setEventType(XSEventTemplate XSETemplate) {
        XSETemplate.setEventType(XSEventType.valueOf(xsevent.customConfig.getString("xsevent.events." + XSETemplate.getIDKey() + ".eventType")));
    }

    public static void setEventTrigger(XSEventTemplate XSETemplate) {

        ArrayList<String> start = new ArrayList<>();
        ArrayList<String> end = new ArrayList<>();

        for(String section : xsevent.customConfig.getConfigurationSection("xsevent.events." + XSETemplate.getIDKey() + ".eventTrigger").getKeys(false)) {
            for(String cmd : xsevent.customConfig.getStringList("xsevent.events." + XSETemplate.getIDKey() + ".eventTrigger." + section + ".commands")) {
                if(section.equals("start")) {
                    start.add(cmd);
                } else {
                    end.add(cmd);
                }
            }
        }

        XSEventTrigger xsEventTrigger = new XSEventTrigger(start,end
        ,new ArrayList<>(xsevent.customConfig.getStringList("xsevent.events."
        + XSETemplate.getIDKey() + ".eventTrigger.start.broadcast")),
                new ArrayList<>(xsevent.customConfig.getStringList("xsevent.events."
                        + XSETemplate.getIDKey() + ".eventTrigger.end.broadcast")));
        XSETemplate.setEvtTrigger(xsEventTrigger);
    }

    public static void setTimerFormat(XSEventTemplate XSETemplate) {
        DecimalFormat df = new DecimalFormat("00");
        for(String section : xsevent.customConfig.getConfigurationSection("xsevent.events." + XSETemplate.getIDKey() + ".eventTimer").getKeys(false)) {

            String time_start = xsevent.customConfig.getString("xsevent.events." + XSETemplate.getIDKey() + ".eventTimer." + section +".time_to_start");
            int time_alive = xsevent.customConfig.getInt("xsevent.events." + XSETemplate.getIDKey() + ".eventTimer." + section +".time_to_alive");


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