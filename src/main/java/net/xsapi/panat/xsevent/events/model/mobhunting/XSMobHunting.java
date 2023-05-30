package net.xsapi.panat.xsevent.events.model.mobhunting;

import net.xsapi.panat.xsevent.configuration.xsevent;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import org.bukkit.Material;

import java.util.ArrayList;

public class XSMobHunting extends XSEventTemplate {

    public ArrayList<String> eventRequired = new ArrayList<>();

    public XSMobHunting(String name) {

        this.setIDKey(name);

        this.setIconMaterial(Material.getMaterial(
                xsevent.customConfig.getString("xsevent.events." + name + ".icon.material")));
        this.setIconName(xsevent.customConfig.getString("xsevent.events." + name + ".icon.name"));
        this.setIconModelData(xsevent.customConfig.getInt("xsevent.events." + name + ".icon.modelData"));
        this.setIconLore(new ArrayList<>(xsevent.customConfig.getStringList("xsevent.events." + name + ".icon.lore")));
        this.setEventDate(xsevent.customConfig.getString("xsevent.events." + name + ".eventRepeat"));

        this.setOnClickMaterial(Material.getMaterial(
                xsevent.customConfig.getString("xsevent.events." + name + ".onClick.material")));
        this.setOnClickName(xsevent.customConfig.getString("xsevent.events." + name + ".onClick.name"));
        this.setOnClickmodelData(xsevent.customConfig.getInt("xsevent.events." + name + ".onClick.modelData"));
        this.setOnClickLore(new ArrayList<>(xsevent.customConfig.getStringList("xsevent.events." + name + ".onClick.lore")));

        setEventRequired(new ArrayList<>(xsevent.customConfig.getStringList("xsevent.events." + name + ".eventRequire")));
    }

    public void setEventRequired(ArrayList<String> eventRequired) {
        this.eventRequired = eventRequired;
    }

    public ArrayList<String> getEventRequired() {
        return eventRequired;
    }
}
