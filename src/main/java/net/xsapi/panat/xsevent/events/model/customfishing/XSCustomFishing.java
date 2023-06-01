package net.xsapi.panat.xsevent.events.model.customfishing;

import net.xsapi.panat.xsevent.events.model.utils.XSEventTemplate;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

public class XSCustomFishing extends XSEventTemplate {


    public XSCustomFishing(String name, File file, FileConfiguration fileConfiguration) {
        super(name,file,fileConfiguration);
    }
}