package fr.snipertvmc.essentialsxgui.utilities.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseUtils {


	// ---------------------------------------- //


    public static HikariDataSource connectDatabase(String host, String port, String database,
                                                   String user, String password,
                                                   String settings,
												   long maximumPoolSize, long minimumIdle,
												   long maxLifetime, long keepaliveTime, long connectionTimeout,
                                                   boolean isMariaDB) {

	    HikariConfig config = new HikariConfig();

	    String driverType = isMariaDB ? "mariadb" : "mysql";

	    // Connection settings
	    config.setJdbcUrl("jdbc:" + driverType + "://" + host + ":" + port + "/" + database + (settings.isEmpty() ? "" : settings));
	    config.setUsername(user);
	    config.setPassword(password);

	    // JDBC Driver
	    config.setDriverClassName(isMariaDB ? "org.mariadb.jdbc.Driver" : "com.mysql.cj.jdbc.Driver");

	    // HikariCP settings
	    config.setMaximumPoolSize(((Number) maximumPoolSize).intValue());
		config.setMinimumIdle(((Number) minimumIdle).intValue());
		config.setMaxLifetime(maxLifetime);
		config.setKeepaliveTime(keepaliveTime);
		config.setConnectionTimeout(connectionTimeout);
	    config.setPoolName("EssentialsX-GUI-HikariCP");

	    return new HikariDataSource(config);
    }


	public static HikariDataSource connectDatabase(String sqliteFile) {

		HikariConfig config = new HikariConfig();

		// Connection settings
		config.setJdbcUrl("jdbc:sqlite:" + sqliteFile);

		// JDBC Driver
		config.setDriverClassName("org.sqlite.JDBC");

		// HikariCP settings
		config.setMaximumPoolSize(1);
		config.setPoolName("EssentialsX-GUI-SQLite");

		// Connection test query
		config.setConnectionTestQuery("SELECT 1");

		return new HikariDataSource(config);
	}


	// ---------------------------------------- //


    public static void disconnectDatabase(HikariDataSource database) {
		if (isConnected(database)) {
		    database.close();
	    }
    }


	// ---------------------------------------- //


    public static boolean isConnected(HikariDataSource database) {
	    return database != null && !database.isClosed() && database.isRunning();
    }


	// ---------------------------------------- //
}