package fr.snipertvmc.essentialsxgui.infrastructure.enums;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MCServerVersion {


	// -------------------------------------------------- //


	UnknownVersion("Unknown version", false, false),

	v1_8_8("1.8.8", true, false),

	v1_9_4("1.9.4", false, false),

	v1_10_2("1.10.2", false, false),

	v1_11_2("1.11.2", false, false),

	v1_12_2("1.12.2", true, false),

	v1_13_2("1.13.2", false, false),

	v1_14_4("1.14.4", false, false),

	v1_15_2("1.15.2", false, false),

	v1_16_5("1.16.5", true, false),

	v1_17_1("1.17.1", false, false),

	v1_18_2("1.18.2", false, false),

	v1_19_4("1.19.4", false, false),

	v1_20_6("1.20.6", false, false),

	v1_21("1.21", false, false),
	v1_21_1("1.21.1", false, false),
	v1_21_2("1.21.2", false, false),
	v1_21_3("1.21.3", false, false),
	v1_21_4("1.21.4", true, false),
	v1_21_5("1.21.5", false, false),
	v1_21_6("1.21.6", false, false),
	v1_21_7("1.21.7", false, false),
	v1_21_8("1.21.8", true, false),
	v1_21_9("1.21.9", true, false),
	v1_21_10("1.21.10", true, false);


	// -------------------------------------------------- //


	private final String versionName;

	private final boolean fullySupported;
	private final boolean deprecated;


	// -------------------------------------------------- //


	MCServerVersion(String versionName, boolean fullySupported, boolean deprecated) {
		this.versionName = versionName;

		this.fullySupported = fullySupported;
		this.deprecated = deprecated;
	}


	// -------------------------------------------------- //


	public String getVersionName() {
		return versionName;
	}

	public boolean isFullySupported() {
		return fullySupported;
	}
	public boolean isDeprecated() {
		return deprecated;
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


	public boolean isFormatValid(String version) {
		Pattern pattern = Pattern.compile("^(1\\.\\d+\\.\\d+|[0-9]+\\.[0-9]+)$");
		Matcher matcher = pattern.matcher(version);
		return matcher.matches();
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
