package com.foxelbox.foxbukkit.badge.database;

import com.foxelbox.foxbukkit.badge.BadgeDescriptor;

import java.sql.*;

public class DatabaseBadge implements BadgeDescriptor {
    private int id = -1;
    private String shortName;
    private String name;
    private String description;
    private int maxLevel = 1;

    private String[] levelNames = null;

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

    private final DatabaseBadgeManager databaseBadgeManager;

    DatabaseBadge(DatabaseBadgeManager databaseBadgeManager) {
        this.databaseBadgeManager = databaseBadgeManager;
    }

    DatabaseBadge(DatabaseBadgeManager databaseBadgeManager, int id, String shortName, String name, String description, int maxLevel, String levelNames) {
        this(databaseBadgeManager);
        this.id = id;
        this.shortName = shortName;
        this.name = name;
        this.description = description;
        this.maxLevel = maxLevel;
        if(levelNames != null && !levelNames.isEmpty()) {
            this.levelNames = levelNames.split(",");
        }
    }

    public int getDatabaseId() {
        return id;
    }

    @Override
    public String[] getLevelNames() {
        return (levelNames != null) ? levelNames : ROMAN_NUMERALS_BASE;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
        save();
    }

    public void setName(String name) {
        this.name = name;
        save();
    }

    @Override
    public String getLevelName(int level) {
        if(levelNames.length > level) {
            return levelNames[level];
        }
        if(ROMAN_NUMERALS_BASE.length > level) {
            return ROMAN_NUMERALS_BASE[level];
        }
        return String.valueOf(level);
    }

    public void setLevelNames(String[] levelNames) {
        this.levelNames = levelNames.clone();
        save();
    }

    @Override
    public String getId() {
        return shortName;
    }

    public void setShortName(String shortName) {
        databaseBadgeManager.badgesByShortName.remove(this.shortName, this);
        this.shortName = shortName.toLowerCase();
        save();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        save();
    }

    public void delete() {
        if(id < 0) {
            return;
        }

        try {
            Connection connection = databaseBadgeManager.pool.getConnection();
            PreparedStatement preparedStatement;

            preparedStatement = connection.prepareStatement("DELETE FROM fb_badges WHERE badgeid = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            preparedStatement.close();

            databaseBadgeManager.badgesById.remove(id, this);
            databaseBadgeManager.badgesByShortName.remove(shortName, this);

            preparedStatement = connection.prepareStatement("DELETE FROM fb_badgexuser WHERE badgeid = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            preparedStatement.close();

            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void save() {
        try {
            Connection connection = databaseBadgeManager.pool.getConnection();
            PreparedStatement preparedStatement;
            if (id >= 0) {
                preparedStatement = connection.prepareStatement("UPDATE fb_badges SET shortname = ?, name = ?, description = ?, maxLevel = ?, levelNames = ? WHERE badgeid = ?");
                preparedStatement.setString(1, shortName);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, description);
                preparedStatement.setInt(4, maxLevel);
                if(levelNames != null) {
                    preparedStatement.setString(5, String.join(",", levelNames));
                } else {
                    preparedStatement.setString(5, "");
                }
                preparedStatement.setInt(6, id);

                preparedStatement.execute();
            } else {
                preparedStatement = connection.prepareStatement("INSERT INTO fb_badges (shortname, name, description, maxLevel, levelNames) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, shortName);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, description);
                preparedStatement.setInt(4, maxLevel);
                if(levelNames != null) {
                    preparedStatement.setString(5, String.join(",", levelNames));
                } else {
                    preparedStatement.setString(5, "");
                }
                preparedStatement.executeUpdate();

                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                id = keys.getInt(1);
                databaseBadgeManager.badgesById.put(id, this);
                databaseBadgeManager.badgesByShortName.put(shortName, this);
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
