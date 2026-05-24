package fr.snipertvmc.essentialsxgui.infrastructure.models.databases;

import java.sql.Connection;
import java.sql.SQLException;

public interface EXGStorage {


	// -------------------------------------------------- //


	void connect();
	void disconnect();
	boolean isConnected();
	Connection getConnection() throws SQLException;
	boolean isMySQL();
	boolean isMariaDB();
	boolean isSQLite();


	// -------------------------------------------------- //
}
