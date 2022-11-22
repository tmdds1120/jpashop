package jpabook.jpashop.service;


import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly= true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;


    @Transactional
    //readOnly 면 저장이 안되서 오버라이딩 개념으로 수정이 가능하게
    public void saveItem(Item item){
        itemRepository.save(item);
    }

    @Transactional
    // 이미 영속상태
    // 트랜잭션 안에서 조회를 해야 영속?
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }


}
