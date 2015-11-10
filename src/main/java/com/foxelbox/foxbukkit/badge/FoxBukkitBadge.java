package com.foxelbox.foxbukkit.badge;

import com.foxelbox.dependencies.config.Configuration;
import com.foxelbox.foxbukkit.badge.commands.BAddCommand;
import com.foxelbox.foxbukkit.badge.commands.BListCommand;
import com.foxelbox.foxbukkit.badge.commands.BManageCommand;
import com.foxelbox.foxbukkit.badge.commands.BadgesCommand;
import com.foxelbox.foxbukkit.badge.database.DatabaseBadgeManager;
import com.foxelbox.foxbukkit.badge.database.DatabaseConnectionPool;
import org.bukkit.plugin.java.JavaPlugin;

public class FoxBukkitBadge extends JavaPlugin {
    public Configuration configuration;
    public DatabaseConnectionPool pool;
    public DatabaseBadgeManager databaseBadgeManager;

    public MergingBadgeManager globalBadgeManager;

    public BadgeManager getGlobalBadgeManager() {
        return globalBadgeManager;
    }

    public void addBadgeManager(BadgeManager badgeManager) {
        globalBadgeManager.badgeManagers.add(badgeManager);
    }

    public void removeBadgeManager(BadgeManager badgeManager) {
        globalBadgeManager.badgeManagers.remove(badgeManager);
    }

    @Override
    public void onEnable() {
        super.onEnable();

        configuration = new Configuration(getDataFolder());
        pool = new DatabaseConnectionPool(this);
        databaseBadgeManager = new DatabaseBadgeManager(pool);

        getServer().getPluginCommand("badd").setExecutor(new BAddCommand(this));
        getServer().getPluginCommand("blist").setExecutor(new BListCommand(this));
        getServer().getPluginCommand("bmanage").setExecutor(new BManageCommand(this));
        getServer().getPluginCommand("badges").setExecutor(new BadgesCommand(this));

        globalBadgeManager = new MergingBadgeManager();
        addBadgeManager(databaseBadgeManager);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        globalBadgeManager.badgeManagers.clear();
    }
}
