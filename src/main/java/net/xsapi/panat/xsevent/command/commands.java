package net.xsapi.panat.xsevent.command;

import net.xsapi.panat.xsevent.configuration.config;
import net.xsapi.panat.xsevent.configuration.messages;
import net.xsapi.panat.xsevent.configuration.xsevent;
import net.xsapi.panat.xsevent.events.handler.XSEventHandler;
import net.xsapi.panat.xsevent.gui.XSEventUI;
import net.xsapi.panat.xsevent.utils.Utils;
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
                String prefix = messages.customConfig.getString("messages.prefix");

                if(args.length == 0) {
                    if(sender.hasPermission("xsevent.gui")) {
                        XSEventUI.openUI(sender);
                        return true;
                    }
                    sender.sendMessage(
                            Utils.replaceColor(messages.customConfig.getString("messages.no_permission")
                                    .replace("<prefix>",prefix))
                    );
                    return false;
                } else if(args.length == 1) {
                    if(args[0].equals("reload")) {
                        if(sender.hasPermission("xsevent.reload")) {
                            xsevent.reload();
                            config.reload();
                            messages.reload();
                            sender.sendMessage(
                                    Utils.replaceColor(messages.customConfig.getString("messages.reload")
                                            .replace("<prefix>",prefix))
                            );
                            XSEventHandler.loadEvent();
                            return true;
                        }
                        sender.sendMessage(
                                Utils.replaceColor(messages.customConfig.getString("messages.no_permission")
                                        .replace("<prefix>",prefix))
                        );
                        return false;
                    }
                }
                sender.sendMessage(
                        Utils.replaceColor(messages.customConfig.getString("messages.no_command")
                                .replace("<prefix>",prefix))
                );
                return false;
            }

        }

        return false;
    }
}