package fr.snipertvmc.essentialsxgui.managers;

import fr.snipertvmc.essentialsxgui.Main;
import net.byteflux.libby.BukkitLibraryManager;
import net.byteflux.libby.Library;

public class LibraryManager {


	// -------------------------------------------------- //


	public LibraryManager() {
		BukkitLibraryManager bukkitLibraryManager = Main.getInstance().getBukkitLibraryManager();
		bukkitLibraryManager.addMavenCentral();
		loadEssentialLibraries();
	}


	// -------------------------------------------------- //


	public void loadEssentialLibraries() {

		BukkitLibraryManager bukkitLibraryManager = Main.getInstance().getBukkitLibraryManager();

		bukkitLibraryManager.loadLibrary(Library.builder()
				.groupId("com.fasterxml.jackson.core")
				.artifactId("jackson-databind")
				.version("2.20.0")
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
