package com.foxelbox.foxbukkit.badge.commands;

import com.foxelbox.foxbukkit.badge.Badge;
import com.foxelbox.foxbukkit.badge.BadgeDescriptor;
import com.foxelbox.foxbukkit.badge.FoxBukkitBadge;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BadgesCommand implements CommandExecutor {
    private final FoxBukkitBadge plugin;

    public BadgesCommand(FoxBukkitBadge plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        final Player target;
        if(args.length > 0) {
            target = plugin.getServer().getPlayer(args[0]);
        } else {
            target = (Player)commandSender;
        }

        if(target == null) {
            plugin.sendMessageTo(commandSender, "Sorry, could not find player.");
            return true;
        }

        plugin.sendMessageTo(commandSender, "Badges for player " + target.getName());

        for(Badge badge : plugin.getGlobalBadgeManager().getBadgesForPlayer(target)) {
            BadgeDescriptor badgeDescriptor = badge.getDescriptor();
            if(badgeDescriptor.getMaxLevel() > 1) {
                plugin.sendMessageTo(commandSender, badgeDescriptor.getName() + ": " + badgeDescriptor.getLevelName(badge.getLevel()));
            } else {
                plugin.sendMessageTo(commandSender, badgeDescriptor.getName());
            }
        }

        plugin.sendMessageTo(commandSender, "--- END ---");

        return true;
    }
}
