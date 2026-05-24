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

public class WarpsTableManager {


	// -------------------------------------------------- //


	public void initialize(boolean useSQLite) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "warps_data";

		String playerHomesData = useSQLite ?

				"CREATE TABLE IF NOT EXISTS " + tableName + " ("
						+ "warp_id INTEGER PRIMARY KEY AUTOINCREMENT,"

						+ "warp_name TEXT NOT NULL UNIQUE,"
						+ "warp_data TEXT NOT NULL,"

						+ "created_at TEXT DEFAULT CURRENT_TIMESTAMP)" :

				"CREATE TABLE IF NOT EXISTS " + tableName + " ("
						+ "warp_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"

						+ "warp_name VARCHAR(16) NOT NULL UNIQUE,"
						+ "warp_data JSON NOT NULL,"

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


	public boolean isWarpExists(String warpName) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "warps_data";

		String sql = "SELECT 1 FROM " + tableName + " WHERE warp_name = ? LIMIT 1";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, warpName);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return resultSet.next();
			}

		} catch (SQLException e) {
			throw new RuntimeException("Error verifying warp existence: " + warpName, e);
		}
	}


	// -------------------------------------------------- //


	public void insertWarp(String warpName, Map<String, Object> warpsRaw) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "warps_data";

		String sql = "INSERT INTO " + tableName + " (warp_name, warp_data) VALUES (?, ?)";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, warpName);
			preparedStatement.setString(2, JsonUtils.mapToJson(warpsRaw));
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Line insertion error: " + warpName, e);
		}
	}


	// -------------------------------------------------- //


	public Map<String, Object> fetchWarps() {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "warps_data";

		String sql = "SELECT warp_name, warp_data FROM " + tableName;

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			ResultSet resultSet = preparedStatement.executeQuery();

			Map<String, Object> warps = new HashMap<>();

			while (resultSet.next()) {
				warps.put(resultSet.getString("warp_name"), JsonUtils.jsonToMap(resultSet.getString("warp_data")));
			}

			return warps;

		} catch (SQLException e) {
			throw new RuntimeException("Error retrieving all warps", e);
		}
	}


	// -------------------------------------------------- //


	public void updateWarp(String warpName, Map<String, Object> warpsRaw) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "warps_data";

		String sql = "UPDATE " + tableName + " SET warp_data = ? WHERE warp_name = ?";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setObject(1, JsonUtils.mapToJson(warpsRaw));
			preparedStatement.setString(2, warpName);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error updating the warp: " + warpName, e);
		}
	}


	public void updateWarps(Map<String, Object> warps) {
		for (Map.Entry<String, Object> entry : warps.entrySet()) {
			if (!isWarpExists(entry.getKey())) {
				insertWarp(entry.getKey(), (Map<String, Object>) entry.getValue());
				continue;
			}
			updateWarp(entry.getKey(), (Map<String, Object>) entry.getValue());
		}
	}


	// -------------------------------------------------- //
}
