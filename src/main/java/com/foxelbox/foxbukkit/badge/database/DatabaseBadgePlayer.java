package com.foxelbox.foxbukkit.badge.database;

public class DatabaseBadgePlayer implements Badge {
    DatabaseBadgePlayer(DatabaseBadge badge, int level) {
        this.badge = badge;
        this.level = level;
    }

    @Override
    public int getId() {
        return badge.getId();
    }

    @Override
    public String getName() {
        return badge.getName();
    }

    @Override
    public String getLevelName() {
        return badge.getLevelName(level);
    }

    @Override
    public String getShortName() {
        return badge.getShortName();
    }

    public final DatabaseBadge badge;
    public final int level;
}
