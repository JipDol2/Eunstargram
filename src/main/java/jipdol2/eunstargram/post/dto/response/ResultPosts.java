package jipdol2.eunstargram.post.dto.response;

import lombok.Getter;

/**
 * 왜 이 class 를 만들었을까?
 * - response 에 array 를 그대로 넘겨버리면 API 스팩이 변경되었을때 대처하기에
 *   매우 유연하지 못하다.
 *   예를 들어서 회원의 이름을 응답해주도록 array 를 반환해주는 API 가 있다고 가정해보자.
 *   여기서 클라이언트가 응답해주는 회원의 count 값도 같이 넘겨달라고 할때 매우 난감해진다.
 *   그래서 array 형태의 data 는 한번 더 class 를 사용하여 감싸주는게 좋다.
 *
 *   count 값도 같이 넘겨달라고 할때 밑에 class 에서 int count 라는 변수만 추가하면 되기때문
 *   참고 : 김영한 JPA 활용 2 - 회원조회 API 강의 참고
 */
@Getter
public class ResultPosts<T>{
    private T data;

    public ResultPosts(){
    }

    public ResultPosts(T data) {
        this.data = data;
    }
}
