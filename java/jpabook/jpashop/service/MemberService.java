package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // JPA의 데이터 변경은 transaction안에서 일어나야 된다. // readOnly는 성능 최적화 // private메서드에는 transactional이 적용되지 않는다.
//@AllArgsConstructor   //모든 필드를 갖고 생성자를 만듦
@RequiredArgsConstructor    //final 필드를 갖고 생성자를 만듦 //생성자가 하나일 경우 @Autowired 생략 가능
public class MemberService {

    //컴파일 시점에 체크를 위해 final을 붙이기
    private final MemberRepository memberRepository;

//    //생성자 주입 //MemberRepository에 의존한다는 것을 알 수 있다.
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//
//    //필드 주입 //테스트할 경우 바꿔줄 수 없다.
//    @Autowired
//    private MemberRepository memberRepository;
//
//    //세터 주입 //동작 중간에 누군가가 바꿀 위험이 있다.
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional  //안에 선언된게 우선권을 가짐
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId(); //member를 생성할 때 영속성 컨텍스트가 id(PK)를 생성하므로 id는 항상 보장이 된다.
    }

    //동일한 이름은 회원가입을 막는 로직
    // 참고) 이름을 찾아서 0보다 큰지 확인하는 로직도 가능
    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());   // 참고) 멀티스레드를 고려하여 유니크제약조건을 사용하면 좋다.
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }

    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 한명 이름으로 조회
    public Member findMember(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {  //변경 감지
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
