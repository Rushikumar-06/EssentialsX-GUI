package fr.snipertvmc.essentialsxgui.infrastructure.enums;

import org.bukkit.Bukkit;

public enum MCServerVersion {


	// -------------------------------------------------- //


	UnknownVersion("Unknown version"),
	UnsupportedVersion("Unsupported version"),

	v1_8_R3("1.8.8"),

	v1_9_R2("1.9.4"),

	v1_10_R1("1.10.2"),

	v1_11_R1("1.11.2"),

	v1_12_R1("1.12.2"),

	v1_13_R2("1.13.2"),

	v1_14_R1("1.14.4"),

	v1_15_R1("1.15.2"),

	v1_16_R3("1.16.4"),

	v1_17_R1("1.17.1"),

	v1_18_R2("1.18.2"),

	v1_19_R3("1.19.4"),

	v1_20_R3("1.20.3"),

	v1_21_R3("1.21.4");


	// -------------------------------------------------- //


	private final String versionName;


	// -------------------------------------------------- //


	MCServerVersion(String versionName) {
		this.versionName = versionName;
	}


	// -------------------------------------------------- //


	public String getVersionName() {
		return versionName;
	}


	// -------------------------------------------------- //


	public static MCServerVersion getMCServerVersion() {
		return getMCServerVersion(getServerVersion());
	}


	public static MCServerVersion getMCServerVersion(String serverVersion) {
		for (MCServerVersion mcServerVersion : MCServerVersion.values()) {
			if (serverVersion.equals(mcServerVersion.name())) {
				return mcServerVersion;
			}
		}

		if (serverVersion.matches("v1_\\d{2}_R\\d")) {
			return UnsupportedVersion;
		}

		return UnknownVersion;
	}



	private static String getServerVersion() {
		return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
	}


	// -------------------------------------------------- //


	public boolean isHigherThan(MCServerVersion version) {
		return this.compareTo(version) > 0;
	}


	public boolean isLowerThan(MCServerVersion version) {
		return this.compareTo(version) < 0;
	}


	public boolean isEqualTo(MCServerVersion version) {
		return this.compareTo(version) == 0;
	}


	// -------------------------------------------------- //


	public boolean isHigherThan(String version) {
		return this.compareTo(MCServerVersion.valueOf(version)) > 0;
	}


	public boolean isLowerThan(String version) {
		return this.compareTo(MCServerVersion.valueOf(version)) < 0;
	}


	public boolean isEqualTo(String version) {
		return this.compareTo(MCServerVersion.valueOf(version)) == 0;
	}


	// -------------------------------------------------- //
}
