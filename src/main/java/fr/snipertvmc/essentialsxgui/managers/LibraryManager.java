package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

public class LibraryManager {


	// -------------------------------------------------- //


	public LibraryManager() {
		BukkitLibraryManager bukkitLibraryManager = Main.getInstance().getBukkitLibraryManager();
		bukkitLibraryManager.addMavenCentral();
		bukkitLibraryManager.addJitPack();
		loadEssentialLibraries();
	}


	// -------------------------------------------------- //


	public void loadEssentialLibraries() {

		BukkitLibraryManager bukkitLibraryManager = Main.getInstance().getBukkitLibraryManager();

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.squareup.moshi")
				.artifactId("moshi")
				.version("1.15.2")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.squareup.okio")
				.artifactId("okio")
				.version("3.16.1")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.squareup.okhttp3")
				.artifactId("okhttp")
				.version("5.2.1")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.squareup.okio")
				.artifactId("okio-jvm")
				.version("3.16.0")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("org.jetbrains.kotlin")
				.artifactId("kotlin-stdlib")
				.version("2.3.0-Beta1")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.zaxxer")
				.artifactId("HikariCP")
				.version("7.0.2")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("io.github.classgraph")
				.artifactId("classgraph")
				.version("4.8.181")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.github.InstantlyMoist")
				.artifactId("privatebin-java-api")
				.version("master-5625a57693-1")
				.build());

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("org.json")
				.artifactId("json")
				.version("20250517")
				.build());
	}


	public void loadLibraries(String libraryName) {

		BukkitLibraryManager bukkitLibraryManager = Main.getInstance().getBukkitLibraryManager();

		switch (libraryName) {

			case "SQLite" -> bukkitLibraryManager.loadLibrary(Library.builder()
					.groupId("org.xerial")
					.artifactId("sqlite-jdbc")
					.version("3.50.3.0")
					.build());

			case "MariaDB" -> bukkitLibraryManager.loadLibrary(Library.builder()
					.groupId("org.mariadb.jdbc")
					.artifactId("mariadb-java-client")
					.version("3.5.6")
					.build());

			case "MySQL" -> bukkitLibraryManager.loadLibrary(Library.builder()
					.groupId("com.mysql")
					.artifactId("mysql-connector-j")
					.version("9.4.0")
					.build());
		}
	}


	// -------------------------------------------------- //
}
