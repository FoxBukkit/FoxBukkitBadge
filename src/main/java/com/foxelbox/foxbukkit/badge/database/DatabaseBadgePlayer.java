package com.foxelbox.foxbukkit.badge.database;

import com.foxelbox.foxbukkit.badge.Badge;

public class DatabaseBadgePlayer implements Badge {
    DatabaseBadgePlayer(DatabaseBadge badge, int level) {
        this.badge = badge;
        this.level = level;
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

    @Override
    public String getDescription() {
        return badge.getDescription();
    }

    public final DatabaseBadge badge;
    public final int level;
}
