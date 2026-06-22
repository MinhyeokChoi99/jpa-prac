package kr.co.prac.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration
public class QueryDslConfig {
	
	@PersistenceContext
	EntityManager em;
	
	@Bean
	public JPQLQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(em);
	}

}
