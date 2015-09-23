package com.foxelbox.foxbukkit.badge.database;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class BadgeManager {
    final DatabaseConnectionPool pool;

    final HashMap<Integer, Badge> badgesById;
    final HashMap<String, Badge> badgesByShortName;

    public BadgeManager(DatabaseConnectionPool pool) {
        this.pool = pool;
        this.badgesById = new HashMap<>();
        this.badgesByShortName = new HashMap<>();
        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, shortname, name FROM fb_badges");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                badgesById.put(resultSet.getInt(1), new Badge(this, resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
                badgesByShortName.put(resultSet.getString(2), new Badge(this, resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3)));
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Badge> getAllBadges() {
        return badgesById.values();
    }

    public Badge getBadgeById(int id) {
        return badgesById.get(id);
    }

    public Badge getBadgeByShortName(String shortName) {
        return badgesByShortName.get(shortName);
    }

    public Badge newBadge() {
        return new Badge(this);
    }

    public Collection<BadgePlayer> getBadgesForPlayer(Player player) {
        ArrayList<BadgePlayer> ret = new ArrayList<>();
        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, level FROM fb_badgexuser WHERE user = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                ret.add(new BadgePlayer(getBadgeById(resultSet.getInt(1)), resultSet.getInt(2)));
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public void setBadgeForPlayer(Player player, Badge badge, int level) {
        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement;
            if (level > 0) {
                preparedStatement = connection.prepareStatement("REPLACE INTO fb_badgexuser (user, badgeid, level) VALUES (?, ?, ?)");
                preparedStatement.setInt(3, level);
            } else {
                preparedStatement = connection.prepareStatement("DELETE FROM fb_badgexuser WHERE user = ? AND badgeid = ?");
            }
            preparedStatement.setString(1, player.getUniqueId().toString());
            preparedStatement.setInt(2, badge.getId());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
