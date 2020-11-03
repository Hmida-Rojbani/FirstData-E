package de.tekup.rest.data.models;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "Person")
@ToString(exclude = "address")
public class PersonEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "personName", length = 50, nullable = false, unique = true)
	private String name;
	
	private LocalDate dateOfBirth;
	
	@OneToOne(cascade = CascadeType.REMOVE)
	private AddressEntity address;
	@OneToMany(mappedBy = "person",cascade = CascadeType.REMOVE)
	private List<TelephoneNumberEntity> phones;
	
	@ManyToMany(mappedBy = "persons",cascade = CascadeType.REMOVE)
	private List<GameEntity> games;

}
