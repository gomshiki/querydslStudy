package org.example.querydslstudy;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.example.querydslstudy.entity.Member;
import org.example.querydslstudy.entity.QMember;
import org.example.querydslstudy.entity.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.example.querydslstudy.entity.QMember.*;

@SpringBootTest
@Transactional
public class QuerydslBasicTest {

    @Autowired
    EntityManager em;

    JPAQueryFactory queryFactory;

    @BeforeEach
    public void before() {
        queryFactory = new JPAQueryFactory(em);
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        em.persist(teamA);
        em.persist(teamB);

        Member member1 = new Member("member1", 10, teamA);
        Member member2 = new Member("member2", 20, teamA);

        Member member3 = new Member("member3", 30, teamB);
        Member member4 = new Member("member4", 40, teamB);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);
    }

    @Test
    void startJPQL() {
        // member1을 찾아라
        Member findMember = em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", "member1")
                .getSingleResult();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void startQuerydsl() {
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em); // JPAQueryFactory를 생성할 때 EntityManager를 넘겨줘야 함
//        QMember m = new QMember("m"); // 별칭 직접 지정
//        QMember m = QMember.member; // 기본 인스턴스 사용

        Member findMember = queryFactory
                .select(member) // QMember.member static으로 선언 후 사용
                .from(member)
                .where(member.username.eq("member1"))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void search(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(member.username.eq("member1").and(member.age.eq(10)))
                .fetchOne();

        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void searchAndParam(){
        Member findMember = queryFactory
                .selectFrom(member)
                .where(
                        member.username.eq("member1"), // 쉼표로 and 조건 추가
                        member.age.eq(10)
                )
                .fetchOne();
        assertThat(findMember.getUsername()).isEqualTo("member1");
    }

    @Test
    void resultFetch(){
   /*     List<Member> fetch = queryFactory.selectFrom(member).fetch();

        Member fetchOne = queryFactory.selectFrom(member).fetchOne(); // 단건 조회

        Member fetchFirst1 = queryFactory.selectFrom(member).fetchFirst(); // limit(1)로 단건 조회
        Member fetchFirst2 = queryFactory.selectFrom(member).limit(1).fetchOne();// limit(1)로 단건 조회*/

        List<Member> results = queryFactory.selectFrom(member).fetch(); // 리스트 조회
        int totalSize = results.size();
        System.out.println("totalSize = " + totalSize);

    }
}
