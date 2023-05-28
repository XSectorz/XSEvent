package net.xsapi.panat.xsevent.events.model.utils;

import java.util.ArrayList;

public class XSEventTrigger {

    public ArrayList<String> startTrigger = new ArrayList<>();
    public ArrayList<String> endTrigger = new ArrayList<>();

    public XSEventTrigger(ArrayList<String> startTrigger,ArrayList<String> endTrigger) {
        this.startTrigger = startTrigger;
        this.endTrigger = endTrigger;
    }

    public ArrayList<String> getEndTrigger() {
        return endTrigger;
    }

    public ArrayList<String> getStartTrigger() {
        return startTrigger;
    }
}
