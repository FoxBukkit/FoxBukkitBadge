package com.foxelbox.foxbukkit.badge;

public interface Badge {
    String getName();
    String getDescription();

    String getLevelName();
    int getLevel();
    int getMaxLevel();

    String getShortName();
}
