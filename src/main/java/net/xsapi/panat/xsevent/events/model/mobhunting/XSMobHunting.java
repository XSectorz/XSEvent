package net.xsapi.panat.xsevent.events.model.mobhunting;

import net.xsapi.panat.xsevent.events.model.utils.XSEventRequire;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.HashMap;

public class XSMobHunting extends XSEventTemplate {

    public HashMap<String, XSEventRequire> eventRequired = new HashMap<>();

    public XSMobHunting(String name, File file, FileConfiguration fileConfiguration) {
        super(name,file,fileConfiguration);

        for(String section : customConfig.getStringList("xsevent.events.eventRequire")) {

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
