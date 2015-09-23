package com.foxelbox.foxbukkit.badge.commands;

import com.foxelbox.foxbukkit.badge.FoxBukkitBadge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class BManageCommand implements CommandExecutor {
    private final FoxBukkitBadge plugin;

    public BManageCommand(FoxBukkitBadge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        return false;
    }
}
