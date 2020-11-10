package de.tekup.rest.data.services;

import java.util.List;

import de.tekup.rest.data.models.PersonEntity;

public interface PersonService {
	
	List<PersonEntity> getAllEntities();
	PersonEntity getEntityById(long id);
	PersonEntity createPerson(PersonEntity entity);
	PersonEntity modifyPerson(long id, PersonEntity newEntity);
	PersonEntity deletePersonById(long id);
	public List<PersonEntity> getAllByOperator(String operator);
	public double getAverageAge() ;
}
