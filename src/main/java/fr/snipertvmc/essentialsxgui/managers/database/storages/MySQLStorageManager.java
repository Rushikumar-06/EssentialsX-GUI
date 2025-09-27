package fr.snipertvmc.essentialsxgui.managers.database.storages;

import com.zaxxer.hikari.HikariDataSource;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGStorage;
import fr.snipertvmc.essentialsxgui.utilities.data.DatabaseUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class MySQLStorageManager implements EXGStorage {


	// -------------------------------------------------- //


	private HikariDataSource dataSource;


	// -------------------------------------------------- //


	public void connect() {

		if (isConnected()) {
			return;
		}

		this.dataSource = DatabaseUtils.connectDatabase(
				Main.getInstance().getConfiguration().getStorageHost(),
				Main.getInstance().getConfiguration().getStoragePort(),
				Main.getInstance().getConfiguration().getStorageDatabase(),

				Main.getInstance().getConfiguration().getStorageUsername(),
				Main.getInstance().getConfiguration().getStoragePassword(),
				"",

				isMariaDB()
		);
	}


	public void disconnect() {

		if (!isConnected()) {
			return;
		}

		DatabaseUtils.disconnectDatabase(this.dataSource);
		this.dataSource = null;
	}


	public boolean isConnected() {
		return DatabaseUtils.isConnected(this.dataSource);
	}


	// -------------------------------------------------- //


	public Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}


	public boolean isMySQL() {
		return true;
	}
	public boolean isMariaDB() {
		return Main.getInstance().getDatabaseManager().isMariaDBDriverLoaded();
	}
	public boolean isSQLite() {
		return false;
	}


	// -------------------------------------------------- //
}
