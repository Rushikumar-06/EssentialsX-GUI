package fr.snipertvmc.essentialsxgui.infrastructure.enums;

public enum EXGMessage {


	// -------------------------------------------------- //


	// ADMIN
	FILES_RELOADING("admin.filesReloading"),
	FILES_RELOADED("admin.filesReloaded"),


	// ERRORS
	INVALID_MATERIAL("errors.invalidMaterial"),
	CHARACTER_LIMIT("errors.characterLimit"),

	ARGUMENT_NOT_FOUND("errors.argumentNotFound"),
	NO_PERMISSION("errors.noPermission"),

	HOME_DELETE_ERROR("general.homeDeleteError"),


	// GENERAL
	ENTER_NEW_DISPLAY_NAME("general.enterNewDisplayName"),
	ENTER_NEW_ICON_NAME("general.enterNewIconName"),
	CONFIRM_DELETE_HOME("general.confirmDeleteHome"),
	CONFIRM_DELETE_KIT("general.confirmDeleteKit"),
	ACTION_CANCELED("general.actionCanceled"),

	DISPLAY_NAME_CHANGED("general.displayNameChanged"),
	ICON_CHANGED("general.iconChanged"),
	HOME_DELETED("general.homeDeleted"),
	KIT_DELETED("general.kitDeleted"),

	OPENING_HOMES_INVENTORY("general.openingHomesInventory"),
	OPENING_ADMIN_KITS_INVENTORY("general.openingAdminKitsInventory"),
	OPENING_PLAYER_KITS_INVENTORY("general.openingPlayerKitsInventory");


	// -------------------------------------------------- //


	private final String path;


	// -------------------------------------------------- //


	EXGMessage(String path) {
		this.path = path;
	}


	// -------------------------------------------------- //


	public String getPath() {
		return path;
	}


	// -------------------------------------------------- //
}
