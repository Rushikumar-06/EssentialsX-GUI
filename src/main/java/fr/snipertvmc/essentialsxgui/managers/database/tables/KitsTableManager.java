package fr.snipertvmc.essentialsxgui.managers.database.tables;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.type.JsonUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class KitsTableManager {


	// -------------------------------------------------- //


	public void initialize(boolean useSQLite) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "kits_data";

		String playerHomesData = useSQLite ?

				"CREATE TABLE IF NOT EXISTS " + tableName + " ("
						+ "kit_id INTEGER PRIMARY KEY AUTOINCREMENT,"

						+ "kit_name TEXT NOT NULL UNIQUE,"
						+ "kit_data TEXT NOT NULL,"

						+ "created_at TEXT DEFAULT CURRENT_TIMESTAMP)" :

				"CREATE TABLE IF NOT EXISTS " + tableName + " ("
						+ "kit_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"

						+ "kit_name VARCHAR(16) NOT NULL UNIQUE,"
						+ "kit_data JSON NOT NULL,"

						+ "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
						+ "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP)";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(playerHomesData)) {

			preparedStatement.execute();

		} catch (SQLException e) {
			ConsoleLogger.error("Table creation error " + tableName + " : " + e.getMessage());
		}
	}


	// -------------------------------------------------- //


	public boolean isKitExists(String kitName) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "kits_data";

		String sql = "SELECT 1 FROM " + tableName + " WHERE kit_name = ? LIMIT 1";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, kitName);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return resultSet.next();
			}

		} catch (SQLException e) {
			throw new RuntimeException("Error verifying kit existence: " + kitName, e);
		}
	}


	// -------------------------------------------------- //


	public void insertKit(String kitName, Map<String, Object> kitsRaw) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "kits_data";

		String sql = "INSERT INTO " + tableName + " (kit_name, kit_data) VALUES (?, ?)";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, kitName);
			preparedStatement.setString(2, JsonUtils.mapToJson(kitsRaw));
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Line insertion error: " + kitName, e);
		}
	}


	// -------------------------------------------------- //


	public Map<String, Object> fetchKits() {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "kits_data";

		String sql = "SELECT kit_name, kit_data FROM " + tableName;

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			Map<String, Object> kits = new HashMap<>();

			while (resultSet.next()) {
				kits.put(resultSet.getString("kit_name"), JsonUtils.jsonToMap(resultSet.getString("kit_data")));
			}

			return kits;

		} catch (SQLException e) {
			throw new RuntimeException("Error retrieving all kits", e);
		}
	}


	// -------------------------------------------------- //


	public void updateKit(String kitName, Map<String, Object> kitsRaw) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "kits_data";

		String sql = "UPDATE " + tableName + " SET kit_data = ? WHERE kit_name = ?";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setObject(1, JsonUtils.mapToJson(kitsRaw));
			preparedStatement.setString(2, kitName);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error updating the kit: " + kitName, e);
		}
	}


	public void updateKits(Map<String, Object> kits) {
		for (Map.Entry<String, Object> entry : kits.entrySet()) {
			if (!isKitExists(entry.getKey())) {
				insertKit(entry.getKey(), (Map<String, Object>) entry.getValue());
				continue;
			}
			updateKit(entry.getKey(), (Map<String, Object>) entry.getValue());
		}
	}


	// -------------------------------------------------- //
}
