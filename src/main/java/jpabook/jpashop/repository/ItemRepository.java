package jpabook.jpashop.repository;


import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
@Repository
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item){
        // 처음 아이템을 넣을때는 아이디가 없다,         새로 생성한 객체다? 신규 등록이다
        // persist 하면 영컨에서 아이디 값을 넣어준다

        if (item.getId()== null){
            em.persist(item);
            //아니면 이미 등록된거를 업데이트하는거랑 비슷허다
        } else {
            //merge 는 업데이트 비슷한건데 정확한거는 나중에 알려준다
            em.merge(item);
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i",Item.class)
                .getResultList();
    }
}
