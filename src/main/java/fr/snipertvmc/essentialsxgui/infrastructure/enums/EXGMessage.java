package fr.snipertvmc.essentialsxgui.infrastructure.enums;

public enum EXGMessage {


	// -------------------------------------------------- //


	// ADMIN
	FILES_RELOADING("admin.filesReloading"),
	FILES_RELOADED("admin.filesReloaded"),

	ALERT_UPDATE_AVAILABLE("admin.alertUpdateAvailable"),


	// ERRORS
	INVALID_MATERIAL("errors.invalidMaterial"),
	LENGTH_LIMIT("errors.lengthLimit"),
	INVALID_NUMBER("errors.invalidNumber"),

	ARGUMENT_NOT_FOUND("errors.argumentNotFound"),
	NO_PERMISSION("errors.noPermission"),
	ONLY_FOR_PLAYERS("errors.onlyForPlayers"),

	HOME_DELETE_ERROR("general.homeDeleteError"),
	HOME_NAME_ALREADY_EXISTS("errors.homeNameAlreadyExists"),
	HOME_LIMIT_REACHED("errors.homeLimitReached"),
	NO_HOME_FOUND("errors.noHomeFound"),

	KIT_NAME_ALREADY_EXISTS("errors.kitNameAlreadyExists"),


	// GENERAL
	ENTER_NEW_DISPLAY_NAME("general.enterNewDisplayName"),
	ENTER_NEW_ICON_NAME("general.enterNewIconName"),
	ENTER_NEW_HOME_NAME("general.enterNewHomeName"),
	ENTER_NEW_KIT_NAME("general.enterNewKitName"),
	ENTER_NEW_KIT_DELAY("general.enterNewKitDelay"),

	CONFIRM_DELETE_HOME("general.confirmDeleteHome"),
	CONFIRM_DELETE_KIT("general.confirmDeleteKit"),

	SEARCH_HOME("general.searchHome"),

	ACTION_CANCELED("general.actionCanceled"),
	ACTION_EXPIRED("general.actionExpired"),
	ONGOING_ACTION("general.ongoingAction"),


	DISPLAY_NAME_CHANGED("general.displayNameChanged"),
	ICON_CHANGED("general.iconChanged"),
	HOME_DELETED("general.homeDeleted"),
	KIT_DELETED("general.kitDeleted"),
	HOME_CREATED("general.homeCreated"),
	KIT_CREATED("general.kitCreated"),


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
