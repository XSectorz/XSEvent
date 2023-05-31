package net.xsapi.panat.xsevent.events.model.mobhunting;

import net.xsapi.panat.xsevent.configuration.xsevent;
import net.xsapi.panat.xsevent.events.model.utils.XSEventRequire;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;

public class XSMobHunting extends XSEventTemplate {

    public HashMap<String, XSEventRequire> eventRequired = new HashMap<>();

    public XSMobHunting(String name) {
        super(name);

        for(String section : xsevent.customConfig.getStringList("xsevent.events." + name + ".eventRequire")) {

            String[] require = section.split(":");

            XSEventRequire xsEventRequire = new XSEventRequire(
                Double.parseDouble(require[1]),Double.parseDouble(require[2])
            );

            eventRequired.put(require[0],xsEventRequire);

        }
    }

    public HashMap<String, XSEventRequire> getEventRequired() {
        return eventRequired;
    }
}
