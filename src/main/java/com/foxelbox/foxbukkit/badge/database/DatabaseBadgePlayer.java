package com.foxelbox.foxbukkit.badge.database;

import com.foxelbox.foxbukkit.badge.Badge;
import com.foxelbox.foxbukkit.badge.BadgeDescriptor;

public class DatabaseBadgePlayer implements Badge {
    final DatabaseBadge badge;
    final int level;

    DatabaseBadgePlayer(DatabaseBadge badge, int level) {
        this.badge = badge;
        this.level = level;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public BadgeDescriptor getDescriptor() {
        return badge;
    }
}
