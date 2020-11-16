package de.tekup.rest.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.tekup.rest.data.models.PersonEntity;

public interface PersonRepository extends JpaRepository<PersonEntity, Long>{
	// exact match 
	Optional<PersonEntity> findByName(String name);
	// match withour case
	Optional<PersonEntity> findByNameIgnoreCase(String name);
	

}
