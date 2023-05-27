package net.xsapi.panat.xsevent.command;

import net.xsapi.panat.xsevent.core.core;

public class commandsLoader {
    public commandsLoader() {
        core.getPlugin().getCommand("xsevent").setExecutor(new commands());
    }
}