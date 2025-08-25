package fr.snipertvmc.essentialsxgui.utilities.other;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(response.body());
				return jsonNode.get("tag_name").asText();
			}

		} catch (IOException | InterruptedException e) {
			ConsoleLogger.error("Failed to fetch the latest version from GitHub API: " + e.getMessage());
		}

		return null;
	}


	// -------------------------------------------------- //
}
