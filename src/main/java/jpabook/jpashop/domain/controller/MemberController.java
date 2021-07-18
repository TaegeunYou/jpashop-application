package jpabook.jpashop.domain.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원 가입
     */
    @GetMapping("/members/new")
    public String createForm(Model model) {

        //controller에서 view로 넘어갈 때 데이터를 model에 실어서 넘긴다.
        //밸리데이션 등을 위해 빈 화면(빈 껍데기)이라도 들고 간다.
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    //Member 와 MemberForm을 따로 만든 이유는 서로 필요한 게 다를 수 있기 때문이다.
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {   //등록 폼을 채우지 않고 등록하여 에러가 발생한 경우
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }

    /**
     * 회원 목록
     */
    @GetMapping("/members")
    public String list(Model model) {
        List<Member> members = memberService.findMembers(); // 참고) 심플하게 엔티티를 수정하지 않고 이용할 게 아니면, Member엔티티를 직접 사용하지 말고 DTO로 변환해서 화면에 꼭 필요한 데이터들만 갖고 출력하면 좋다.
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
