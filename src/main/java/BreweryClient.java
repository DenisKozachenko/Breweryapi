import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import dto.BreweryDTO;

public class BreweryClient {

	private final HttpClient httpClient;
	private final String url;
	private final Gson gson;

//	/**
//	 * Returns a new {@link BreweryClient}.
//	 *
//	 * @param url apiUrl for the open brewery db service.
//	 * @return a BreweryClient instance.
//	 */
	public static BreweryClient createClient(final String url) {
		return new BreweryClient(url);
	}

//	/**
//	 * Private constructor.
//	 *
//	 * @param url endpoint url for the service.
//	 */
	private BreweryClient(final String url) {
		checkNotNull(url);
		this.url = url;
		this.gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
				.create();
		httpClient = HttpClient.newBuilder()
							   .version(HttpClient.Version.HTTP_1_1)
							   .build();
	}
//
//	/**
//	 * Returns the requested brewery.
//	 *
//	 * @param id the id of the brewery being requested.
//	 * @return a {@link Brewery} object.
//	 * @throws OpenBreweryDbClientException when a successful call is not made.
//	 */


	/**
	 * Returns a list of breweries based on the search term provided in the query.
	 *
	 * @param query Search term being queried.
	 * @return a list of Brewery objects.
	 * @throws FindBreweryException if the service call fails.
	 */
	public List<BreweryDTO> searchBreweries(final String query) throws FindBreweryException {
		checkNotNull(query);
		String searchBreweriesUrl = String.format("%s/breweries/search?query=%s", url, query);
		return executeListBreweriesRequest(searchBreweriesUrl);
	}

	private List<BreweryDTO> executeListBreweriesRequest(final String url) throws FindBreweryException {
		try {
			HttpRequest request = HttpRequest.newBuilder()
											 .GET()
											 .uri(URI.create(url))
											 .build();

			HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
			validateResponse(response);
			Type listType = new TypeToken<List<BreweryDTO>>(){}.getType();
			return gson.fromJson(response.body(), listType);
		} catch (IllegalArgumentException | IOException | InterruptedException e) {
			throw new FindBreweryException(e);
		}
	}


	private void validateResponse(HttpResponse<String> response) throws FindBreweryException {
		if (response.statusCode() != 200) {
			throw new FindBreweryException(response.body());
		}
	}
}
