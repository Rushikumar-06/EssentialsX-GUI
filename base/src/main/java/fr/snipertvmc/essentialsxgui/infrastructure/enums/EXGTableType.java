package fr.snipertvmc.essentialsxgui.infrastructure.enums;

public enum EXGTableType {


	// -------------------------------------------------- //


	PLAYER_DATA("playerData"),
	SERVER_DATA("serverData");


	// -------------------------------------------------- //


	private final String tableName;


	// -------------------------------------------------- //


	EXGTableType(String tableName) {
		this.tableName = tableName;
	}


	// -------------------------------------------------- //


	public String getTableName() {
		return tableName;
	}


	// -------------------------------------------------- //
}
