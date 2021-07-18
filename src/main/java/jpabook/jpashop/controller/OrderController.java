package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    /**
     * 상품 주문
     */
    @GetMapping("/order")
    public String createForm(Model model) {

        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    // 참고) Controller 에서는 식별자(Id) 정도만 넘기고 Service 계층에서 비즈니스 로직을 처리한다. Service가 엔티티에 더 많이 의존하고(Transactional 안에서 엔티티가 영속상태로 흘러갈 수 있도록 -> 더티체킹가능) 할 수 있는게 더 많아지기 때문이다.
    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count) {

        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    /**
     * 주문 내역
     */
    @GetMapping("orders")
    public String orderList(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model) {  //@ModelAttribute 는 html Form 으로부터 정보를 받아 바인딩하고 Model에 정보를 담기도 한다.  model.addAttribute("orderSearch", orderSearch); 생략
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders", orders);   //key는 orders, value는 orders로 html에 값을 넘겨줌.

        return "order/orderList";
    }

    /**
     * 주문 내역 취소
     */
    @PostMapping("/orders/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/orders";
    }
}
