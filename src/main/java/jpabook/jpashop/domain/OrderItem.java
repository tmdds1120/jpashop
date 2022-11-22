package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "order_item")
//protected OrderItem() {
//
//        }
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {
//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private Order order;


    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    private int orderPrice;// 주문 가격
    private int count;// 주문수량

    // 생성 메서드
    //d아 생성메서드들을 static 으로 선언한 이유 에 대해서도
    // 얼마를 이렇게 샀어
    // 한번 생각을 해봐야겠네
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        //이후에 해야할거는 아이템은 자신의 재고에 대해서 까줘야한다
        item.removeStock(count);
        return orderItem;
    }

    //비즈니스 로우직
    public void cancel(){
        //재고수량은 원복한다?
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문상품 전체 가격조회
     */
    public int getTotalPrice() {
        return getOrderPrice()*getCount();
    }


}