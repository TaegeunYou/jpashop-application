package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController //@Controller + @ResponseBody
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 회원 등록 API
     */

    @PostMapping("/api/v1/members")
    //엔티티를 파라미터로 받는 위험한 방식(엔티티를 외부에 노출)
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {   //요청받은 JSON 데이터를 member에 매핑해준다.
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PostMapping("/api/v2/members")
    //요청 값으로 Member 엔티티 대신에 별도의 DTO를 받는다.
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {  //api 스펙에 맞춰 객체를 만들 수 있어 유지보수에 좋다

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    /**
     * 회원 수정 API
     */

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id, request.getName());
        Member findMember = memberService.findMember(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor //모든 필드를 파라미터로 갖는 생성자
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

    /**
     * 회원 조회 API
     */

    //엔티티를 직접 반환하는 위험한 방식 -> 엔티티가 변경되면 API 스펙이 변한다 + API 스펙이 한정된다.
    @GetMapping("/api/v1/members")
    public List<Member> memberV1() {
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
//        List<MemberDto> memberDtoList = new ArrayList<>();
//        for (Member findMember : findMembers) {
//            memberDtoList.add(new MemberDto(findMember.getName()));
//        }
//        return new Result(memberDtoList);
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {    //API 는 하나의 배열로만 되어있는 스펙은 드물기 때문에 객체로 감싸준다. -> 유연성을 갖는다.
        private int count;
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {    //API 스펙이 DTO랑 일대일
        private String name;
    }
}
