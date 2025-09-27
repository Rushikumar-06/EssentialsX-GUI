package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGStorage;
import fr.snipertvmc.essentialsxgui.managers.database.storages.MySQLStorageManager;
import fr.snipertvmc.essentialsxgui.managers.database.storages.SQLiteStorageManager;
import fr.snipertvmc.essentialsxgui.managers.database.tables.KitsTableManager;
import fr.snipertvmc.essentialsxgui.managers.database.tables.PlayerHomesTableManager;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;

import java.sql.DriverManager;

public class DatabaseManager {


	// -------------------------------------------------- //


	private boolean mariaDBDriver = false;

	private EXGStorage storage;

	private final PlayerHomesTableManager playerHomesTableManager = new PlayerHomesTableManager();
	private final KitsTableManager kitsTableManager = new KitsTableManager();


	// -------------------------------------------------- //


	public DatabaseManager() {
		initialize();
	}


	public void initialize() {

		String storageType = Main.getInstance().getConfiguration().getStorageType();

		if (storageType.equalsIgnoreCase("MySQL") || (storageType.equalsIgnoreCase("MariaDB"))) {

			if (storageType.equalsIgnoreCase("MariaDB") && !isMariaDBDriverLoaded()) {
				registerMariaDBDriver();
			}

			storage = getMySQL();
			return;
		}

		storage = getSQLite();

		if (isMariaDBDriverLoaded()) {
			unregisterMariaDBDriver();
		}
	}


	// -------------------------------------------------- //


	public void registerMariaDBDriver() {

		try {
			Class.forName("org.mariadb.jdbc.Driver");
			mariaDBDriver = true;

			boolean detailedLoading = Main.getInstance().getConfiguration().isDetailedLoading();
			if (detailedLoading) { ConsoleLogger.console("\t§6EssentialsX-GUI: §7MariaDB JDBC Driver §fregistered §7successfully."); }

		} catch (Exception ignored) {
		}
	}


	public void unregisterMariaDBDriver() {

		try {
			DriverManager.deregisterDriver(DriverManager.getDriver("jdbc:mariadb://"));
			mariaDBDriver = false;

			boolean detailedLoading = Main.getInstance().getConfiguration().isDetailedLoading();
			if (detailedLoading) { ConsoleLogger.info("\t§6EssentialsX-GUI: §7MariaDB JDBC Driver §funregistered §7successfully."); }

		} catch (Exception ignored) {
		}
	}


	public boolean isMariaDBDriverLoaded() {
		return mariaDBDriver;
	}


	// -------------------------------------------------- //


	public void connectAllDatabases() {
		getStorage().connect();
		playerHomesTableManager.initialize(storage.isSQLite());
		kitsTableManager.initialize(storage.isSQLite());
	}


	public void disconnectAllDatabases() {
		getStorage().disconnect();
	}


	// -------------------------------------------------- //


	public EXGStorage getStorage() {
		return storage;
	}

	public PlayerHomesTableManager getPlayerHomesTableManager() {
		return playerHomesTableManager;
	}
	public KitsTableManager getKitsTableManager() {
		return kitsTableManager;
	}


	// -------------------------------------------------- //


	private final MySQLStorageManager mySQLStorageManager = new MySQLStorageManager();
	private final SQLiteStorageManager sqLiteStorageManager = new SQLiteStorageManager();

	private MySQLStorageManager getMySQL() {
		return mySQLStorageManager;
	}
	private SQLiteStorageManager getSQLite() {
		return sqLiteStorageManager;
	}


	// -------------------------------------------------- //
}
