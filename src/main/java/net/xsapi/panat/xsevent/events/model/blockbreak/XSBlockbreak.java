package net.xsapi.panat.xsevent.events.model.blockbreak;

import net.xsapi.panat.xsevent.configuration.xsevent;
import net.xsapi.panat.xsevent.events.model.utils.XSEventRequire;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;

import java.util.HashMap;

public class XSBlockbreak extends XSEventTemplate {

    public HashMap<String, XSEventRequire> eventRequired = new HashMap<>();

    public XSBlockbreak(String name) {
        super(name);
        for (String section : xsevent.customConfig.getStringList("xsevent.events." + name + ".eventRequire")) {

            String[] require = section.split(":");

            XSEventRequire xsEventRequire = new XSEventRequire(
                    Double.parseDouble(require[1]), 0);

            eventRequired.put(require[0], xsEventRequire);

        }
    }

    public HashMap<String, XSEventRequire> getEventRequired() {
        return eventRequired;
    }

}
