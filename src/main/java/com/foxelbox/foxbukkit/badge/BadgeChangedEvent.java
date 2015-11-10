package com.foxelbox.foxbukkit.badge;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class BadgeChangedEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private final Badge oldBadge;
    private final Badge newBadge;

    public BadgeChangedEvent(Badge oldBadge, Badge newBadge) {
        this.oldBadge = oldBadge;
        this.newBadge = newBadge;
    }

    public BadgeChangedEvent(Badge newBadge) {
        this(null, newBadge);
    }

    public Badge getNewBadge() {
        return newBadge;
    }

    public Badge getOldBadge() {
        return oldBadge;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
