package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M")
@Getter @Setter
public class Movie  extends Item {
    // 상속받으면서 fk를 item에 있는거를 받아오는데
    //각각의 item 들의 정보들(item,book,album) 을 item 의 fk 로 조회한다는 의미겠지
    // 그러면 이거를 어떻게 하나
    private String director;
    private String actor;

}
