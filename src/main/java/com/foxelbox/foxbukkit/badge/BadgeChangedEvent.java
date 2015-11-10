package com.foxelbox.foxbukkit.badge;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BadgeChangedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Player player;
    private final Badge oldBadge;
    private final Badge newBadge;

    public BadgeChangedEvent(Player player, Badge oldBadge, Badge newBadge) {
        this.player = player;
        this.oldBadge = oldBadge;
        this.newBadge = newBadge;
    }

    public BadgeChangedEvent(Player player, Badge newBadge) {
        this(player, null, newBadge);
    }

    public Badge getNewBadge() {
        return newBadge;
    }

    public Badge getOldBadge() {
        return oldBadge;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
