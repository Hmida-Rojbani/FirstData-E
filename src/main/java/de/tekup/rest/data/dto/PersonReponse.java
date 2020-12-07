package de.tekup.rest.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonReponse {
	
	private String name;
	private int age;
	private String fullAddress;

}
