package com.foxelbox.foxbukkit.badge;

import com.foxelbox.dependencies.config.Configuration;
import com.foxelbox.foxbukkit.badge.commands.BAddCommand;
import com.foxelbox.foxbukkit.badge.commands.BListCommand;
import com.foxelbox.foxbukkit.badge.commands.BManageCommand;
import com.foxelbox.foxbukkit.badge.commands.BadgeCommand;
import com.foxelbox.foxbukkit.badge.database.BadgeManager;
import com.foxelbox.foxbukkit.badge.database.DatabaseConnectionPool;
import org.bukkit.plugin.java.JavaPlugin;

public class FoxBukkitBadge extends JavaPlugin {
    public Configuration configuration;
    public DatabaseConnectionPool pool;
    public BadgeManager badgeManager;

    @Override
    public void onEnable() {
        super.onEnable();

        configuration = new Configuration(getDataFolder());
        pool = new DatabaseConnectionPool(this);
        badgeManager = new BadgeManager(pool);

        getServer().getPluginCommand("badd").setExecutor(new BAddCommand(this));
        getServer().getPluginCommand("blist").setExecutor(new BListCommand(this));
        getServer().getPluginCommand("bmanage").setExecutor(new BManageCommand(this));
        getServer().getPluginCommand("badge").setExecutor(new BadgeCommand(this));
    }
}
