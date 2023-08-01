package net.xsapi.panat.xsevent.utils;

import java.util.HashMap;

public class RedisPlayerData {

    public String uuid;
    public String name;
    public HashMap<String,Double> scoreList = new HashMap<String,Double>();


    public RedisPlayerData(String uuid,String name,HashMap<String,Double> scoreList) {
        this.uuid = uuid;
        this.name = name;
        this.scoreList = scoreList;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public HashMap<String, Double> getScoreList() {
        return scoreList;
    }

}
