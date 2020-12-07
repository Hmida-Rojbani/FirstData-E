package de.tekup.rest.data.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressRequest {
	@Positive
	private int number;
	@NotBlank
	private String street;
	@NotBlank
	private String city;
}
