package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.hooks.EssentialsHook;

public class HookManager {


	// -------------------------------------------------- //


	private final EssentialsHook essentialsHook;


	// -------------------------------------------------- //


	public HookManager() {
		essentialsHook = new EssentialsHook();
	}


	// -------------------------------------------------- //


	public EssentialsHook getEssentialsHook() {
		return essentialsHook;
	}


	// -------------------------------------------------- //
}
