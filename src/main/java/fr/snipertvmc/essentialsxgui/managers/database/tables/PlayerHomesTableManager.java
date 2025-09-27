package fr.snipertvmc.essentialsxgui.managers.database.tables;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import fr.snipertvmc.essentialsxgui.utilities.type.JsonUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class PlayerHomesTableManager {


	// -------------------------------------------------- //


	public void initialize(boolean useSQLite) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "player_homes_data";

		String playerHomesData = useSQLite ?

				"CREATE TABLE IF NOT EXISTS " + tableName + " ("
						+ "profile_id INTEGER PRIMARY KEY AUTOINCREMENT,"

						+ "username TEXT NOT NULL UNIQUE,"
						+ "homes TEXT NOT NULL,"

						+ "created_at TEXT DEFAULT CURRENT_TIMESTAMP)" :

				"CREATE TABLE IF NOT EXISTS " + tableName + " ("
						+ "profile_id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"

						+ "username VARCHAR(16) NOT NULL UNIQUE,"
						+ "homes JSON NOT NULL,"

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


	public boolean isPlayerExists(String playerName) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "player_homes_data";

		String sql = "SELECT 1 FROM " + tableName + " WHERE username = ? LIMIT 1";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, playerName);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				return resultSet.next();
			}

		} catch (SQLException e) {
			throw new RuntimeException("Error verifying player's existence: " + playerName, e);
		}
	}


	// -------------------------------------------------- //


	public void insertPlayer(String playerName, Map<String, Object> homesRaw) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "player_homes_data";

		String sql = "INSERT INTO " + tableName + " (username, homes) VALUES (?, ?)";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, playerName);
			preparedStatement.setString(2, JsonUtils.mapToJson(homesRaw));
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Line insertion error: " + playerName, e);
		}
	}


	// -------------------------------------------------- //


	public Map<String, Object> fetchHomes(String playerName) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "player_homes_data";

		String sql = "SELECT homes FROM " + tableName + " WHERE username = ?";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, playerName);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return JsonUtils.jsonToMap(resultSet.getString("homes"));
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException("Error retrieving value: " + playerName, e);
		}

		return null;
	}


	// -------------------------------------------------- //


	public void updateHomes(String playerName, Map<String, Object> homesRaw) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "player_homes_data";

		String sql = "UPDATE " + tableName + " SET homes = ? WHERE username = ?";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, JsonUtils.mapToJson(homesRaw));
			preparedStatement.setString(2, playerName);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Error updating value: " + playerName, e);
		}
	}


	// -------------------------------------------------- //
}
