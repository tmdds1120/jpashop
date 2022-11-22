package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;


    @NotEmpty //
    private String name;

    //내장타임을 포함했다는 의미 -> 내장 타입이라는게 무엇이지 =
    // ed ,able 중 하나만 있으면 된다는데 어디에 무엇을 써야하지 -> 둘다 써도 문제 없으면 둘다써주는게 좋을거 같은데
    //참고 https://velog.io/@conatuseus/JPA-%EC%9E%84%EB%B2%A0%EB%94%94%EB%93%9C-%ED%83%80%EC%9E%85embedded-type-8ak3ygq8wo
    @Embedded
    private Address address;


    // order 테이블에 있는 Member 필드에 대해서 매핑 되었다
    //읽기 전용, 값을 넣어도 pk의 변경이 없다
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
