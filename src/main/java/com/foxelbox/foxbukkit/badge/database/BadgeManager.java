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

    final HashMap<Integer, DatabaseBadge> badgesById;
    final HashMap<String, DatabaseBadge> badgesByShortName;

    public BadgeManager(DatabaseConnectionPool pool) {
        this.pool = pool;
        this.badgesById = new HashMap<>();
        this.badgesByShortName = new HashMap<>();
        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, shortname, name, levelNames FROM fb_badges");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                DatabaseBadge badge = new DatabaseBadge(this, resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4));
                badgesById.put(resultSet.getInt(1), badge);
                badgesByShortName.put(resultSet.getString(2), badge);
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<DatabaseBadge> getAllBadges() {
        return badgesById.values();
    }

    public DatabaseBadge getBadgeById(int id) {
        return badgesById.get(id);
    }

    public DatabaseBadge getBadgeByShortName(String shortName) {
        return badgesByShortName.get(shortName);
    }

    public DatabaseBadge newBadge() {
        return new DatabaseBadge(this);
    }

    public Collection<DatabaseBadgePlayer> getBadgesForPlayer(Player player) {
        ArrayList<DatabaseBadgePlayer> ret = new ArrayList<>();
        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, level FROM fb_badgexuser WHERE user = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                ret.add(new DatabaseBadgePlayer(getBadgeById(resultSet.getInt(1)), resultSet.getInt(2)));
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public void setBadgeForPlayer(Player player, DatabaseBadge badge, int level) {
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
