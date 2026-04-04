package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import org.bukkit.configuration.file.YamlConfiguration;

public class MessagesFile {


	// -------------------------------------------------- //


	private final YamlConfiguration yamlConfiguration;


	// -------------------------------------------------- //


	public MessagesFile(YamlConfiguration yamlConfiguration) {
		this.yamlConfiguration = yamlConfiguration;
	}


	// -------------------------------------------------- //


	public String getMessagesVersion() {
		String version = yamlConfiguration.getString("messages-version");
		if (version != null) {
			return version;
		}
		return "0.0";
	}


	// -------------------------------------------------- //


	public String getPrefix() {
		return yamlConfiguration.getString("prefix");
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
