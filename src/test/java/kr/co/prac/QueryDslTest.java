package kr.co.prac;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.co.prac.member.entity.Member;
import kr.co.prac.member.entity.QMember;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class QueryDslTest {
	@PersistenceContext
	EntityManager em;
	
	@Test
	void dslTest1() {
		JPAQuery<Member> query = new JPAQuery<>(em);
		QMember qMember = QMember.member;
		
		List<Member> members = query.from(qMember).orderBy(qMember.name.desc()).fetch();
		for (Member member : members) {
			log.info(member.getName());
			log.info(member.getEmail());
		}
	}
	
	@Test
	void dslTest2() {
		JPAQueryFactory jf = new JPAQueryFactory(em);
		QMember qMember = QMember.member;
		
		List<Member> members = jf.selectFrom(qMember).orderBy(qMember.name.desc()).fetch();
	
		for (Member member : members) {
			log.info(member.getName());
			log.info(member.getEmail());
		}
		
	}
}

