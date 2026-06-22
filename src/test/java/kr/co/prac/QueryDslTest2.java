package kr.co.prac;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import kr.co.prac.member.entity.Member;
import kr.co.prac.member.entity.QMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest

public class QueryDslTest2 {
	
	@Autowired
	JPAQueryFactory jpaQueryFactory;
	
	@Test
	void dslTest1() {
		QMember qMember = QMember.member;
		
		List<Member> members = jpaQueryFactory.selectFrom(qMember).where(qMember.name.eq("Kim")).orderBy(qMember.name.desc()).fetch();
		
		for (Member member : members) {
			log.info(member.getName());
			log.info(member.getEmail());
		}
	}
	
}

