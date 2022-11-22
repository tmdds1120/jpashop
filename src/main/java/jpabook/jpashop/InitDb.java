package jpabook.jpashop;


import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;


/**
 * 총 주문 2개
 * userA
 * JPA1 BOOK
 * JPA2 BOOK
 *
 * userB
 * Spring1 BOOK
 * Spring2 BOOK
 *
 *
 * 사전에 컴포넌트 스캔을 통해서 스프링이 올라갈 시점에
 * 해당 클래스를 읽고 내부클래스로 생성한 InitService 를 통해서  db에 샘플 데이터를
 * 넣고 싶다
 * 그럴려면 entitiyManager 를 통해서 persist 해주는 방법이 있겠는데
 * 내부 클래스에 영속화 시키고 싶은 엔티티의 정보를 설정하고 persist 하면
 * jpa 가 관리하게 되는데
 * 관리를 하긴하는데 이거를 어느 시점에 적용되느냐가 또 문제가 될 것이다
 * 그래서 우리가 사용하는겟은
 * @PostConstruct 라는 어노테이션인데
 * 해당 어노테이션이 붙은 메서드 init() 이 내부 클래스의 initService.dbInit1() 을
 * 사전에 반영해주는 것인가 라고만 이해를 하고 있다
 */


@Component
@RequiredArgsConstructor
public class InitDb {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.dbInit1();
        initService.dbInit2();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{

        private final EntityManager em;




        public void dbInit1(){
            Member member1 = getMember("userA", "서울", "1", "1111");
            em.persist(member1);

            Book book1 = createBook("JPA1 BOOK",10000, 100);
            em.persist(book1);


            Book book2 = createBook("JPA2 BOOK",10000, 100);
            em.persist(book2);


            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);



            // 메서드 밖으로 뺴기 ctrl alt v
            Delivery delivery = getDelivery(member1);
            Order order = Order.createOrder(member1, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private static Delivery getDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

        private static Book createBook(String BookName, int Price, int stockQuantity) {
            Book book = new Book();

            book.setName(BookName);
            book.setPrice(Price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private static Member getMember(String name, String city, String street, String zipcode) {
            Member member1 = new Member();
            member1.setName(name);
            member1.setAddress(new Address(city, street, zipcode));
            return member1;
        }


        public void dbInit2(){
            Member member2 = getMember("userB", "부산", "2", "1232");
            em.persist(member2);

            Book book1 = createBook("SPRING1 BOOK",20000, 200);
            em.persist(book1);


            Book book2 = createBook("SPRING2 BOOK",40000, 300);
            em.persist(book2);


            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);



            // 메서드 밖으로 뺴기 ctrl alt v
            Delivery delivery = getDelivery(member2);
            Order order = Order.createOrder(member2, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

    }

}


