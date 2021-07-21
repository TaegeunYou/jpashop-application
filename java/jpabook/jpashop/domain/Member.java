package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

//    @NotEmpty     //API마다 스펙이 다르므로 위험하다.
    private String name;

//    @JsonIgnore   //엔티티에는 화면을 꾸리기 위한 로직이 들어가지 않게 구현하는 것이 좋다. //엔티티로 의존관계가 들어와야 하는데 이 경우는 반대로 의존관계가 나가는 입장이다. -> 양방향 의존관계는 애플리케이션을 수정하기 어려워 진다.
    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
