package de.tekup.rest.data.services;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.tekup.rest.data.dto.GameType;
import de.tekup.rest.data.dto.PersonReponse;
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
	private ModelMapper mapper = new ModelMapper();

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
	public PersonReponse getEntityById(long id) {
		Optional<PersonEntity> opt = reposPerson.findById(id);
		PersonEntity entity;
		if (opt.isPresent())
			entity = opt.get();
		else
			throw new NoSuchElementException("Person with this Id is not found");
		return mapper.map(entity, PersonReponse.class);
	}
	
	
	private PersonEntity getPersonEntityById(long id) {
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
		PersonEntity oldPerson = this.getPersonEntityById(id);
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
		PersonEntity entity = this.getPersonEntityById(id);
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
		/*List<PersonEntity> returnPersons = reposPhone.findAll()
													 .stream()
													 .filter(phone -> phone.getOperator().equalsIgnoreCase(operator))
													 .map(phone -> phone.getPerson())
													 .distinct()
													 .collect(Collectors.toList());
		return new ArrayList<>(returnPersons) ;*/
		return reposPhone.getPersonsWithOperator(operator);
	}
	
	// Average age of all Persons
	public double getAverageAge() {
		LocalDate now = LocalDate.now();
		List<PersonEntity> persons = reposPerson.findAll();
		//version 1
		/*double sum = 0;
		
		for (PersonEntity person : persons) {
			//sum += now.getYear() - person.getDateOfBirth().getYear();
			sum += ChronoUnit.YEARS.between(person.getDateOfBirth(), now);
		}
		
		
		return sum/(double)persons.size();
		*/
		
		// Version 2 with stream / primitiveStream
		OptionalDouble opt =persons.stream()
			.mapToLong(person -> ChronoUnit.YEARS.between(person.getDateOfBirth(), now))
			.average();
		
		return opt.orElse(0);
	}
	
	//Persons who play the type of game the most played.
	public List<PersonEntity> getMostTypePlayed(){
		Map<String, Set<PersonEntity>> map = new HashMap<>();
		List<GameEntity> games = reposGame.findAll();
		
		// create a map key -> type ; value -> list of persons play this type
		for (GameEntity game : games) {
			// check if type is exist in the map as a key
			if(map.containsKey(game.getType())) {
				// if exist add the persons of the current game to the value of the type
				map.get(game.getType()).addAll(game.getPersons());
			}else {
				// if not exist 
				// create a key with type and assign the list of person as value
				map.put(game.getType(), new HashSet<>(game.getPersons()));
			}
		}
		
		List<PersonEntity> persons = new ArrayList<>();
		// find the longest set to returned to the user (like search a max)
		for (Set<PersonEntity> set : map.values()) {
			if(set.size() > persons.size()) {
				persons = new ArrayList<>(set);
			}
		}
		
		
		return persons;
	}
	
	// Display the games type and the number of games for each type
	public List<GameType> getTypeWithNumber(){
		List<GameType> gamesType = new ArrayList<>();
		List<GameEntity> games = reposGame.findAll();
		
		// A x
		// B y
		// A z
		// A z1
		for (GameEntity game : games) {
			GameType  gameType = new GameType(game.getType(), 1);
			if(gamesType.contains(gameType)) {
				GameType gameInList = gamesType.get(gamesType.indexOf(gameType));
				gameInList.increment();
			} else {
				gamesType.add(gameType);
			}
		}
		
		//List //  1 gt(A,1)
		// list gt(A,1) // 2 gt(B,1)
		// list gt(A,2); gt(B,1) // 3 gt(A,1)
		// list gt(A,3); gt(B,1) // 4 gt(A,1)
		
		return gamesType;
	}
	// return a person by name 
	public PersonEntity getByName(String name) {
		/*List<PersonEntity> persons = reposPerson.findAll();
		
		for (PersonEntity person : persons) {
			if(person.getName().equalsIgnoreCase(name))
				return person;
		}
		throw new NoSuchElementException("Person with this Name is not found");
		*/
		// version with a stream
		/*
		  return reposPerson.findAll().stream()
		
							.filter(p -> p.getName().equalsIgnoreCase(name))
							.findFirst()
							.orElseThrow(()-> new NoSuchElementException("Person with this Name is not found"));
		 */
	
		// version with query
		return reposPerson.findByNameIgnoreCase(name)
				.orElseThrow(()-> new NoSuchElementException("Person with this Name is not found"));
	}

}
