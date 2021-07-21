package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
// 참고) 경우에 따라서는 이렇게 위임만 하는 경우를 따로 만들어야 할 필요가 있을까 고민해봐야 함.
//      이럴때는 Controller 에서 바로 Repository 에 접근 하는 것도 문제없다.
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    //변경감지 : transactional이 끝나면서 commit할 때 flush하면서 영속성 컨텍스트에 있는 것 중에 변경된 것을 찾아 그것에 대한 업데이트 쿼리를 알아서 만들어 보낸다. 따라서 persist 안해줘도 된다.
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        //findItem.change(name, price, stockQuantity); 처럼 메서드를 만들어서 setter를 외부에서 사용하지 않는 방법이 유지보수에 좋음.
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
