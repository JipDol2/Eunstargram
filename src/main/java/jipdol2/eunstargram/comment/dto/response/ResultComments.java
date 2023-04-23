package jipdol2.eunstargram.comment.dto.response;

import lombok.Getter;

@Getter
public class ResultComments <T>{

    T data;

    public ResultComments(){}

    public ResultComments(T data) {
        this.data = data;
    }
}
