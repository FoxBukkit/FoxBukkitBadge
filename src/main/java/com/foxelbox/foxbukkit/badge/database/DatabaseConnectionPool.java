package com.foxelbox.foxbukkit.badge.database;

import com.foxelbox.foxbukkit.badge.FoxBukkitBadge;
import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.StackKeyedObjectPoolFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DatabaseConnectionPool {
    private PoolingDataSource dataSource;

    public DatabaseConnectionPool(FoxBukkitBadge instance) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch(Exception e) {
            System.err.println("Error loading JBBC MySQL: " + e.toString());
        }

        GenericObjectPool connectionPool = new GenericObjectPool(null);
        connectionPool.setMaxActive(10);
        connectionPool.setMaxIdle(5);
        connectionPool.setTestOnBorrow(true);
        connectionPool.setTestOnReturn(true);
        connectionPool.setTestWhileIdle(true);

        ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(instance.configuration.getValue("database-uri", "jdbc:mysql://localhost:3306/foxbukkit_database"), instance.configuration.getValue("database-user", "root"), instance.configuration.getValue("database-password", "password"));
        PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(
                connectionFactory,
                connectionPool,
                new StackKeyedObjectPoolFactory(),
                "SELECT 1",
                false,
                true
        );
        poolableConnectionFactory.setValidationQueryTimeout(3);

        dataSource = new PoolingDataSource(connectionPool);

        try {
            initialize();
        } catch (SQLException exc) {
            System.err.println("Error initializing MySQL Database");
            exc.printStackTrace();
        }
    }

    public void initialize() throws SQLException {
        Connection connection = getConnection();
        _runSQL(connection, "CREATE TABLE IF NOT EXISTS `fb_badges` (\n" +
                "  `id` int(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `shortname` varbinary(32) NOT NULL,\n" +
                "  `name` varbinary(255) NOT NULL,\n" +
                "  `description` blob NOT NULL,\n" +
                "  `maxLevel` int(11) NOT NULL,\n" +
                "  `levelNames` blob NOT NULL,\n" +
                "  UNIQUE KEY `shortname` (`shortname`)\n" +
                "  PRIMARY KEY (`badgeid`)\n" +
                ") ENGINE=InnoDB;");
        _runSQL(connection, "CREATE TABLE IF NOT EXISTS `fb_badgexuser` (\n" +
                "  `user` binary(36) NOT NULL,\n" +
                "  `badgeid` int(11) NOT NULL,\n" +
                "  `level` int(11) NOT NULL,\n" +
                "  PRIMARY KEY (`user`,`badgeid`)\n" +
                ") ENGINE=InnoDB;");
        connection.close();
    }

    private void _runSQL(Connection connection, String sql) throws SQLException {
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.execute();
        stmt.close();
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}