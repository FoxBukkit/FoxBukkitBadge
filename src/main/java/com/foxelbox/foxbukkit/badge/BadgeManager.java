package com.foxelbox.foxbukkit.badge;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface BadgeManager {
    Collection<Badge> getBadgesForPlayer(Player player);
    Badge getBadgeForPlayer(Player player, BadgeDescriptor badgeDescriptor);
    Collection<BadgeDescriptor> getBadges();
    BadgeDescriptor getBadge(String shortName);
}
