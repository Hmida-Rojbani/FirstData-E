package de.tekup.rest.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// DTO : Data Transfer Object
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "type")
public class GameType {
	
	private String type;
	private int numberOfGames;
	
	public void increment() {
		numberOfGames++;
	}
	
	

}
