package jipdol2.eunstargram.auth.entity;

import jipdol2.eunstargram.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Builder
    public Session(Member member) {
        this.accessToken = UUID.randomUUID().toString();
        this.member = member;
    }
}
