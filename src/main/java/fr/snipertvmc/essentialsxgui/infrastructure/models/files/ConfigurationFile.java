package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import com.cryptomorin.xseries.XMaterial;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;
import org.bukkit.configuration.file.YamlConfiguration;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationFile {


	// -------------------------------------------------- //


	private final YamlConfiguration yamlConfiguration;


	// -------------------------------------------------- //


	public ConfigurationFile(YamlConfiguration yamlConfiguration) {
		this.yamlConfiguration = yamlConfiguration;
	}


	// -------------------------------------------------- //


	public String getConfigVersion() {
		return yamlConfiguration.getString("general.configVersion", "Config version not found");
	}

	public boolean isDetailedLoading() {
		return yamlConfiguration.getBoolean("general.detailedLoading", true);
	}

	public boolean checkForUpdates() {
		return yamlConfiguration.getBoolean("general.checkForUpdates", true);
	}

	public ZoneId getDateTimezone() {
		String zoneIdString = yamlConfiguration.getString("general.timezone", ZoneId.systemDefault().toString());
		try {
			return ZoneId.of(zoneIdString);

		} catch (Exception exception) {
			ConsoleLogger.warn("The timezone '" + zoneIdString + "' is not valid. Using the system default timezone instead.");
			return ZoneId.systemDefault();
		}
	}


	// -------------------------------------------------- //


	public boolean isHomesModuleEnabled() {
		return yamlConfiguration.getBoolean("homes.enabled", false);
	}

	public boolean isKitsModuleEnabled() {
		return yamlConfiguration.getBoolean("kits.enabled", false);
	}

	public boolean isWarpsModuleEnabled() {
		return yamlConfiguration.getBoolean("warps.enabled", false);
	}


	// -------------------------------------------------- //


	public EXGEntryType getEntryType(String module, String entry) {
		String typeString = yamlConfiguration.getString(module + "." + entry, "CHAT");
		try {
			return EXGEntryType.valueOf(typeString);
		} catch (IllegalArgumentException exception) {
			ConsoleLogger.warn("The entry type '" + typeString + "' is not valid for the entry '" + entry + "' in the module '" + module + "'. Using the default type 'CHAT' instead.");
			return EXGEntryType.CHAT;
		}
	}

	public List<Pair<XMaterial, Byte>> getMaterialsList(String path) {

		List<String> materialsStringList = yamlConfiguration.getStringList(path);
		List<Pair<XMaterial, Byte>> materialsList = new ArrayList<>();

		for (String materialString : materialsStringList) {
			String[] parts = materialString.split(":");
			String materialName = parts[0];
			byte dataValue = 0;
			if (parts.length > 1) {
				try {
					dataValue = Byte.parseByte(parts[1]);
				} catch (NumberFormatException exception) {
					ConsoleLogger.warn("The data value '" + parts[1] + "' is not a valid byte for the material '" + materialName + "' in the path '" + path + "'. Using the default data value '0' instead.");
				}
			}
			if (XMaterial.matchXMaterial(materialName).isPresent()) {
				XMaterial material = XMaterial.matchXMaterial(materialName).get();
				materialsList.add(new Pair<>(material, dataValue));
			} else {
				ConsoleLogger.warn("The material '" + materialName + "' is not valid in the path '" + path + "'. Using the default material 'BEDROCK' instead.");
				materialsList.add(new Pair<>(XMaterial.BEDROCK, dataValue));
			}
		}

		return materialsList;
	}


	// -------------------------------------------------- //


	public String getAnvilTypeTextHere() {
		return yamlConfiguration.getString("general.anvilTypeHereText", "Type here");
	}

	public int getMinNameLength() {
		return yamlConfiguration.getInt("general.minNameLength", 3);
	}

	public int getMaxNameLength() {
		return yamlConfiguration.getInt("general.maxNameLength", 32);
	}

	public boolean mustOpenKitAdminViewByDefault() {
		return yamlConfiguration.getBoolean("general.openKitAdminViewByDefault", false);
	}

	public boolean mustOpenWarpAdminViewByDefault() {
		return yamlConfiguration.getBoolean("general.openWarpAdminViewByDefault", false);
	}


	public int getDelayForTypingInChat() {
		return yamlConfiguration.getInt("general.delayForTypingInChat", 10);
	}

	public boolean skipDataEntryProcess() {
		return yamlConfiguration.getBoolean("general.skipDataEntryProcess", false);
	}

	public boolean acceptOnlyMiniMessageFormatInEntries() {
		return yamlConfiguration.getBoolean("general.acceptOnlyMiniMessageFormatInEntries", false);
	}

	public String getInstantCreationDefaultHomeName() {
		return yamlConfiguration.getString("general.instantCreationDefaultValues.home_name", "home_%number%");
	}
	public String getInstantCreationDefaultKitName() {
		return yamlConfiguration.getString("general.instantCreationDefaultValues.kit_name", "kit_%number%");
	}
	public long getInstantCreationDefaultKitDelay() {
		return yamlConfiguration.getLong("general.instantCreationDefaultValues.kit_delay", 3600L);
	}
	public String getInstantCreationDefaultWarpName() {
		return yamlConfiguration.getString("general.instantCreationDefaultValues.warp_name", "warp_%number%");
	}


	// -------------------------------------------------- //


	public boolean areSoundsEnabled() {
		return yamlConfiguration.getBoolean("sounds.enabled", true);
	}

	public String getSound(String path) {
		return yamlConfiguration.getString("sounds." + path, null);
	}


	// -------------------------------------------------- //


	public String getStorageType() {
		return yamlConfiguration.getString("storage.type", "SQLite");
	}

	public String getStorageHost() {
		return yamlConfiguration.getString("storage.mysql.host", "Host not found");
	}
	public String getStoragePort() {
		return yamlConfiguration.getString("storage.mysql.port", "Port not found");
	}
	public String getStorageDatabase() {
		return yamlConfiguration.getString("storage.mysql.database", "Database not found");
	}
	public String getStorageUsername() {
		return yamlConfiguration.getString("storage.mysql.username", "Username not found");
	}
	public String getStoragePassword() {
		return yamlConfiguration.getString("storage.mysql.password", "Password not found");
	}
	public String getStorageSettings() {
		return yamlConfiguration.getString("storage.mysql.settings", "Settings not found");
	}
	public long getStorageMaximumPoolSize() {
		return yamlConfiguration.getLong("storage.mysql.connectionPool.maximumPoolSize", 10L);
	}
	public long getStorageMinimumIdle() {
		return yamlConfiguration.getLong("storage.mysql.connectionPool.minimumIdle", 10L);
	}
	public long getStorageMaxLifetime() {
		return yamlConfiguration.getLong("storage.mysql.connectionPool.maxLifetime", 1800000L);
	}
	public long getStorageKeepaliveTime() {
		return yamlConfiguration.getLong("storage.mysql.connectionPool.keepaliveTime", 0L);
	}
	public long getStorageConnectionTimeout() {
		return yamlConfiguration.getLong("storage.mysql.connectionPool.connectionTimeout", 5000L);
	}
	public String getStorageTablePrefix() {
		return yamlConfiguration.getString("storage.mysql.tablePrefix", "exg_");
	}


	// -------------------------------------------------- //


	public List<String> getKeysToRemove() {
		return List.of();
	}


	// -------------------------------------------------- //
}
