package fr.snipertvmc.essentialsxgui.utilities.data;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseUtils {


	// ---------------------------------------- //


    public static HikariDataSource connectDatabase(String host, String port, String database,
                                                   String user, String password,
                                                   String settings,
                                                   boolean isMariaDB) {

	    HikariConfig config = new HikariConfig();

	    String driverType = isMariaDB ? "mariadb" : "mysql";

	    // Connection settings
	    config.setJdbcUrl("jdbc:" + driverType + "://" + host + ":" + port + "/" + database);
	    config.setUsername(user);
	    config.setPassword(password);

	    // MariaDB JDBC Driver
	    config.setDriverClassName(isMariaDB ? "org.mariadb.jdbc.Driver" : "com.mysql.cj.jdbc.Driver");

	    // HikariCP settings
	    config.setMaximumPoolSize(10);
	    config.setMinimumIdle(10);
	    config.setMaxLifetime(1800000);
	    config.setKeepaliveTime(0);
	    config.setConnectionTimeout(5000);
	    config.setPoolName("EssentialsX-GUI-HikariCP");

	    // Additional properties
//			config.addDataSourceProperty("settings", settings);

	    return new HikariDataSource(config);
    }


	public static HikariDataSource connectDatabase(String sqliteFile) {

		HikariConfig config = new HikariConfig();

		config.setJdbcUrl("jdbc:sqlite:" + sqliteFile);
		config.setMaximumPoolSize(1);
		config.setPoolName("EssentialsX-GUI-SQLite");

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