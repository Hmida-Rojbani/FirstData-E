package de.tekup.rest.data.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tekup.rest.data.models.AddressEntity;
import de.tekup.rest.data.models.PersonEntity;
import de.tekup.rest.data.repositories.AddressRepository;
import de.tekup.rest.data.repositories.PersonRepository;

@Service
public class PersonServiceImpl implements PersonService {
	
	private PersonRepository reposPerson;
	private AddressRepository reposAddress;
	
	@Autowired
	public PersonServiceImpl(PersonRepository reposPerson, AddressRepository reposAddress) {
		super();
		this.reposPerson = reposPerson;
		this.reposAddress = reposAddress;
	}

	@Override
	public List<PersonEntity> getAllEntities() {
		return reposPerson.findAll();
	}

	@Override
	public PersonEntity getEntityById(long id) {
		Optional<PersonEntity> opt = reposPerson.findById(id);
		PersonEntity entity;
		if(opt.isPresent())
			entity= opt.get();
		else
			throw new NoSuchElementException("Person with this Id is not found");
		return entity;
	}

	@Override
	public PersonEntity createPerson(PersonEntity entity) {
		AddressEntity address = entity.getAddress();
		reposAddress.save(address);
		address.setPerson(entity);
		entity = reposPerson.save(entity);
		System.err.println(address);
		return entity;
	}

	@Override
	public PersonEntity modifyPerson(long id, PersonEntity newEntity) {
		PersonEntity entity = this.getEntityById(id);
		if(newEntity.getName() != null)
			entity.setName(newEntity.getName());
		if(newEntity.getDateOfBirth() != null)
			entity.setDateOfBirth(newEntity.getDateOfBirth());
		if(newEntity.getAddress() != null)
			entity.setAddress(newEntity.getAddress());
		
		return reposPerson.save(entity);
	}

	@Override
	public PersonEntity deletePersonById(long id) {
		PersonEntity entity = this.getEntityById(id);
		reposPerson.deleteById(id);
		return entity;
	}

}
