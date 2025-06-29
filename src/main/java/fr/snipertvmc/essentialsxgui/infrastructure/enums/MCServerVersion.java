package fr.snipertvmc.essentialsxgui.infrastructure.enums;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MCServerVersion {


	// -------------------------------------------------- //


	UnknownVersion("Unknown version", "Unknown", -1, false),
	UnsupportedVersion("Unsupported version", "Unsupported", -1, false),

	v1_8_8("1.8.8", "v1_8_R3", 47, true),

	v1_9_4("1.9.4", "v1_9_R2", 110, false),

	v1_10_2("1.10.2", "v1_10_R1", 210, false),

	v1_11_2("1.11.2", "v1_11_R1", 316, false),

	v1_12_2("1.12.2", "v1_12_R1", 340, true),

	v1_13_2("1.13.2", "v1_13_R2", 404, false),

	v1_14_4("1.14.4", "v1_14_R1", 498, false),

	v1_15_2("1.15.2", "v1_15_R1", 578, false),

	v1_16_5("1.16.5", "v1_16_R3", 754, true),

	v1_17_1("1.17.1", "v1_17_R1", 756, false),

	v1_18_2("1.18.2", "v1_18_R2", 758, false),

	v1_19_4("1.19.4", "v1_19_R3", 762, false),

	v1_20_6("1.20.6", "v1_20_R3", 766, false),

	v1_21_1("1.21.1", "v1_21_R3", 769, false),
	v1_21_4("1.21.4", "v1_21_R3", 769, true),
	v1_21_5("1.21.5", "v1_21_R4", 770, false);


	// -------------------------------------------------- //


	private final String versionName;
	private final String versionNMS;
	private final int versionProtocol;

	private final boolean fullySupported;


	// -------------------------------------------------- //


	MCServerVersion(String versionName, String versionNMS, int versionProtocol, boolean fullySupported) {
		this.versionName = versionName;
		this.versionNMS = versionNMS;
		this.versionProtocol = versionProtocol;

		this.fullySupported = fullySupported;
	}


	// -------------------------------------------------- //


	public String getVersionName() {
		return versionName;
	}
	public String getVersionNMS() {
		return versionNMS;
	}
	public int getVersionProtocol() {
		return versionProtocol;
	}

	public boolean isFullySupported() {
		return fullySupported;
	}


	// -------------------------------------------------- //


	public static MCServerVersion getMCServerVersion() {
		return getMCServerVersion(getServerVersion());
	}


	private static MCServerVersion getMCServerVersion(String serverVersion) {
		for (MCServerVersion mcServerVersion : MCServerVersion.values()) {
			if (serverVersion.equals(mcServerVersion.getVersionName())) {
				return mcServerVersion;
			}
		}

		if (serverVersion.matches("1.\\d+\\.\\d+")) {
			return UnsupportedVersion;
		}

		return UnknownVersion;
	}



	private static String getServerVersion() {

		String versionText = Bukkit.getVersion();
		Pattern pattern = Pattern.compile("\\(MC: ([0-9]+\\.[0-9]+\\.[0-9]+)\\)");
		Matcher matcher = pattern.matcher(versionText);

		if (matcher.find()) {
			return matcher.group(1);
		}

		return "Unknown";
	}


	private String getServerNMSVersion() {
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
