package com.foxelbox.foxbukkit.badge;

public abstract class BadgeDescriptor {
    public abstract String getName();
    public abstract String getDescription();

    private static final String[] ROMAN_NUMERALS_BASE = {
        "I",
        "II",
        "III",
        "IV",
        "V",
        "VI",
        "VII",
        "VIII",
        "IX",
        "X"
    };

    public String getLevelName(int level) {
        if(ROMAN_NUMERALS_BASE.length > level) {
            return ROMAN_NUMERALS_BASE[level];
        }
        return String.valueOf(level);
    }

    public String[] getLevelNames() {
        return ROMAN_NUMERALS_BASE;
    }

    public abstract int getMaxLevel();

    public abstract String getId();
}
