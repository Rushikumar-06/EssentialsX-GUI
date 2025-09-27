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
			ConsoleLogger.error("Erreur lors de la création de la table " + tableName + " : " + e.getMessage());
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
			throw new RuntimeException("Erreur lors de la vérification de l'existence du kit: " + kitName, e);
		}
	}


	// -------------------------------------------------- //


	public void insertKit(String kit_name, Map<String, Object> kitDataRaw) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "kits_data";

		String sql = "INSERT INTO " + tableName + " (kit_name, kit_data) VALUES (?, ?)";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, kit_name);
			preparedStatement.setString(2, JsonUtils.mapToJson(kitDataRaw));
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Erreur lors de l'insertion de la ligne: " + kit_name, e);
		}
	}


	// -------------------------------------------------- //


	public String fetchKit(String kit_name) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "kits_data";

		String sql = "SELECT kit_data FROM " + tableName + " WHERE kit_name = ?";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			preparedStatement.setString(1, kit_name);
			try (ResultSet resultSet = preparedStatement.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString("kit_data");
				}
			}

		} catch (SQLException e) {
			throw new RuntimeException("Erreur lors de la récupération du kit: " + kit_name, e);
		}

		return null;
	}


	public Map<String, Object> fetchAllKits() {

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
			throw new RuntimeException("Erreur lors de la récupération de tous les kits", e);
		}
	}


	// -------------------------------------------------- //


	public void updateKit(String kit_name, String kitDataJson) {

		String tableName = Main.getInstance().getConfiguration().getStorageTablePrefix() + "kits_data";

		String sql = "UPDATE " + tableName + " SET kit_data = ? WHERE kit_name = ?";

		try (Connection connection = Main.getInstance().getDatabaseManager().getStorage().getConnection();
		     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

			ConsoleLogger.info("debug1: " + kitDataJson);
			ConsoleLogger.info("debug2: " + kit_name);

			preparedStatement.setObject(1, kitDataJson);
			preparedStatement.setString(2, kit_name);
			preparedStatement.executeUpdate();

		} catch (SQLException e) {
			throw new RuntimeException("Erreur lors de la mise à jour du kit: " + kit_name, e);
		}
	}


	public void updateKits(Map<String, Object> kits) {

		ConsoleLogger.info("debug3: " + kits.toString());

		for (Map.Entry<String, Object> entry : kits.entrySet()) {
			updateKit(entry.getKey(), JsonUtils.mapToJson((Map<String, Object>) entry.getValue()));
		}
	}


	// -------------------------------------------------- //
}
