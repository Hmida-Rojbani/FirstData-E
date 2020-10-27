package de.tekup.rest.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.tekup.rest.data.models.PersonEntity;

public interface PersonRepository extends JpaRepository<PersonEntity, Long>{

}
