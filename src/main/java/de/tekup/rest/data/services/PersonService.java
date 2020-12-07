package de.tekup.rest.data.services;

import java.util.List;

import de.tekup.rest.data.dto.GameType;
import de.tekup.rest.data.dto.PersonReponse;
import de.tekup.rest.data.models.PersonEntity;

public interface PersonService {

	List<PersonEntity> getAllEntities();

	PersonReponse getEntityById(long id);

	PersonEntity createPerson(PersonEntity entity);

	PersonEntity modifyPerson(long id, PersonEntity newEntity);

	PersonEntity deletePersonById(long id);

	public List<PersonEntity> getAllByOperator(String operator);

	public double getAverageAge();

	public List<PersonEntity> getMostTypePlayed();
	
	public List<GameType> getTypeWithNumber();
	
	public PersonEntity getByName(String name);
}
