package com.foxelbox.foxbukkit.badge.database;

import java.sql.*;

public class Badge {
    private int id = -1;
    private String shortName;
    private String name;

    private final BadgeManager badgeManager;

    Badge(BadgeManager badgeManager) {
        this.badgeManager = badgeManager;
    }

    Badge(BadgeManager badgeManager, int id, String shortName, String name) {
        this(badgeManager);
        this.id = id;
        this.shortName = shortName;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        save();
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        badgeManager.badgesByShortName.remove(this.shortName, this);
        this.shortName = shortName;
        save();
    }

    public void delete() {
        if(id < 0) {
            return;
        }

        try {
            Connection connection = badgeManager.pool.getConnection();
            PreparedStatement preparedStatement;

            preparedStatement = connection.prepareStatement("DELETE FROM fb_badges WHERE badgeid = ?");
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            preparedStatement.close();

            badgeManager.badgesById.remove(id, this);
            badgeManager.badgesByShortName.remove(shortName, this);

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
            Connection connection = badgeManager.pool.getConnection();
            PreparedStatement preparedStatement;
            if (id >= 0) {
                preparedStatement = connection.prepareStatement("UPDATE fb_badges SET shortname = ?, name = ? WHERE badgeid = ?");
                preparedStatement.setString(1, shortName);
                preparedStatement.setString(2, name);
                preparedStatement.setInt(3, id);
                preparedStatement.execute();
            } else {
                preparedStatement = connection.prepareStatement("INSERT INTO fb_badges (shortname, name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, shortName);
                preparedStatement.setString(2, name);
                preparedStatement.executeUpdate();

                ResultSet keys = preparedStatement.getGeneratedKeys();
                keys.next();
                id = keys.getInt(1);
                badgeManager.badgesById.put(id, this);
                badgeManager.badgesByShortName.put(shortName, this);
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
