package jpabook.jpashop.service;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
//@RequiredArgsConstructor
//중요: jpa 의 모든 데이터 변경이나 로직들은 가능하면
// 트랜잭션 안에서 실행되야한다 ==> lazy 로딩이 가능하다
//오픈섹션같은거는 나중에

//클래스 레벨에서 쓰면 public 하위에 메서드들에 트*젝이 걸리게 된다
// 뭐가잇냐면
//1 .javax
//2. spring -- 스프링에
// readOnly 가 성능향상 시키는 이유?
//https://willseungh0.tistory.com/75
//변경 을 위한 플러시를 하지 않아도 되고 이러한 변경을 감지하기위한 스냅샷을 유지하지 않아서
// 성능을 최적화 할수 있다
@Transactional(readOnly = true)
//@AllArgsConstructor
@RequiredArgsConstructor
//final 에 있는 필드만 가지고 생성자를 만들어준다
// rerquire 객체를 주입해주고 세팅이 끝나는거

public class MemberService {



    // 생성자 인젝션
    //  스프링올라갈떄 생성제에서 주입
    // -> 중간에 set 을 통해서 memberRepository 의 변경이 없다

    // 테스트 케이스 작성시 에서 mock 객체를 넣는둥 이런부분을 안 놓칠수 있다
    //    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }


//    @Autowired
    // 스프링에서 생성자가 하나만 있을때에는 위 어노테이션이 없어도 자동으로 주입을 해준다


    // 인젝션,,, 주입 해준다이
    // 스프링이 빈에 있는 repo를 찾아서 주입해줌
    // 필드 인젝션
    // 단점? 테스트하거나 할때 바꾸기가 어렵다
    //@Autowired 하는 거


    //final 선언시 컴파일 시에 값체크도 해준다
    private final MemberRepository memberRepository;

    //--> setter 인젝션은 만든다
    // 장점 ,, 테스트에서 mock 을 통해서 가짜 멤버 리포지토리 주입이 가능하다
    // 단점 실제 앱이 돌아가는 순간에 변경된다면-> 좋은게 없다?
    //==> 생성자 주입
    // 필드는 테스트에서 주입 하기 힘들수 있다

//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }



    /**
     * 회원가입
     *  검증사항 : 같은 이름의 회원이면 (중복검증) x
     */
    @Transactional
    // readonly false가 먹힌다
    public Long join(Member member){

        validateDuplicateMember(member);
        memberRepository.save(member);
        //save 메서드에서 em.persistence를 하면
        // 영속성 컨텍스트에 올려서
        //영속성컨텍스트는 키,value 가있는데
        //db에서 member_id pk 가되는데
        //db의 id값과 매핑한게 key 가 된다
        //아이디값이 항상 박혀들여 있따는게 persist 할때
        // 보장이 된다
        //컨텍스트는 key를 pk를 통해 매핑한다?

        return member.getId();
    }

    //가입을 하는데 이름을 통해서 찾아온 컬력션에서
    // 이미 가입요청을 한 회원이 있으면 안되니까
    // 그에 대한 검증
    private void validateDuplicateMember(Member member) {
        //EXCEPTION

        //강원도사는 A 와 서울사는 A씨가
        // 검증오류 was가 동시에 여러개가 뜨기에
        //db에 쌍 A 씨가 insert 하기위해 이 메서드를 호출한다면
        // 문제가 될수있따? member의네임을 유니크 제약 조건을 걸어주는게 좋겠다
        // 둘다 동시에 가입을할떄의 최후의 보루? 말보루 member.getName()
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }

    }


    //회원 전체 조회

    //jpa가 조회하는 곳에서는 성능을 최적화한다 ?
    // 더티체킹을 안한다 ?
    // 몇몇 드라이버에서는
    // 읽기전용 트랜잭션이다?
    // 리소스를 덜쓴다?

    // 읽기에는 가급적 readonly를 넣고
    // 변경이 있을때는 넣으면 x
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


    @Transactional
    // 변경감지에 의해 ,,, 감지 , 트랜잭션 시작
    // jpa 가 영속성 컨텍스트에서 없으니 db에서 끍어와 영컨에 저장한거를 반환 -> 영속상태의
    // member 를 setMember 로 저장 -> 종료되면  스프링 aop 가 동작, 트랜잭션 어노테이션에 의해
    // 끝나는 시점에 커밋 , 플러시 -> 쿼리 반영
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
        // 멤버를 업데이트해서 반환해도 되는데 애매하다 -> 영속 상태가 끊긴다
        //
    }
}
