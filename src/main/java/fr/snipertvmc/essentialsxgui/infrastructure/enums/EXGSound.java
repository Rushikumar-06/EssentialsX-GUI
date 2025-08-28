package fr.snipertvmc.essentialsxgui.infrastructure.enums;

import fr.snipertvmc.essentialsxgui.Main;
import org.bukkit.Sound;

public enum EXGSound {


	// -------------------------------------------------- //


	// GUI
	GUI_OPEN("guiOpen"),
	GUI_CLOSE("guiClose"),
	GUI_BACK("guiBack"),
	GUI_CLICK("guiClick"),
	GUI_PAGE_CHANGE("guiPageChange"),

	// ACTION
	ACTION_SUCCESS("actionSuccess"),
	ACTION_CANCELED("actionCanceled"),
	ACTION_FAILURE("actionFailure");


	// -------------------------------------------------- //


	private final String soundPath;


	// -------------------------------------------------- //


	EXGSound(String soundPath) {
		this.soundPath = soundPath;
	}


	// -------------------------------------------------- //


	public String getSoundPath() {
		return soundPath;
	}
	public String getSoundName() {
		return Main.getInstance().getConfiguration().getSound(soundPath);
	}


	public Sound getSound() {

		try {
			return Sound.valueOf(getSoundName());

		} catch (Exception e) {
			return null;
		}
	}


	// -------------------------------------------------- //
}
