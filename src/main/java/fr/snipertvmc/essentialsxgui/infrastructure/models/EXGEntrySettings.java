package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import fr.snipertvmc.essentialsxgui.libraries.exglib.Pair;
import org.bukkit.Material;

public class EXGEntrySettings {


	// -------------------------------------------------- //


	private final EXGEntryType entryType;
	private String entryDisplayName;

	// For String
	int minLength = -1;
	int maxLength = -1;
	char[] acceptedCharacters;
	boolean mustBeNumber = false;
	String equalsToSomething;

	// For Material
	private Material[] acceptedMaterials;
	private String materialListPath;


	// -------------------------------------------------- //


	public EXGEntrySettings(EXGEntryType entryType) {
		this.entryType = entryType;
	}


	// -------------------------------------------------- //


	// General
	public EXGEntryType getType() {
		return entryType;
	}
	public String getEntryDisplayName() {
		return entryDisplayName;
	}

	// For String
	public int getMinLength() {
		return minLength;
	}
	public int getMaxLength() {
		return maxLength;
	}
	public char[] getAcceptedCharacters() {
		return acceptedCharacters;
	}
	public boolean isMustBeNumber() {
		return mustBeNumber;
	}
	public String getEqualsToSomething() {
		return equalsToSomething;
	}

	// For Material
	public Material[] getAcceptedMaterials() {
		return acceptedMaterials;
	}
	public String getMaterialListPath() {
		return materialListPath;
	}


	// -------------------------------------------------- //


	// General
	public EXGEntrySettings setEntryDisplayName(String entryDisplayName) {
		this.entryDisplayName = entryDisplayName;
		return this;
	}


	// For String
	public EXGEntrySettings setMinLength(int minLength) {
		this.minLength = minLength;
		return this;
	}
	public EXGEntrySettings setMaxLength(int maxLength) {
		this.maxLength = maxLength;
		return this;
	}
	public EXGEntrySettings setAcceptedCharacters(char[] acceptedCharacters) {
		this.acceptedCharacters = acceptedCharacters;
		return this;
	}
	public EXGEntrySettings setMustBeNumber(boolean mustBeNumber) {
		this.mustBeNumber = mustBeNumber;
		return this;
	}
	public EXGEntrySettings setEqualsToSomething(String equalsToSomething) {
		this.equalsToSomething = equalsToSomething;
		return this;
	}


	// For Material
	public EXGEntrySettings setAcceptedMaterials(Material[] acceptedMaterials) {
		this.acceptedMaterials = acceptedMaterials;
		return this;
	}
	public EXGEntrySettings setMaterialListPath(String materialListPath) {
		this.materialListPath = materialListPath;
		return this;
	}


	// -------------------------------------------------- //
}
