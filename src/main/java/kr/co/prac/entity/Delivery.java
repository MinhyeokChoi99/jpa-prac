package kr.co.prac.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter @Setter
public class Delivery {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	@OneToOne
	@JoinColumn(name = "order_id")
	private Orders order;
	
	private String city;
	private String street;
	private String zipcode;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	
	
	
}
