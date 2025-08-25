package fr.snipertvmc.essentialsxgui.infrastructure.models;

import fr.snipertvmc.essentialsxgui.infrastructure.enums.EXGEntryType;
import org.bukkit.Material;

public class EXGEntrySettings {


	// -------------------------------------------------- //


	private final EXGEntryType entryType;

	// For String
	int minLength = -1;
	int maxLength = -1;
	char[] acceptedCharacters;
	boolean mustBeNumber = false;
	String equalsToSomething;

	// For Material
	private Material[] acceptedMaterials;


	// -------------------------------------------------- //


	public EXGEntrySettings(EXGEntryType entryType) {
		this.entryType = entryType;
	}


	// -------------------------------------------------- //


	// General
	public EXGEntryType getType() {
		return entryType;
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


	// -------------------------------------------------- //


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


	// -------------------------------------------------- //
}
