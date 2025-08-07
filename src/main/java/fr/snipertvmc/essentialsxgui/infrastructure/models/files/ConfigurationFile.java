package fr.snipertvmc.essentialsxgui.infrastructure.models.files;

import fr.snipertvmc.essentialsxgui.Main;
import fr.snipertvmc.essentialsxgui.infrastructure.enums.MCServerVersion;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	public boolean isHomesModuleEnabled() {
		return yamlConfiguration.getBoolean("general.modules.homes", true);
	}
	public boolean isKitsModuleEnabled() {
		return yamlConfiguration.getBoolean("general.modules.kits", true);
	}


	// -------------------------------------------------- //


	public boolean areSoundsEnabled() {
		return yamlConfiguration.getBoolean("sounds.enabled", true);
	}

	public String getSound(String path) {
		return yamlConfiguration.getString("sounds." + path, null);
	}


	// -------------------------------------------------- //


	public boolean hasEssentialsXGUICommand(Player player) {
		return player.hasPermission(yamlConfiguration.getString("permissions.commands.essentialsxgui", "unknownPermission"));
	}

	public boolean hasEssentialsXGUICommandDebugArgument(Player player) {
		return player.hasPermission(yamlConfiguration.getString("permissions.commands.essentialsxgui.debugArgument", "unknownPermission"));
	}


	public boolean hasKitsAdminAccess(Player player) {
		return player.hasPermission(yamlConfiguration.getString("permissions.guis.kitsAdminAccess", "unknownPermission"));
	}


	// -------------------------------------------------- //


	public Map<String, String> getPlaceholders() {

		MCServerVersion serverVersion = Main.getInstance().getMCServerVersion();
		switch (serverVersion) {

			case v1_8_8 -> {

				return Map.of(
						"value_guiOpen", "CHEST_OPEN",
						"value_guiClose", "CHEST_CLOSE",
						"value_guiBack", "SHOOT_ARROW",
						"value_guiClick", "CHICKEN_EGG_POP",

						"value_actionSuccess", "LEVEL_UP",
						"value_actionFailure", "VILLAGER_NO"
				);
			}

			case v1_9_4, v1_10_2, v1_11_2, v1_12_2, v1_13_2, v1_14_4, v1_15_2, v1_16_5,
			     v1_17_1, v1_18_2, v1_19_4, v1_20_6, v1_21, v1_21_1, v1_21_2, v1_21_3,
			     v1_21_4, v1_21_5, v1_21_6, v1_21_7, v1_21_8 -> {

				return Map.of(
						"value_guiOpen", "BLOCK_CHEST_OPEN",
						"value_guiClose", "BLOCK_CHEST_CLOSE",
						"value_guiBack", "ENTITY_EXPERIENCE_BOTTLE_THROW",
						"value_guiClick", "ENTITY_CHICKEN_EGG",

						"value_actionSuccess", "ENTITY_PLAYER_LEVELUP",
						"value_actionFailure", "ENTITY_VILLAGER_NO"
				);
			}
		}

		return new HashMap<>();
	}


	public List<String> getKeysToRemove() {
		return List.of();
	}


	// -------------------------------------------------- //
}
