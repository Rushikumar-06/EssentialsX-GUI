package fr.snipertvmc.essentialsxgui.infrastructure.enums;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.Sound;

public enum EXGSound {


	// -------------------------------------------------- //


	// GUI
	GUI_OPEN("guiOpen", "CHEST_OPEN"),
	GUI_CLOSE("guiClose", "CHEST_CLOSE"),
	GUI_BACK("guiBack", "SHOOT_ARROW"),
	GUI_CLICK("guiClick", "CHICKEN_EGG_POP"),

	// ACTION
	ACTION_SUCCESS("actionSuccess", "LEVEL_UP"),
	ACTION_CANCELED("actionCanceled", "ITEM_BREAK"),
	ACTION_FAILURE("actionFailure", "VILLAGER_NO");


	// -------------------------------------------------- //


	private final String soundPath;
	private final String defaultSoundName;


	// -------------------------------------------------- //


	EXGSound(String soundPath, String defaultSoundName) {
		this.soundPath = soundPath;
		this.defaultSoundName = defaultSoundName;
	}


	// -------------------------------------------------- //


	public String getSoundPath() {
		return soundPath;
	}


	public String getSoundName() {
		return Main.getInstance().getConfiguration().getSound(soundPath);
	}


	public String getDefaultSoundName() {
		return defaultSoundName;
	}


	public Sound getSound() {

		try {
			return Sound.valueOf(getSoundName());

		} catch (IllegalArgumentException e) {
			return Sound.valueOf(defaultSoundName);
		}
	}


	// -------------------------------------------------- //
}
