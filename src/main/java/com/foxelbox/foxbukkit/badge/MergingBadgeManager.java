package com.foxelbox.foxbukkit.badge;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;

public class MergingBadgeManager implements BadgeManager {
    ArrayList<BadgeManager> badgeManagers;

    @Override
    public Collection<Badge> getBadgesForPlayer(Player player) {
        ArrayList<Badge> ret = new ArrayList<>();
        for(BadgeManager badgeManager : badgeManagers) {
            ret.addAll(badgeManager.getBadgesForPlayer(player));
        }
        return ret;
    }

    @Override
    public Collection<BadgeDescriptor> getBadges() {
        ArrayList<BadgeDescriptor> ret = new ArrayList<>();
        for(BadgeManager badgeManager : badgeManagers) {
            ret.addAll(badgeManager.getBadges());
        }
        return ret;
    }

    @Override
    public BadgeDescriptor getBadge(String shortName) {
        BadgeDescriptor badgeDescriptor;
        for(BadgeManager badgeManager : badgeManagers) {
            badgeDescriptor = badgeManager.getBadge(shortName);
            if(badgeDescriptor != null) {
                return badgeDescriptor;
            }
        }
        return null;
    }
}
