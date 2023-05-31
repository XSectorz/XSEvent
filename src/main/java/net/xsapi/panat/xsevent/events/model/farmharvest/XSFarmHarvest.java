package net.xsapi.panat.xsevent.events.model.farmharvest;

import net.xsapi.panat.xsevent.configuration.xsevent;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;

import java.util.ArrayList;

public class XSFarmHarvest extends XSEventTemplate {

    public ArrayList<String> eventRequired = new ArrayList<>();

    public XSFarmHarvest(String name) {
        super(name);
        setEventRequired(new ArrayList<>(xsevent.customConfig.getStringList("xsevent.events." + name + ".eventRequire")));
    }

    public void setEventRequired(ArrayList<String> eventRequired) {
        this.eventRequired = eventRequired;
    }

    public ArrayList<String> getEventRequired() {
        return eventRequired;
    }

}
