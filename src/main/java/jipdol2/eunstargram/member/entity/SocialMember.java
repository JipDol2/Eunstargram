package jipdol2.eunstargram.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue(value = "SocialMember")
public class SocialMember extends Member{

    @Enumerated(EnumType.STRING)
    private SocialProvider socialProvider;
    private Long socialId;

    @Builder
    public SocialMember(String memberEmail, String password, String nickname, String phoneNumber, String birthDay, String intro,SocialProvider socialProvider,Long socialId) {
        super(memberEmail, password, nickname, phoneNumber, birthDay, intro);
        this.socialProvider = socialProvider;
        this.socialId = socialId;
    }
}
