package jipdol2.eunstargram.exception.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * {
 * "code": "400",
 * "message": "잘못된 요청입니다.",
 * "validation": {
 * "title": "값을 입력해주세요"
 * }
 * }
 */

@Getter
/**
 * @JsonInclude : 응답객체에 비어있는 객체가 존재한다면 그 부분을 제외할 수 있는 기능
 * -> 클라이언트쪽 요청에 따라 옵션을 조절하면 됨
 */
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}

