package com.foxelbox.foxbukkit.badge;

public interface BadgeDescriptor {
    String getName();
    String getDescription();

    String getLevelName(int level);
    String[] getLevelNames();
    int getMaxLevel();

    String getId();
}
