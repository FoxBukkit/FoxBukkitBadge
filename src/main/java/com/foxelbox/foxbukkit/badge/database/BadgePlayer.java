package com.foxelbox.foxbukkit.badge.database;

public class BadgePlayer {
    BadgePlayer(Badge badge, int level) {
        this.badge = badge;
        this.level = level;
    }

    public final Badge badge;
    public final int level;
}
