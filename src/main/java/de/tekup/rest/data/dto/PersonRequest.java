package de.tekup.rest.data.dto;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonRequest {
	@NotBlank(message = "Name must contain characters")
	@Size(min = 5, max = 50)
	@Pattern(regexp = "[a-zA-Z ]+", message =  "Name must contain only characters")
	private String name;
	@Past
	private LocalDate dateOfBirth;
	@Valid
	private AddressRequest addressReq;
}
