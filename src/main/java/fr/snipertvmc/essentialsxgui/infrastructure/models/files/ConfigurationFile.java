package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public class ConfigurationFile {


	// -------------------------------------------------- //


	private final YamlConfiguration yamlConfiguration;


	// -------------------------------------------------- //


	public ConfigurationFile(YamlConfiguration yamlConfiguration) {
		this.yamlConfiguration = yamlConfiguration;
	}


	// -------------------------------------------------- //


	public String getConfigVersion() {
		String version = yamlConfiguration.getString("config-version");
		if (version != null) {
			return version;
		}
		return "0.0";
	}


	public boolean isDetailedLoading() {
		return yamlConfiguration.getBoolean("detailed-loading");
	}


	// -------------------------------------------------- //


	public boolean hasEssentialsXGUICommand(Player player) {
		return player.hasPermission(yamlConfiguration.getString("permissions.commands.essentialsxgui", "unknownPermission"));
	}


	public boolean hasKitsAdminAccess(Player player) {
		return player.hasPermission(yamlConfiguration.getString("kits-admin-access"));
	}


	// -------------------------------------------------- //


	public String getString(String path) {
		return yamlConfiguration.getString(path);
	}


	public String getString(String path, String defaultValue) {
		return yamlConfiguration.getString(path, defaultValue);
	}


	public double getNumber(String path) {
		return yamlConfiguration.getDouble(path);
	}


	public double getNumber(String path, double defaultValue) {
		return yamlConfiguration.getDouble(path, defaultValue);
	}


	public boolean getBoolean(String path) {
		return yamlConfiguration.getBoolean(path);
	}


	public boolean getBoolean(String path, boolean defaultValue) {
		return yamlConfiguration.getBoolean(path, defaultValue);
	}


	// -------------------------------------------------- //
}
