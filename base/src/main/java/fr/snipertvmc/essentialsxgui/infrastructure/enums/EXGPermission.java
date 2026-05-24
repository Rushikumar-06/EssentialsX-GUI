package fr.snipertvmc.essentialsxgui.infrastructure.enums;

public enum EXGPermission {


	// -------------------------------------------------- //


	// ADMIN
	ADMIN_UPDATE("essentialsxgui.admin.update"),


	// COMMAND - /essentialsxgui
	CMD_EXG("essentialsxgui.command.essentialsxgui"),
	CMD_EXG_RELOAD("essentialsxgui.command.essentialsxgui.reload"),
	CMD_EXG_DEBUG("essentialsxgui.command.essentialsxgui.debug"),


	// GENERAL
	KITS_ADMIN("essentialsxgui.kits.admin"),
	WARPS_ADMIN("essentialsxgui.warps.admin");


	// -------------------------------------------------- //


	private final String permission;


	// -------------------------------------------------- //


	EXGPermission(String permission) {
		this.permission = permission;
	}


	// -------------------------------------------------- //


	public String get() {
		return permission;
	}


	// -------------------------------------------------- //
}
