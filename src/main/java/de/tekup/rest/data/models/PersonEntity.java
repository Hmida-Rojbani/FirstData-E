package de.tekup.rest.data.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

import org.modelmapper.ModelMapper;

import de.tekup.rest.data.dto.AddressRequest;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "Person")
@ToString(exclude = {"address","phones","games"})
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
	
	public int getAge() {
		return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
	}

	public String getFullAddress() {
		return address.getNumber()+" "+address.getStreet()+", "+address.getCity()+".";
	}
	
	public void setAddressReq(AddressRequest addressReq) {
		ModelMapper mapper = new ModelMapper();
		this.address = mapper.map(addressReq, AddressEntity.class);
	}
}
