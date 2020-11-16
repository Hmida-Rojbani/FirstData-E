package de.tekup.rest.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import de.tekup.rest.data.models.PersonEntity;
import de.tekup.rest.data.models.TelephoneNumberEntity;

public interface TelephoneNumberRepository extends JpaRepository<TelephoneNumberEntity, Integer>{

	// JPQL
	@Query("select distinct(t.person) from TelephoneNumberEntity t "
			+ "where lower(t.operator) = lower(:opt)")
	List<PersonEntity> getPersonsWithOperator(@Param("opt") String operator);
	
	/*@Query("select t.person from TelephoneNumberEntity t "
			+ "where t.operator = ?1")
	List<PersonEntity> getPersonsWithOperator(String operator);*/
}
