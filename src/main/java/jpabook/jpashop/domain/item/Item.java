package jpabook.jpashop.domain.item;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import jpabook.jpashop.domain.Category;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter@Setter
// 추상화할떄 일어나는 상황은 뭘까?
public abstract class Item {
    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    //setter 를 넣는게 아니라
    // stockQuantity를 변경할 일이 있으면  핵심 비즈니스 메서드를 가지고 해야한다
    private int stockQuantity;


    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<Category>();

    // 비즈니스로직 , 제고 관리, 재고가 늘고 줄고
    // 엔티티인 item 내의 stockQuantity 에서 로직을 가지고 있는게
    // 객체지향적이고, 응집도가 높다
    //이엔티티 밖에서 set 메서드를 통해서 밖에서 가져와 변경하는게 아니라
    // 엔티티 안에서 필요한 로직들을 하는게 가장 객체지향적

    /**
     * stock 증가
     */
    public void addStock(int quantity){
        this.stockQuantity+=quantity;
    }

    /**
     * stock 감소
     * 조건, stock 0보다 줄어들면안됨 - 검증
     */
    public void removeStock(int orderQuantity){

        int restStock = this.stockQuantity-orderQuantity;

        if (restStock<0){
            throw new NotEnoughStockException("need more stock");

        }
        this.stockQuantity = restStock;
    }
}