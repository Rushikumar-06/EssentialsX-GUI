package fr.snipertvmc.essentialsxgui.managers.database.storages;

import com.zaxxer.hikari.HikariDataSource;
import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.models.EXGStorage;
import fr.snipertvmc.essentialsxgui.utilities.data.DatabaseUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class SQLiteStorageManager implements EXGStorage {


	// -------------------------------------------------- //


	private HikariDataSource dataSource;
	private File sqliteFile;


	// -------------------------------------------------- //


	public void connect() {

		if (isConnected()) {
			return;
		}


		try {
			sqliteFile = new File(Main.getInstance().getDataFolder(), "database.sqlite");
			sqliteFile.createNewFile();

			dataSource = DatabaseUtils.connectDatabase(
					sqliteFile.getAbsolutePath(),
					Main.getInstance().getConfiguration().getStorageSettings()
			);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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
		return false;
	}
	public boolean isMariaDB() {
		return false;
	}
	public boolean isSQLite() {
		return true;
	}


	// -------------------------------------------------- //
}
