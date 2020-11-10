package de.tekup.rest.data.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO : Data Transfer Object
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameType {
	
	private String type;
	private int numberOfGames;

}
