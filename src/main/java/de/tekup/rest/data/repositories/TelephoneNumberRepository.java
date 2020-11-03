package de.tekup.rest.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import de.tekup.rest.data.models.TelephoneNumberEntity;

public interface TelephoneNumberRepository extends JpaRepository<TelephoneNumberEntity, Integer>{

}
