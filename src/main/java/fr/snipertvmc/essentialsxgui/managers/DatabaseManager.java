package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.databases.EXGStorage;
import fr.snipertvmc.essentialsxgui.managers.database.storages.MySQLStorageManager;
import fr.snipertvmc.essentialsxgui.managers.database.storages.SQLiteStorageManager;
import fr.snipertvmc.essentialsxgui.managers.database.tables.KitsTableManager;
import fr.snipertvmc.essentialsxgui.managers.database.tables.PlayerHomesTableManager;
import fr.snipertvmc.essentialsxgui.managers.database.tables.WarpsTableManager;

public class DatabaseManager {


	// -------------------------------------------------- //


	private EXGStorage storage;

	private final KitsTableManager kitsTableManager = new KitsTableManager();
	private final PlayerHomesTableManager playerHomesTableManager = new PlayerHomesTableManager();
	private final WarpsTableManager warpsTableManager = new WarpsTableManager();


	// -------------------------------------------------- //


	public DatabaseManager() {
		updateDatabaseStorage();
	}


	public void updateDatabaseStorage() {

		String storageType = Main.getInstance().getConfiguration().getStorageType();
		if (storageType.equals("SQLite")) {
			Main.getInstance().getLibraryManager().loadLibraries("SQLite");
			storage = getSQLite();
			return;
		}

		if (storageType.equalsIgnoreCase("MariaDB")) {
			Main.getInstance().getLibraryManager().loadLibraries("MariaDB");
		} else {
			Main.getInstance().getLibraryManager().loadLibraries("MySQL");
		}

		storage = getMySQL();
	}


	// -------------------------------------------------- //


	public void connectAllDatabases() {
		getStorage().connect();
		playerHomesTableManager.initialize(storage.isSQLite());
		kitsTableManager.initialize(storage.isSQLite());
		warpsTableManager.initialize(storage.isSQLite());
	}


	public void disconnectAllDatabases() {
		getStorage().disconnect();
	}


	// -------------------------------------------------- //


	public EXGStorage getStorage() {
		return storage;
	}

	public KitsTableManager getKitsTableManager() {
		return kitsTableManager;
	}
	public PlayerHomesTableManager getPlayerHomesTableManager() {
		return playerHomesTableManager;
	}
	public WarpsTableManager getWarpsTableManager() {
		return warpsTableManager;
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
