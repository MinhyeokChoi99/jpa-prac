package kr.co.prac.member.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.co.prac.global.entity.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter

public class Member extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long number;
	
	private String name;
	
	private String email;
}
