package net.xsapi.panat.xsevent.events.model.utils;

import java.util.ArrayList;

public class XSEventTrigger {

    public ArrayList<String> startTrigger = new ArrayList<>();
    public ArrayList<String> endTrigger = new ArrayList<>();

    public ArrayList<String> startBoardcast = new ArrayList<>();
    public ArrayList<String> endBoardcast = new ArrayList<>();

    public XSEventTrigger(ArrayList<String> startTrigger,ArrayList<String> endTrigger,
                          ArrayList<String> startBoardcast,
                          ArrayList<String> endBoardcast) {
        this.startTrigger = startTrigger;
        this.endTrigger = endTrigger;
        this.startBoardcast = startBoardcast;
        this.endBoardcast = endBoardcast;
    }

    public ArrayList<String> getStartBoardcast() {
        return startBoardcast;
    }

    public ArrayList<String> getEndBoardcast() {
        return endBoardcast;
    }

    public ArrayList<String> getEndTrigger() {
        return endTrigger;
    }

    public ArrayList<String> getStartTrigger() {
        return startTrigger;
    }
}
