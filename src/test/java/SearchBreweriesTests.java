import java.util.List;
import java.util.NoSuchElementException;

import dto.BreweryDTO;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class SearchBreweriesTests implements WithAssertions {

	public static final String ENDPOINT = "https://api.openbrewerydb.org";

	@Test
	void testSearchBreweriesWorks() throws FindBreweryException {

		BreweryClient breweryClient = BreweryClient.createClient(ENDPOINT);
		List<BreweryDTO> breweries = breweryClient.searchBreweries("dog");

		assertThat(breweries).isNotNull();
		assertThat(breweries.size()).isGreaterThan(0);
	}

	@Test
	void testSearchBreweriesWorksForNameField() throws FindBreweryException {

		BreweryClient breweryClient = BreweryClient.createClient(ENDPOINT);
		List<BreweryDTO> breweries = breweryClient.searchBreweries("dog");
		breweries.stream()
				 .filter(record->record.getName().toLowerCase().contains("dog"))
				 .findFirst()
				 .orElseThrow(NoSuchElementException::new);
	}

	@SuppressWarnings ("ResultOfMethodCallIgnored")
	@Test
	void testSearchReturnsValidRecords() throws FindBreweryException {

		BreweryClient breweryClient = BreweryClient.createClient(ENDPOINT);
		List<BreweryDTO> breweries = breweryClient.searchBreweries("dog");
		List <String> breweriesName = null;
		for (int i =0; i < breweries.size();i++){
			breweriesName.add(breweries.get(i).getName());
		}
		for (String s : breweriesName) {
			s.toLowerCase();
			assertThat(s.contains("dog"));
		}
	}

	@Test
	void testSearchReturnsNullOnBadRequest() throws FindBreweryException {
		BreweryClient breweryClient = BreweryClient.createClient(ENDPOINT);
		List<BreweryDTO> breweries = breweryClient.searchBreweries("###");

		assertThat(breweries.size()).isEqualTo(0);
	}

	@Test
	void testSearchProcessMultipleWordsQuery() throws FindBreweryException {
		BreweryClient breweryClient = BreweryClient.createClient(ENDPOINT);
		List<BreweryDTO> breweries = breweryClient.searchBreweries("Barrel Dog Brewing");
		breweries.stream()
				 .filter(record->record.getName().toLowerCase().contains("barrel dog brewing"))
				 .findFirst()
				 .orElseThrow(NoSuchElementException::new);
	}
}
