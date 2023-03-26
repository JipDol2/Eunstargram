package jipdol2.eunstargram.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 프로젝트의 규모가 커지면 커질수록 다양한 상황에 대한 exception 이 발생한다.
 * 그때마다 새로운 exception 을 추가하게 된다면 너무나도 반복적이고 어마어마하게 코드양이
 * 늘어나게 된다.
 * 그래서 최상위 exception 추상 클래스를 선언하여 해결해 준다.
 * ExceptionController 에서 @ExceptionHandler 의 class 명을 JipDol2Exception 으로 변경
 */
@Getter
public abstract class JipDol2Exception extends RuntimeException{

    public final Map<String,String> validation = new HashMap<>();

    public JipDol2Exception(String message){
        super(message);
    }

    public JipDol2Exception(String message,Throwable cause){
        super(message,cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName,String message){
        validation.put(fieldName,message);
    }
}
