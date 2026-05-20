package fr.snipertvmc.essentialsxgui.infrastructure.enums;

public enum EXGEcoAction {


	// -------------------------------------------------- //


	ADD(EXGMessage.ADD),
	TAKE(EXGMessage.TAKE),
	SET(EXGMessage.SET),
	RESET(EXGMessage.RESET);


	// -------------------------------------------------- //


	private final EXGMessage actionName;


	// -------------------------------------------------- //


	EXGEcoAction(EXGMessage actionName) {
		this.actionName = actionName;
	}


	// -------------------------------------------------- //


	public EXGMessage getActionName() {
		return actionName;
	}

	public String getCommand(String targetName, double amount) {
		return switch (this) {
			case ADD -> "eco give " + targetName + " " + amount;
			case TAKE -> "eco take " + targetName + " " + amount;
			case SET -> "eco set " + targetName + " " + amount;
			case RESET -> "eco set " + targetName + " 0";
		};
	}


	// -------------------------------------------------- //
}
