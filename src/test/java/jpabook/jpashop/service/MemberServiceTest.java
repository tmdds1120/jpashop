package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
//junit 실행할때 spring 이랑 같이 실행 할래
@SpringBootTest
//https://www.inflearn.com/questions/211302
//스프링컨테이너 안에서 테스트를 돌리는것, auto는 이게 없으면 실패
@Transactional// 이게 있어야 롤백, 테스트케이스에서는 -> 롤백
                // 실제는 롤백 x
public class MemberServiceTest {

    @Autowired MemberService memberService;
//    private MemberService memberService = new MemberService();
    @Autowired MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception{
        //given
        Member member = new Member();
        member.setName("json");
        //when
        Long saveId = memberService.join(member);
        //then
        //flush 영컨에 있는 내용을 db에 반영 이게 없으면 쿼리문이 안쏴진다
        em.flush();
        assertEquals(member,memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        //then
//        try {
            memberService.join(member2);// <-- 예외 튀어나오는 부분

//        } catch (IllegalStateException e){
//            return;
//        }

        //junit test 여기까지 도달하면 안된다!!
        Assert.fail("예외가 발생해야 한다 ");
    }

}