package dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@Builder (toBuilder = true)
public class BreweryDTO {
		private int id;
		private String name;
		private String breweryType;
		private String street;
		private String city;
		private String state;
		private String postalCode;
		private String country;
		private String longitude;
		private String latitude;
		private String phone;
		private String websiteUrl;
		private LocalDateTime updated_at;
		private LocalDateTime created_at;
}
