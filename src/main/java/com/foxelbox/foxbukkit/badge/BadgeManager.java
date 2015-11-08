package com.foxelbox.foxbukkit.badge;

import org.bukkit.entity.Player;

import java.util.Collection;

public interface BadgeManager {
    public Collection<Badge> getBadgesForPlayer(Player player);
}
