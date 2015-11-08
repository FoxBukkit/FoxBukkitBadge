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
}
