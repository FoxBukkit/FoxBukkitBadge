package com.foxelbox.foxbukkit.badge.database;

import com.foxelbox.foxbukkit.badge.Badge;
import com.foxelbox.foxbukkit.badge.BadgeDescriptor;
import com.foxelbox.foxbukkit.badge.BadgeManager;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class DatabaseBadgeManager implements BadgeManager {
    final DatabaseConnectionPool pool;

    final HashMap<Integer, DatabaseBadgeDescriptor> badgesById;
    final HashMap<String, DatabaseBadgeDescriptor> badgesByShortName;

    public DatabaseBadgeManager(DatabaseConnectionPool pool) {
        this.pool = pool;
        this.badgesById = new HashMap<>();
        this.badgesByShortName = new HashMap<>();
        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, shortname, name, description, maxLevel, levelNames FROM fb_badges");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                String shortName = resultSet.getString(2).toLowerCase();
                DatabaseBadgeDescriptor badge = new DatabaseBadgeDescriptor(this, resultSet.getInt(1), shortName, resultSet.getString(3), resultSet.getString(4), resultSet.getInt(5), resultSet.getString(6));
                badgesById.put(resultSet.getInt(1), badge);
                badgesByShortName.put(shortName, badge);
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseBadgeDescriptor getBadgeById(int id) {
        return badgesById.get(id);
    }

    public DatabaseBadgeDescriptor getBadgeByShortName(String shortName) {
        return badgesByShortName.get(shortName);
    }

    public DatabaseBadgeDescriptor newBadge() {
        return new DatabaseBadgeDescriptor(this);
    }

    public Collection<Badge> getBadgesForPlayer(Player player) {
        ArrayList<Badge> ret = new ArrayList<>();
        try {
            Connection connection = pool.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, level FROM fb_badgexuser WHERE user = ?");
            preparedStatement.setString(1, player.getUniqueId().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                ret.add(new DatabaseBadge(getBadgeById(resultSet.getInt(1)), resultSet.getInt(2)));
            }
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ret;
    }

    public void setBadgeForPlayer(Player player, DatabaseBadge badge) {
        setBadgeForPlayer(player, badge.badge, badge.level);
    }

    public void setBadgeForPlayer(Player player, DatabaseBadgeDescriptor badge, int level) {
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
            preparedStatement.setInt(2, badge.getDatabaseId());
            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<BadgeDescriptor> getBadges() {
        return (Collection<BadgeDescriptor>)(Collection)badgesById.values();
    }

    @Override
    public BadgeDescriptor getBadge(String shortName) {
        return badgesByShortName.get(shortName.toLowerCase());
    }
}
