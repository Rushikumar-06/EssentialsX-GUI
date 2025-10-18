package fr.snipertvmc.essentialsxgui.utilities.other;

import com.squareup.moshi.Json;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import fr.snipertvmc.essentialsxgui.utilities.ConsoleLogger;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class UpdateUtils {


	// -------------------------------------------------- //


	public static String getLatestVersionTag() {

		String API_URL = "https://api.github.com/repos/SniperTVmc/EssentialsX-GUI/releases/latest";

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(API_URL))
				.header("Accept", "application/vnd.github+json")
				.header("X-GitHub-Api-Version", "2022-11-28")
				.build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			if (response.statusCode() == 200) {
				Moshi moshi = new Moshi.Builder().build();
				JsonAdapter<Release> adapter = moshi.adapter(Release.class);

				Release release = adapter.fromJson(response.body());
				if (release != null) {
					return release.tagName;
				}
			}

		} catch (IOException | InterruptedException e) {
			ConsoleLogger.error("Failed to fetch latest version: " + e.getMessage());
		}

		return null;
	}


	// -------------------------------------------------- //


	private static class Release {
		@Json(name = "tag_name")
		public String tagName;
	}


	// -------------------------------------------------- //
}
