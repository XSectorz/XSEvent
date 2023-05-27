package net.xsapi.panat.xsevent.command;

import net.xsapi.panat.xsevent.gui.XSEventUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class commands  implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String arg, String[] args) {

        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;

            if (command.getName().equalsIgnoreCase("xsevent")) {
                XSEventUI.openUI(sender);
            }
        }

        return false;
    }
}