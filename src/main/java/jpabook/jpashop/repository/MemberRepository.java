package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

//Repository 어노테이션 -> spring 빈으로 등록을 해주고
// component 어노테이션 붙어 있어서
//스프링 부트 기본 동작방식 SpringBootApplication 이 있으면
// 이패키지, 이패키지 하위의 모든것을 스프링이 컴포넌트 스캔-> 스프링빈으로 자동 등록
//-> 컴포넌트 스캔의 대상이되서 빈으로 등록
@Repository
@RequiredArgsConstructor // 생성자로 인젝션한것
// 이게 될려면
public class MemberRepository {
    // jpa 를 사용하는거기 때문에 jpa가 제공하는 표준 어노테이션
    // 스프링이 EntityManager 를 만들어서 주입
    //jpa 의 EntitiyManger를   스프링이 주입을 해준다

    // 기존방식 엔티티 메니저 팩토리-> 엔티티 매니저를 꺼내서 쓰고
    //그럴필요없이 해결 가능하다
    // 엔티티매니저 팩토리를 주입받고 싶다
//    @PersistenceUnit
//    private EntityManagerFactory emf;
//    @PersistenceContext
    //springboot-data-jpa가 지원 해준다 <-- 이게 중요할듯?
    //원래는 Autowired 로는 해결이 안되고
    // persistencecontext 로 되는데 스프링 data-jpa 가 있어야 된다

    private final EntityManager em;


    public void save (Member member){
        //persist-> 영속성컨텍스트에 Member 엔티티를 관리하게 된다
        // 트랜잭션이 커밋되는 경우-> db에 반영 , insert 쿼리 ㄱ
        em.persist(member);
    }

    // 단건 조회
    public Member findOne(Long id){
        //타입, pk
//        public <T> T find(Class<T> entityClass, Object primaryKey);

//        반환: the found entity instance or null if the entity does not exist
        return em.find(Member.class, id);
    }

    public List<Member> findAll(){
        //엔티티 객체에 대한 엘리엇을 m 으로 주고 엔티티 멤버를 조회하여라 - 기본편 참고
        //jpql - sql 이랑 비슷하지만 from 의대상이 쿼리가아니라 엔티티가 된다
        //jpql 강의 참고?
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    //파라미터를 바인딩하여서 특정회원만 찾도록
    public List<Member> findByName(String name){
        //name 이라는 파라미터를 바인딩
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name",name)
                .getResultList();
    }
}

