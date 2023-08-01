package net.xsapi.panat.xsevent.utils;

import net.xsapi.panat.xsevent.events.model.utils.XSScore;

import java.util.HashMap;

public class RedisDataObject {

    public HashMap<String, XSScore> score;
    public String key;

    public RedisDataObject(String key,HashMap<String ,XSScore> score) {
        this.key = key;
        this.score = score;
    }

}
