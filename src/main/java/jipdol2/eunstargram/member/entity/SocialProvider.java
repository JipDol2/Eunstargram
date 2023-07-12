package jipdol2.eunstargram.member.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum SocialProvider {

    GITHUB("GITHUB"),
    GOOGLE("GOOGLE"),
    NAVER("NAVER"),
    KAKAO("KAKAO");

    private final String socialProviderName;

    private final static Map<String,SocialProvider> socialMap = new HashMap<>();

    static{
        Arrays.stream(SocialProvider.values())
                .forEach(s->socialMap.put(s.getSocialProviderName(),s));
    }

    public static SocialProvider from(String socialProviderName){
        return socialMap.get(socialProviderName.toUpperCase());
    }
}
