package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import fr.snipertvmc.essentialsxgui.Main;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.Map;

public class EXGInventoryTitle {


	// -------------------------------------------------- //


	private String title;


	// -------------------------------------------------- //


	public EXGInventoryTitle(String title) {
		this.title = title;
	}


	// -------------------------------------------------- //


	public String getTitle(Player player) {
		if (Main.getInstance().getHookManager().getPlaceholderAPIHook().isSupported()) {
			return PlaceholderAPI.setPlaceholders(player, title);
		} else {
			return title;
		}
	}


	// -------------------------------------------------- //


	public void setTitle(String title) {
		this.title = title;
	}


	// -------------------------------------------------- //


	public EXGInventoryTitle updateVariables(Map<String, String> variables) {

		variables.forEach((key, value) -> {
			title = title.replace("{" + key + "}", value);
		});

		return this;
	}


	// -------------------------------------------------- //


	public EXGInventoryTitle duplicate() {
		return new EXGInventoryTitle(title);
	}


	// -------------------------------------------------- //
}
