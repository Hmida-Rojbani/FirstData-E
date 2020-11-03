package de.tekup.rest.data.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tekup.rest.data.models.AddressEntity;
import de.tekup.rest.data.models.GameEntity;
import de.tekup.rest.data.models.PersonEntity;
import de.tekup.rest.data.models.TelephoneNumberEntity;
import de.tekup.rest.data.repositories.AddressRepository;
import de.tekup.rest.data.repositories.GameRepository;
import de.tekup.rest.data.repositories.PersonRepository;
import de.tekup.rest.data.repositories.TelephoneNumberRepository;

@Service
public class PersonServiceImpl implements PersonService {

	private PersonRepository reposPerson;
	private AddressRepository reposAddress;
	private TelephoneNumberRepository reposPhone;
	private GameRepository reposGame;

	@Autowired
	public PersonServiceImpl(PersonRepository reposPerson, AddressRepository reposAddress,
			TelephoneNumberRepository reposPhone, GameRepository reposGame) {
		super();
		this.reposPerson = reposPerson;
		this.reposAddress = reposAddress;
		this.reposPhone = reposPhone;
		this.reposGame = reposGame;
	}

	@Override
	public List<PersonEntity> getAllEntities() {
		return reposPerson.findAll();
	}

	@Override
	public PersonEntity getEntityById(long id) {
		Optional<PersonEntity> opt = reposPerson.findById(id);
		PersonEntity entity;
		if (opt.isPresent())
			entity = opt.get();
		else
			throw new NoSuchElementException("Person with this Id is not found");
		return entity;
	}

	// consider the games in the saving
	@Override
	public PersonEntity createPerson(PersonEntity personRequest) {
		// save address
		AddressEntity address = personRequest.getAddress();
		reposAddress.save(address);
		address.setPerson(personRequest);
		// save Person
		PersonEntity personInBase = reposPerson.save(personRequest);
		System.err.println(address);
		// save phones
		List<TelephoneNumberEntity> phones = personRequest.getPhones();
		// version 1
		/*
		 * for (TelephoneNumberEntity phone : phones) { phone.setPerson(personInBase);
		 * reposPhone.save(phone); }
		 */
		// version 2 Java 8
		phones.forEach(phone -> phone.setPerson(personInBase));
		reposPhone.saveAll(phones);

		boolean found;
		List<GameEntity> games = personRequest.getGames();
		List<GameEntity> gamesInBase = reposGame.findAll();
		for (GameEntity game : games) {
			found = false;
			for (GameEntity gameInBase : gamesInBase) {
				if (game.equals(gameInBase)) {
					gameInBase.getPersons().add(personInBase);
					reposGame.save(gameInBase);
					found = true;
					break;
				}
			}
			if (found == false) {
				List<PersonEntity> persons = new ArrayList<>();
				persons.add(personInBase);
				game.setPersons(persons);
				reposGame.save(game);
			}
		}

		return personRequest;
	}

	@Override
	public PersonEntity modifyPerson(long id, PersonEntity newPerson) {
		// is there a better (3 point bonus DS)
		PersonEntity oldPerson = this.getEntityById(id);
		if (newPerson.getName() != null)
			oldPerson.setName(newPerson.getName());
		if (newPerson.getDateOfBirth() != null)
			oldPerson.setDateOfBirth(newPerson.getDateOfBirth());
		// Correct Address Part
		AddressEntity newAddress = newPerson.getAddress();
		AddressEntity oldAddress = oldPerson.getAddress();
		if (newAddress != null) {
			if (newAddress.getNumber() != 0)
				oldAddress.setNumber(newAddress.getNumber());
			if (newAddress.getStreet() != null)
				oldAddress.setStreet(newAddress.getStreet());
			if (newAddress.getCity() != null)
				oldAddress.setCity(newAddress.getCity());
		}

		// Consider Phone and Game
		List<TelephoneNumberEntity> oldPhones = oldPerson.getPhones();
		List<TelephoneNumberEntity> newPhones = newPerson.getPhones();
		if (newPhones != null) {
			for (TelephoneNumberEntity newPhone : newPhones) {
				for (TelephoneNumberEntity oldPhone : oldPhones) {
					if (oldPhone.getId() == newPhone.getId()) {
						if (newPhone.getNumber() != null)
							oldPhone.setNumber(newPhone.getNumber());
						if (newPhone.getOperator() != null)
							oldPhone.setOperator(newPhone.getOperator());
					}
				}
			}
		}
		
		// Game 
		List<GameEntity> oldGames = oldPerson.getGames();
		List<GameEntity> newGames = newPerson.getGames();
		
		if(newGames != null) {
			for (GameEntity newGame : newGames) {
				for (GameEntity oldGame : oldGames) {
					if(oldGame.getId() == newGame.getId()) {
						if(newGame.getTitle() != null)
							oldGame.setTitle(newGame.getTitle());
						if(newGame.getType() != null)
							oldGame.setType(newGame.getType());
					}
				}
			}
		}

		return reposPerson.save(oldPerson);
	}

	@Override
	public PersonEntity deletePersonById(long id) {
		PersonEntity entity = this.getEntityById(id);
		reposPerson.deleteById(id);
		return entity;
	}
	
	// All persons with a given operator
	public List<PersonEntity> getAllByOperator(String operator){
		// version 1
		/*List<PersonEntity> persons = reposPerson.findAll();
		List<PersonEntity> returnPersons= new ArrayList<>();
		for (PersonEntity person : persons) {
			for (TelephoneNumberEntity phone : person.getPhones()) {
				if(phone.getOperator().equalsIgnoreCase(operator)) {
					returnPersons.add(person);
					break;
				}
			}
		}*/
		
		// version 2
		/*Set<PersonEntity> returnPersons= new HashSet<>();
		List<TelephoneNumberEntity> phones = reposPhone.findAll();
		for (TelephoneNumberEntity phone : phones) {
			if(phone.getOperator().equalsIgnoreCase(operator)) {
				returnPersons.add(phone.getPerson());
			}
		}*/
		// version 3 java 8
		List<PersonEntity> returnPersons = reposPhone.findAll()
													 .stream()
													 .filter(phone -> phone.getOperator().equalsIgnoreCase(operator))
													 .map(phone -> phone.getPerson())
													 .distinct()
													 .collect(Collectors.toList());
		return new ArrayList<>(returnPersons) ;
	}

}
