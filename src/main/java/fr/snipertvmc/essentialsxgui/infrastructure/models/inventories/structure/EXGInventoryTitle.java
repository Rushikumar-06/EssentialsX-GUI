package fr.snipertvmc.essentialsxgui.infrastructure.models.inventories.structure;

import java.util.Map;

public class EXGInventoryTitle {


	// -------------------------------------------------- //


	private String title;


	// -------------------------------------------------- //


	public EXGInventoryTitle(String title) {
		this.title = title;
	}


	// -------------------------------------------------- //


	public String getTitle() {
		return title.replace("&", "§");
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
