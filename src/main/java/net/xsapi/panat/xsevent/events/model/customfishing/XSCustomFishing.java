package net.xsapi.panat.xsevent.events.model.customfishing;

import net.xsapi.panat.xsevent.configuration.xsevent;
import net.xsapi.panat.xsevent.events.model.utils.XSDate;
import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;

public class XSCustomFishing extends XSEventTemplate {

    public XSCustomFishing(String name) {

        this.setIDKey(name);

        this.setIconMaterial(Material.getMaterial(
                xsevent.customConfig.getString("xsevent.events." + name + ".icon.material")));
        this.setIconName(xsevent.customConfig.getString("xsevent.events." + name + ".icon.name"));
        this.setIconModelData(xsevent.customConfig.getInt("xsevent.events." + name + ".icon.modelData"));
        this.setIconLore(new ArrayList<>(xsevent.customConfig.getStringList("xsevent.events." + name + ".icon.lore")));
        this.setEventDate(xsevent.customConfig.getString("xsevent.events." + name + ".eventRepeat"));
    }

    public void fish() {
        Bukkit.broadcastMessage("FISH !");
    }

}