package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//테이블을 orders 로 적어줘야 한다 , 없으면 --> 관례대로 Orders가 아니라
// order 로 테이블명이 작성 된다
@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class  Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 클래스이름에 따라서 id 값을 order_id, member-> member_id 이렇게 작성하는 이유?
    // 관례


     
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    /**
     * 외래키를 매핑할 떄 사용한다 name ="매핑할 외래 키 이름"
     */
    private Member member;
    // order 에서 member 의 변경이 있을때에 member_id fk 값이 다른 멤버로 변경이 된다
    // 연관관계의 주인설정을 통한 결과??

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();
    //엔티티당 각각 persist 해줘야한다
    //persist(orderItemA)
    //persist(orderItemB)
    //persist(orderItemC)
    //persist(order)
// cascadeType ==> persist(order) 가 가능하다
// persist를 전파한다? -->  a,b,c 를 컬렉션에 담아서 persistx3 해버리다
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;
    // 과거 Date 타입선언시에 이를 다른 날짜관련 어노테이션을 매핑 했어야 했지만
    // 현제 ldt는 하이버네이트가 자동 지원 해준다

    @Enumerated(EnumType.STRING)
    //ENUM 타입의 기본 ORDINAL, STRING
    // ORDINAL : enum 의 순서를 저장
    // STRING: ENUM의 이름을 DB에 저장
    private OrderStatus status; // 주문상태[ORDER,CANCEL]


    //연관관계 메서드?? , 위치는 컨트롤 하는쪽에
    //양쪽셋팅하는 것을 원자적으로 한코드로 해결한다
    // 비즈니스로직, 양방향 연관관계에 묶인다?
// public static void main(String[] args){
// Member member = new Member();

// Order order = new Order();

//    member.getOrders().add(order);

//    order.setMember(member);


    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        //delivery
        delivery.setOrder(this);
    }

    //생성 메서드
    // ... 문법-> 어러개 넣을수 있음
    //이메서드가 애초에 처음부터 밖에서 order를 set을통해서 값을
    // 넣어주는게
    // 생성할때부터 아예 createorder 를 무조건 호출을 해줘야한다
    //값을 쭉쭉넣어서 생성메서드에서 완성을 시킨다? , 응집을 시킨다?
    // 주문생성관련 해서 수정할거는 여기와서 고쳐?
    // https://jekal82.tistory.com/47 ... 문법 참고 하기
    public static Order createOrder(Member member, Delivery delivery,OrderItem... orderItems){

        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        //처음상태를 ORDER 강제해놓음
        order.setStatus(OrderStatus.ORDER);
        //현재시간으로
        order.setOrderDate(LocalDateTime.now());
        return order;

    }

    //==비즈니스 로직 ==
    /**
     * 주문 취소
     */

    public void cancel(){
        // 이미 배송된상품은 취소가 안된다
        // 비즈니스로직에 대한 체크가 엔티티 안에 있어?
        //READY,COMP 준비, 완료
        if (delivery.getStatus() ==DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다");
        }

        // 상태를 바꾸고 order ==> cancel
        this.setStatus(OrderStatus.CANCEL);
        // 여러 상품을 주문을 하기때문에 제고를 롤복? 을 할것이다
        // for 문을 돌면서
        // 오더 아이템내에서 캔슬 치켠서
        //cancel 메서드에서 아이템의 제고를 원복 시킨다
        for (OrderItem orderItem : orderItems) {
            // this를 잘쓰지 않는 이유? 색칠을 해주니까 큰ㅇ유가 있을까?
            // 강조할떄?나 이름이 같을때? 이부분은 스타일
            orderItem.cancel();
            //오더 아이템에도 캔슬을 해줘야한다  고객이 상품을 2개주문을 하면은
            // 아이템 2개에 각각 캔슬을 해준다

        }

    }

    //== 조회 로직==/
    /**
     전체 주문 가격 조회
     **/
//    public int getTotalPrice(){
//        int totalPrice = 0;
//        for (OrderItem orderItem : orderItems) {
//            totalPrice += orderItem.getTotalPrice();;
//
//        }
//        return totalPrice;
//        // 자바 스트림이나 람다를 통해서 깔끔하게 작성이 가능합니다
//        // 이거 어떻게 할까유? 몰라유
//    }

    public int getTotalPrice(){
        return orderItems.stream()
                .mapToInt(OrderItem :: getTotalPrice).sum();
    }
    //자바 8 스트림쪽 공부도 해야겠네요
}
