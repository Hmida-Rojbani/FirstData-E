package de.tekup.rest.data.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {
	private String name;

	private LocalDate dateOfBirth;
	
	private AddressRequest addressReq;
}
