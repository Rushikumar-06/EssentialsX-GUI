package fr.snipertvmc.essentialsxgui.hooks;

public class PlaceholderAPIHook {


	// -------------------------------------------------- //


	public boolean isSupported() {
		try {
			Class.forName("me.clip.placeholderapi.PlaceholderAPI");
			return true;

		} catch (ClassNotFoundException e) {
			return false;
		}
	}


	// -------------------------------------------------- //
}
