package jpabook.jpashop;


import jpabook.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() throws Exception{
        //given
        Book book = em.find(Book.class, 1L);
        //tx --> jpa 가 변경본에 대해서 update 쿼리, 변경본 저장, 더티체킹
        book.setName("asdfasdf");

        //변경감지 == dirty chcking
        //then
    }
}
