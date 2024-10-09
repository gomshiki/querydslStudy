package org.example.querydslstudy.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@ToString(of = {"id", "name"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class Team {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // 연관관계 주인이 아닌면 mappedBy 사용
    private List<Member> member = new ArrayList<>();

    public Team(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return member;
    }
}
